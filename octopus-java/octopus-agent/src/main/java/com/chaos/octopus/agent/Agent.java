/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.agent;

import com.chaos.octopus.agent.endpoint.*;
import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.exception.DisconnectError;
import com.chaos.octopus.commons.http.SimpleServer;

import java.io.IOException;

public class Agent implements AutoCloseable, TaskStatusChangeListener {
  private ExecutionHandler _executionHandler;
  private Orchestrator _orchestrator;
  private PluginFactory _pluginFactory;
  private SimpleServer _simpleServer;

  public Agent(String orchestratorHostname, int orchestratorPort, int listenPort) {
    this(new OrchestratorProxy(orchestratorHostname, orchestratorPort, listenPort), 4);
  }

  public Agent(String orchestratorHostname, int orchestratorPort, int listenPort, int parrallelism) {
    this(new OrchestratorProxy(orchestratorHostname, orchestratorPort, listenPort), parrallelism);
  }

  public Agent(Orchestrator orchestrator, int parallelism) {
    _executionHandler = new ExecutionHandler(this, parallelism);
    _pluginFactory = new PluginFactory();
    _orchestrator = orchestrator;
    _simpleServer = new SimpleServer(_orchestrator.get_localListenPort());
    _simpleServer.addEndpoint("Task/Enqueue", new TaskEnqueueEndpoint(this));
    _simpleServer.addEndpoint("State/Get", new StateGetEndpoint(_executionHandler));
    _simpleServer.addEndpoint("Plugin/Get", new PluginGetEndpoint(_executionHandler, _pluginFactory));
  }

  public static Agent create(OctopusConfiguration config) {
    return new Agent(config.getOrchestratorIp(), config.getOrchestratorPort(), config.getListeningPort(), config.getNumberOfParallelTasks());
  }

  public void open() throws IOException {
    try {
      _orchestrator.open();
    } catch (DisconnectError e) {
      System.out.println(Thread.currentThread().getId() + " AGENT DISCONNECTED");
      try {
        close();
      } catch (Exception ex) {
        e.printStackTrace();
      }
    }
  }

  public void close() throws Exception {
    _simpleServer.stop();
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
