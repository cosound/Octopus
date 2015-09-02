package com.chaos.octopus.agent.action;

import com.chaos.octopus.agent.PluginFactory;
import com.chaos.octopus.commons.core.AgentConfigurationMessage;
import com.chaos.octopus.commons.core.PluginDefinition;
import com.chaos.octopus.commons.util.NetworkingUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class ListSupportedPluginsAction implements AgentAction {
  private final PluginFactory pluginFactory;
  private final int parallelism;

  public ListSupportedPluginsAction(PluginFactory pluginFactory, int parallelism){
    this.pluginFactory = pluginFactory;
    this.parallelism = parallelism;
  }

  public void invoke(String message, OutputStream out) throws IOException {
    AgentConfigurationMessage response = createAgentConfigurationMessage();

    NetworkingUtil.send(response.toJson(), out);
  }

  private AgentConfigurationMessage createAgentConfigurationMessage() {
    AgentConfigurationMessage message = new AgentConfigurationMessage();

    message.setNumberOfSimulataniousTasks(parallelism);

    for (PluginDefinition definition : get_SupportedPlugins())
      message.getSupportedPlugins().add(definition.getId());

    return message;
  }

  public List<PluginDefinition> get_SupportedPlugins() {
    List<PluginDefinition> list = new ArrayList<>();

    for (PluginDefinition definition : pluginFactory.get_SupportedPlugins())
      list.add(definition);

    return list;
  }
}
