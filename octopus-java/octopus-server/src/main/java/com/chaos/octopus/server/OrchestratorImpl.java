/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.core.message.ConnectMessage;
import com.chaos.octopus.commons.core.message.Message;
import com.chaos.octopus.commons.exception.ConnectException;
import com.chaos.octopus.commons.http.SimpleServer;
import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.chaos.octopus.server.synchronization.EnqueueJobs;
import com.chaos.octopus.server.synchronization.Heartbeat;
import com.chaos.octopus.server.synchronization.Synchronization;
import com.chaos.octopus.server.synchronization.UpdateJob;
import com.chaos.sdk.AuthenticatedChaosClient;
import com.chaos.sdk.Chaos;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class OrchestratorImpl implements Orchestrator {
  // keeps track of the jobs thqt need to be updated
  // listens for packets from the agents
  // Parses the message and decides how to handle it
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
    _simpleServer.addEndpoint("Task/Update", new TaskUpdateEndpoint());
    _simpleServer.addEndpoint("Task/Complete", new TaskCompleteEndpoint());
    _simpleServer.addEndpoint("Agent/Connect", new AgentConnectEndpoint());
  }

  public static OrchestratorImpl create(OctopusConfiguration config) throws IOException {
    Synchronization sync = new Synchronization();
    ConcurrentJobQueue queue = new ConcurrentJobQueue();
    OrchestratorImpl leader = new OrchestratorImpl(config.getListeningPort(), sync, queue);

    Chaos chaos = new Chaos(config.getChaosApiUrl());
    AuthenticatedChaosClient client = chaos.authenticate(config.getChaosApiKey());
    sync.addSynchronizationTask(new UpdateJob(queue, client));
    sync.addSynchronizationTask(new EnqueueJobs(leader, client));
    sync.addSynchronizationTask(new Heartbeat(leader._AllocationHandler, client));

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

  private class TaskUpdateEndpoint implements Endpoint {
    public Response invoke(Request request) {
      String taskJson = request.queryString.get("task");

      Task task = StreamUtilities.ReadJson(taskJson, Task.class);

      taskUpdate(task);

      return new Response();
    }
  }

  private class TaskCompleteEndpoint implements Endpoint{
    public Response invoke(Request request) {
      String taskJson = request.queryString.get("task");
      Task task = StreamUtilities.ReadJson(taskJson, Task.class);

      taskCompleted(task);

      return new Response();
    }
  }

  private class AgentConnectEndpoint implements Endpoint {
    public Response invoke(Request request) {
      String hostname = request.queryString.get("hostname");
      int port = Integer.parseInt(request.queryString.get("port"));

      try {
        AgentProxy ap = new AgentProxy(hostname, port);
        ap.InitializeAgent();

        _AllocationHandler.addAgent(ap);
      } catch (ConnectException e) {
        System.err.println("Connection to Agent could not be established, hostname: " + hostname + ", port: " + port);
        e.printStackTrace();
      }

      return new Response();
    }
  }
}
