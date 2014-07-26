/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.exception.ConnectException;
import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.chaos.octopus.server.synchronization.EnqueueJobs;
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

public class OrchestratorImpl implements Orchestrator, Runnable {
  // keeps track of the jobs thqt need to be updated
  // listens for packets from the agents
  // Parses the messages and decides how to handle it
  // Contains the synchronization
  private final ConcurrentJobQueue _jobsWithUpdates;
  private boolean _isRunning = false;
  private Thread _thread;
  private ServerSocket _socket;
  private int _port;
  private AllocationHandler _AllocationHandler;
  private Synchronization _synchronization;

  public OrchestratorImpl(int port) {
    this(port, new Synchronization(), new ConcurrentJobQueue());
  }

  private OrchestratorImpl(int listeningPort, Synchronization sync, ConcurrentJobQueue queue) {
    _AllocationHandler = new AllocationHandler();
    _port = listeningPort;

    _jobsWithUpdates = queue;
    _synchronization = sync;
  }

  public static OrchestratorImpl create(OctopusConfiguration config) throws IOException {
    Synchronization sync = new Synchronization();
    ConcurrentJobQueue queue = new ConcurrentJobQueue();
    OrchestratorImpl leader = new OrchestratorImpl(config.getListeningPort(), sync, queue);

    Chaos chaos = new Chaos(config.getChaosApiUrl());
    AuthenticatedChaosClient client = chaos.authenticate(config.getChaosApiKey());
    sync.addSynchronizationTask(new UpdateJob(queue, client));
    sync.addSynchronizationTask(new EnqueueJobs(leader, client));

    return leader;
  }

  public ArrayList<AgentProxy> getAgents() {
    return _AllocationHandler.getAgents();
  }

  public void open() {
    try {
      _socket = new ServerSocket(_port);
      _isRunning = true;

      _thread = new Thread(this);
      _thread.setName("Orchestrator");
      _thread.start();

      // todo: move interval to configuration
      _synchronization.synchronize(30 * 1000); // synchronize every 30 seconds
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    while (_isRunning) {
      try (Socket agent = _socket.accept()) {
        String result = StreamUtilities.ReadString(agent.getInputStream());

        Message message = Message.createFromJson(result);

        // todo: refactor switch, perhaps using the specification pattern.
        switch (message.getAction()) {
          case Commands.CONNECT: {
            ConnectMessage connect = ConnectMessage.createFromJson(result);

            try {
              AgentProxy agentProxy = new AgentProxy(connect.get_Hostname(), connect.get_Port());

              _AllocationHandler.addAgent(agentProxy);
            } catch (ConnectException e) {
              System.err.println("Connection to Agent could not be established, hostname: " + connect.get_Hostname() + ", port: " + connect.get_Port());
              e.printStackTrace();
            }

            break;
          }
          case Commands.TASK_DONE: {
            TaskMessage taskMessage = TaskMessage.createFromJson(result);

            taskCompleted(taskMessage.getTask());

            break;
          }
          case Commands.TASK_UPDATE: {
            TaskMessage taskMessage = TaskMessage.createFromJson(result);

            taskUpdate(taskMessage.getTask());

            break;
          }
        }
      } catch (SocketException se) {
        // if the socket is closed it means the server is turned off, so we can ignore the exception
        if (!_socket.isClosed()) se.printStackTrace();
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
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
    _isRunning = false;

    if (_socket != null) _socket.close();
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
}
