/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.exception.ConnectException;
import com.chaos.octopus.commons.exception.DisconnectError;
import com.chaos.sdk.v6.dto.ClusterState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentProxy {
  private final int port;
  private final String hostname;
  private List<String> _SupportedPlugins;
  private int _MaxNumberOfSimultaneousTasks;
  private Map<String, Task> _AllocatedTasks;

  public AgentProxy(String hostname, int port) {
    _AllocatedTasks = new HashMap<>();
    this.port = port;
    this.hostname = hostname;
  }

  public List<String> get_SupportedPlugins() {
    return _SupportedPlugins;
  }

  public void InitializeAgent() {
    Response<AgentConnectResult> response = sendRequest("Plugin/Get", new TypeToken<Response<AgentConnectResult>>() {
    }.getType());

    InitializeAgent(response);
  }

  private void InitializeAgent(Response<AgentConnectResult> response) {
    _SupportedPlugins = new ArrayList<>();

    for (String pluginId : response.Results.get(0).supportedPlugins)
      _SupportedPlugins.add(pluginId);

    _MaxNumberOfSimultaneousTasks = response.Results.get(0).masNumberOfSimultaneousTasks;
  }

  public void set_SupportedPlugins(List<String> supportedPlugins) {
    _SupportedPlugins = supportedPlugins;
  }

  public void enqueue(Task task) throws DisconnectError {
    _AllocatedTasks.put(task.taskId, task);

    String taskString = new Gson().toJson(task);

    sendRequest("Task/Enqueue", new KeyValue("task", taskString));
  }

  ClusterState.AgentState getAgentState() throws DisconnectError {
    AgentStateResult result = (AgentStateResult) sendRequest("State/Get",
            new TypeToken<Response<AgentStateResult>>() {
            }.getType()).Results.get(0);

    result.agentState.hostname = hostname;
    result.agentState.port = port;

    return result.agentState;
  }

  void taskCompleted(Task task) {
    _AllocatedTasks.remove(task.taskId);
  }

  boolean isQueueFull() {
    return _MaxNumberOfSimultaneousTasks - _AllocatedTasks.size() == 0;
  }

  public ClusterState.AgentState getState() {
    ClusterState.AgentState state = new ClusterState.AgentState();

    try {
      String stateString = new Gson().toJson(state);

      Response<AgentStateResult> result = sendRequest("State/Get",
              new TypeToken<Response<AgentStateResult>>() {
              }.getType(),
              new KeyValue("state", stateString));

      state = result.Results.get(0).agentState;
      state.state = "Connected";
    } catch (DisconnectError e) {
      state.state = "Disconnected";
    } catch (ConnectException e) {
      state.state = "Disconnected";
    } catch (Exception e) {
      state.state = "Disconnected";
    }

    return state;
  }

  String getHostname() {
    return hostname;
  }

  private <T> Response<T> sendRequest(String endpoint, KeyValue... parameters) {
    String queryString = "";

    for (KeyValue entry : parameters)
      queryString += String.format("%1s=%2s&", entry.key, entry.value);

    return sendRequest(String.format("GET /%1s/?%2s HTTP/1.1", endpoint, queryString),
            new TypeToken<Response>() {
            }.getType(),
            10);
  }

  private <T> Response<T> sendRequest(String endpoint, Type t, KeyValue... parameters) {
    String queryString = "";

    for (KeyValue entry : parameters)
      queryString += String.format("%1s=%2s&", entry.key, entry.value);

    return sendRequest(String.format("GET /%1s/?%2s HTTP/1.1", endpoint, queryString), t, 10);
  }

  private <T> Response<T> sendRequest(String message, Type t, int retries) {

		try (Socket socket = new Socket(hostname, port)) {
			socket.getOutputStream().write(message.getBytes());

      long timeout = System.currentTimeMillis() + 5000;

      while (socket.getInputStream().available() == 0 && timeout > System.currentTimeMillis()) {
        Thread.sleep(1);
      }

      String reaponseString = "";

      while (socket.getInputStream().available() != 0) {
        byte[] buffer = new byte[socket.getInputStream().available()];
        socket.getInputStream().read(buffer);

        reaponseString += new String(buffer);
      }

      String content = reaponseString.substring(reaponseString.indexOf("\n\n") + 2);

      return new Gson().fromJson(content, t);
    } catch (java.net.ConnectException e) {
			throw new com.chaos.octopus.commons.exception.ConnectException(
              "Connection to Orchestrator could not be established, check hostname and port", e);
    } catch (Exception e) {
      if (retries > 0) {
				sleep(100);
        sendRequest(message, t, --retries);
      }

      System.err.println("Couldn't connect to: " + "" + ":" + "");
      e.printStackTrace();

      throw new com.chaos.octopus.commons.exception.ConnectException("Failed to call Agent after multiple attempts", e);
    }
  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e1) {

    }
  }
}
