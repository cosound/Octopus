/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.AgentConfigurationMessage;
import com.chaos.octopus.commons.core.message.AgentStateMessage;
import com.chaos.octopus.commons.core.message.Message;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.message.TaskMessage;
import com.chaos.octopus.commons.exception.ConnectException;
import com.chaos.octopus.commons.exception.DisconnectError;
import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.NetworkingUtil;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.chaos.sdk.v6.dto.ClusterState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentProxy {
  private final String _hostname;
  private List<String> _SupportedPlugins;
  private int _MaxNumberOfSimultaneousTasks;
  private Map<String, Task> _AllocatedTasks;
  private NetworkingUtil _network;

  public AgentProxy(String hostname, int port) {
    _hostname = hostname;
    _AllocatedTasks = new HashMap<>();
    _network = new NetworkingUtil(hostname, port);
  }

  public List<String> get_SupportedPlugins() {
    return _SupportedPlugins;
  }

  public void InitializeAgent() {
    String msg = Message.createWithAction(Commands.LIST_SUPPORTED_PLUGINS).toJson();
    String responseString = _network.sendWithReply(msg);

    AgentConfigurationMessage response = AgentConfigurationMessage.create(responseString);

    InitializeAgent(response);
  }

  private void InitializeAgent(AgentConfigurationMessage response) {
    _SupportedPlugins = new ArrayList<>();

    for (String pluginId : response.getSupportedPlugins())
      _SupportedPlugins.add(pluginId);

    _MaxNumberOfSimultaneousTasks = response.getNumberOfSimulataniousTasks();
  }

  public void set_SupportedPlugins(List<String> supportedPlugins) {
    _SupportedPlugins = supportedPlugins;
  }

  private Object _EnqueueBlock = new Object();

  public void enqueue(Task task) throws DisconnectError {
    synchronized (_EnqueueBlock) {
      try {
        String msg = new TaskMessage(Commands.ENQUEUE_TASK, task).toJson();
        String response = _network.sendWithReply(msg);
        Message parsedResponse = StreamUtilities.ReadJson(response, Message.class);

        if (!parsedResponse.getAction().equals("OK")) throw new IOException("Agent didnt queue task");
        _AllocatedTasks.put(task.taskId, task);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void taskCompleted(Task task) {
    _AllocatedTasks.remove(task.taskId);
  }

  public boolean isQueueFull() {
    synchronized (_EnqueueBlock) {
      return _MaxNumberOfSimultaneousTasks - _AllocatedTasks.size() == 0;
    }
  }

  public NetworkingUtil get_network() {
    return _network;
  }

  public ClusterState.AgentState getState() {
    ClusterState.AgentState state = new ClusterState.AgentState();

    try{
      String response = _network.sendWithReply(new AgentStateMessage().toJson());
      state = AgentStateMessage.createFromJson(response).getState();
      state.state = "Connected";
    }catch (DisconnectError e){
      state.state = "Disconnected";
    } catch (ConnectException e){
      state.state = "Disconnected";
    } catch (Exception e){
      state.state = "Disconnected";
    }

    return state;
  }

  public String getHostname() {
    return _hostname;
  }
}
