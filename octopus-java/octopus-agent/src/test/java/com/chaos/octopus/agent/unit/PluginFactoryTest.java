package com.chaos.octopus.agent.unit;

import com.chaos.octopus.agent.PluginFactory;
import com.chaos.octopus.commons.core.PluginDefinition;
import com.chaos.octopus.commons.core.TestPlugin;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PluginFactoryTest {
  @Test
  public void addPlugin_Default_PluginIsAddedToSupporedPlugins() throws Exception {
    PluginFactory pf = new PluginFactory();

    pf.addPlugin(new TestPlugin());

    List<PluginDefinition> plugins = pf.get_SupportedPlugins();
    assertEquals(1, plugins.size());
  }
}
