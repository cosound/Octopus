/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.agent;

import com.chaos.octopus.agent.action.AgentAction;
import com.chaos.octopus.agent.action.AgentStateAction;
import com.chaos.octopus.agent.action.EnqueueTaskAction;
import com.chaos.octopus.agent.action.ListSupportedPluginsAction;
import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.core.message.Message;
import com.chaos.octopus.commons.exception.DisconnectError;
import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Agent implements Runnable, AutoCloseable, TaskStatusChangeListener {
  private boolean _isRunning;
  private Thread _thread;
  private ExecutionHandler _executionHandler;
  private Orchestrator _orchestrator;
  private ServerSocket _Server;
  private Map<String, AgentAction> _agentActions = new HashMap<>();
  private PluginFactory _pluginFactory;

  public Agent(String orchestratorHostname, int orchestratorPort, int listenPort) {
    this(new OrchestratorProxy(orchestratorHostname, orchestratorPort, listenPort), 4);
  }

  public Agent(String orchestratorHostname, int orchestratorPort, int listenPort, int parrallelism) {
    this(new OrchestratorProxy(orchestratorHostname, orchestratorPort, listenPort), parrallelism);
  }

  public Agent(Orchestrator orchestrator, int parallelism) {
    _orchestrator = orchestrator;
    _isRunning = false;
    _thread = new Thread(this);
    _thread.setName("Agent");
    _executionHandler = new ExecutionHandler(this, parallelism);

    _pluginFactory =  new PluginFactory();

    _agentActions.put(Commands.LIST_SUPPORTED_PLUGINS, new ListSupportedPluginsAction(_pluginFactory, parallelism));
    _agentActions.put(Commands.ENQUEUE_TASK, new EnqueueTaskAction(this));
    _agentActions.put(Commands.AGENT_STATE, new AgentStateAction(_executionHandler));
  }

  public static Agent create(OctopusConfiguration config) {
    return new Agent(config.getOrchestratorIp(), config.getOrchestratorPort(), config.getListeningPort(), config.getNumberOfParallelTasks());
  }

  public void open() throws IOException {
    try {
      _orchestrator.open();

      _Server = new ServerSocket(_orchestrator.get_localListenPort());
      _isRunning = true;
      _thread.start();
    } catch (DisconnectError e) {
      System.out.println(Thread.currentThread().getId() + " AGENT DISCONNECTED");
      try {
        close();
      } catch (Exception ex) {
        e.printStackTrace();
      }
    }

  }

  public void run() {
    while (_isRunning) {
      try {
        // todo refactor so the implementation doesn't depend on the socket
        try (Socket socket = _Server.accept()) {
          String message = StreamUtilities.ReadString(socket.getInputStream());

          Message msg = StreamUtilities.ReadJson(message, Message.class);

          _agentActions.get(msg.getAction()).invoke(message, socket.getOutputStream());
        }
      } catch (Exception e) {
        if (!_Server.isClosed()) e.printStackTrace();
      }
    }
  }

  public void close() throws Exception {
    _isRunning = false;

    if (_Server != null) _Server.close();
    if (_executionHandler != null) _executionHandler.close();
  }

  public void addPlugin(PluginDefinition pluginFactory) {
    _pluginFactory.addPlugin(pluginFactory);
  }

  public Plugin enqueue(Task task) {
    Plugin plugin = _pluginFactory.create(task);

    _executionHandler.enqueue(plugin);

    return plugin;
  }

  public void onTaskComplete(Task task) {
    _orchestrator.taskCompleted(task);
  }
  public void onTaskUpdate(Task task) {
    _orchestrator.taskUpdate(task);
  }

  public int getQueueSize() {
    return _executionHandler.getQueueSize();
  }
}
