/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.agent;

import com.chaos.octopus.agent.action.AgentAction;
import com.chaos.octopus.agent.action.AgentStateAction;
import com.chaos.octopus.agent.action.ListSupportedPluginsAction;
import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.exception.DisconnectError;
import com.chaos.octopus.commons.http.SimpleServer;
import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.NetworkingUtil;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.chaos.sdk.v6.dto.ClusterState;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    _orchestrator = orchestrator;
    _executionHandler = new ExecutionHandler(this, parallelism);

    _pluginFactory =  new PluginFactory();

    _simpleServer = new SimpleServer(_orchestrator.get_localListenPort());
    _simpleServer.addEndpoint("Task/Enqueue", new TaskEnqueueEndpoint());
    _simpleServer.addEndpoint("State/Get", new StateGetEndpoint());
    _simpleServer.addEndpoint("Plugin/Get", new PluginGetEndpoint());
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

  private class TaskEnqueueEndpoint implements Endpoint {
    public Response invoke(Request request) {
      String taskJson = request.queryString.get("task");

      Task task = StreamUtilities.ReadJson(taskJson, Task.class);

      enqueue(task);

      // todo add proper OK response
      return new Response();
    }
  }

  private class StateGetEndpoint implements Endpoint {
    public Response invoke(Request request) {
      ClusterState.AgentState state = new ClusterState.AgentState();
      state.runningSize = _executionHandler.getQueueSize() > _executionHandler.getParallelism()
          ? _executionHandler.getParallelism()
          : _executionHandler.getQueueSize();
      state.queueSize = _executionHandler.getQueueSize();
      state.parallelism = _executionHandler.getParallelism();

      Response<AgentStateResult> response = new Response<>();
      response.Results.add(new AgentStateResult(state));

      return response;
    }
  }

  private class PluginGetEndpoint implements Endpoint {
    public Response invoke(Request request) {
      AgentConfigurationMessage response = createAgentConfigurationMessage();

      Response res = new Response();
      AgentConnectResult result = new AgentConnectResult();
      res.Results.add(result);

      for (String s:response.getSupportedPlugins())
        result.supportedPlugins.add(s);

      result.masNumberOfSimultaneousTasks = _executionHandler.getParallelism();

      return res;
    }

    private AgentConfigurationMessage createAgentConfigurationMessage() {
      AgentConfigurationMessage message = new AgentConfigurationMessage();

      message.setNumberOfSimulataniousTasks(_executionHandler.getParallelism());

      for (PluginDefinition definition : get_SupportedPlugins())
        message.getSupportedPlugins().add(definition.getId());

      return message;
    }

    public List<PluginDefinition> get_SupportedPlugins() {
      List<PluginDefinition> list = new ArrayList<>();

      for (PluginDefinition definition : _pluginFactory.get_SupportedPlugins())
        list.add(definition);

      return list;
    }
  }
}
