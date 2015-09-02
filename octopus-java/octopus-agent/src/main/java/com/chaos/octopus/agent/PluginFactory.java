package com.chaos.octopus.agent;

import com.chaos.octopus.commons.core.Plugin;
import com.chaos.octopus.commons.core.PluginDefinition;
import com.chaos.octopus.commons.core.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginFactory {
  private final Map<String, PluginDefinition> pluginDefinitions;

  public PluginFactory(){
    this.pluginDefinitions = new HashMap<>();
  }

  public Plugin create(Task task){
    return pluginDefinitions.get(task.pluginId).create(task);
  }

  public void addPlugin(PluginDefinition definition) {
    pluginDefinitions.put(definition.getId(), definition);
  }

  public List<PluginDefinition> get_SupportedPlugins() {
    List<PluginDefinition> list = new ArrayList<>();

    for (PluginDefinition definition : pluginDefinitions.values())
      list.add(definition);

    return list;
  }
}
