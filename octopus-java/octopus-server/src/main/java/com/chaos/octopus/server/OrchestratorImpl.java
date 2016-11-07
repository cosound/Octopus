/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.http.SimpleServer;
import com.chaos.octopus.server.endpoint.*;
import com.chaos.octopus.server.synchronization.*;
import com.chaos.sdk.AuthenticatedChaosClient;
import com.chaos.sdk.Chaos;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class OrchestratorImpl implements Orchestrator {
  // keeps track of the jobs that need to be updated
  // Contains the synchronization
  private final ConcurrentJobQueue _jobsWithUpdates;
  private int _port;
  private AllocationHandler _AllocationHandler;
  private Synchronization _synchronization;
  private SimpleServer _simpleServer;

  public OrchestratorImpl(int port) {
    this(port, new Synchronization(), new ConcurrentJobQueue());
  }

  private OrchestratorImpl(int listeningPort, Synchronization sync, ConcurrentJobQueue queue) {
    _AllocationHandler = new AllocationHandler();
    _port = listeningPort;

    _jobsWithUpdates = queue;
    _synchronization = sync;

    _simpleServer = new SimpleServer(listeningPort);
    _simpleServer.addEndpoint("Task/Update", new TaskUpdateEndpoint(this));
    _simpleServer.addEndpoint("Task/Complete", new TaskCompleteEndpoint(this));
    _simpleServer.addEndpoint("Agent/Connect", new AgentConnectEndpoint(_AllocationHandler));
    try {
      _simpleServer.addEndpoint("Heartbeat", new HeartbeatEndpoint(_AllocationHandler, listeningPort, Inet4Address.getLocalHost().getHostAddress()));
    } catch (UnknownHostException e) {
      _simpleServer.addEndpoint("Heartbeat", new HeartbeatEndpoint(_AllocationHandler, listeningPort, "Unknown Hostname"));
    }
    _simpleServer.addEndpoint("Job/Enqueue", new JobEnqueueEndpoint(this));
  }

  public static OrchestratorImpl create(OctopusConfiguration config) throws IOException {
    Synchronization sync = new Synchronization();
    ConcurrentJobQueue queue = new ConcurrentJobQueue();
    OrchestratorImpl leader = new OrchestratorImpl(config.getListeningPort(), sync, queue);

    if(config.getChaosApiUrl() != null && config.getChaosApiKey() != null)
    {
      Chaos chaos = new Chaos(config.getChaosApiUrl());
      AuthenticatedChaosClient client = chaos.authenticate(config.getChaosApiKey());
      sync.addSynchronizationTask(new UpdateJob(queue, client));
      sync.addSynchronizationTask(new EnqueueJobs(leader, client));
      sync.addSynchronizationTask(new Heartbeat(leader._AllocationHandler, client));
    }
    else
    {
      sync.addSynchronizationTask(new StandaloneUpdateJob(queue));
    }

    return leader;
  }

  public ArrayList<AgentProxy> getAgents() {
    return _AllocationHandler.getAgents();
  }

  public void open() {
    // todo: move interval to configuration
    _synchronization.synchronize(30 * 1000); // synchronize every 30 seconds
  }

  @Override
  public void taskCompleted(Task task) {
    try {
      Job job = _AllocationHandler.getJob(task);
      _jobsWithUpdates.put(job);

      _AllocationHandler.taskComplete(task);
    } catch (ArrayIndexOutOfBoundsException e) {
      // No job found
    }
  }

  @Override
  public void taskUpdate(Task task) {
    try {
      Job job = _AllocationHandler.getJob(task);
      _jobsWithUpdates.put(job);

      _AllocationHandler.taskUpdate(task);
    } catch (ArrayIndexOutOfBoundsException e) {
      // No job found
    }
  }

  @Override
  public int get_localListenPort() {
    return _port;
  }

  @Override
  public void enqueue(Job job) {
    if (_jobsWithUpdates.contains(job.id)) return;

    _AllocationHandler.enqueue(job);
  }

  public void close() throws Exception {
    _simpleServer.stop();

    if (_AllocationHandler != null) _AllocationHandler.close();
    if (_synchronization != null) _synchronization.stop();
  }

  public List<String> parsePluginList(byte[] data) {
    String s = new String(data);
    ArrayList<String> pluginDefinitions = new ArrayList<String>();

    for (String id : s.split(";"))
      pluginDefinitions.add(id);

    return pluginDefinitions;
  }

  public Synchronization get_synchronization() {
    return _synchronization;
  }

  public boolean getIsRunning(){
    return _simpleServer.getIsRunning();
  }

  private static class StandaloneUpdateJob implements SynchronizationTask {
    private ConcurrentJobQueue queue;

    public StandaloneUpdateJob(ConcurrentJobQueue queue) {
      this.queue = queue;
    }

    public void action() {
      for (Job job:queue.popAll()) {

      }
    }
  }
}
