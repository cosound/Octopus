package com.chaos.octopus.agent.endpoint;

import com.chaos.octopus.agent.ExecutionHandler;
import com.chaos.octopus.agent.PluginFactory;
import com.chaos.octopus.commons.core.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesper on 30-06-2016.
 */
public class PluginGetEndpoint implements Endpoint {

  private ExecutionHandler executionHandler;
  private PluginFactory pluginFactory;

  public PluginGetEndpoint(ExecutionHandler executionHandler, PluginFactory pluginFactory) {
    this.executionHandler = executionHandler;
    this.pluginFactory = pluginFactory;
  }

  public Response invoke(Request request) {
    AgentConnectResult result = new AgentConnectResult();

    for (PluginDefinition definition : get_SupportedPlugins())
      result.supportedPlugins.add(definition.getId());

    result.masNumberOfSimultaneousTasks = executionHandler.getParallelism();

    return new Response(result);
  }

  public List<PluginDefinition> get_SupportedPlugins() {
    List<PluginDefinition> list = new ArrayList<>();

    for (PluginDefinition definition : pluginFactory.get_SupportedPlugins())
      list.add(definition);

    return list;
  }
}
