package com.chaos.octopus.agent.unit;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.agent.Plugin;

public class AgentTest
{
	@Test
	public void addPlugin_Default_PluginIsAddedToSupporedPlugins() throws Exception
	{
		Plugin plugin = new TestPlugin();
		
		try(Agent agent = new Agent("",0))
		{
			agent.addPlugin(plugin);
			
			List<Plugin> plugins = agent.get_SupportedPlugins();
			assertEquals(1, plugins.size());
			assertEquals(plugin, plugins.get(0));
		}
	}
	
	@Test
	public void serializeSupportedPlugins_AgentSupportASinglePlugin_ReturnByteArray() throws Exception
	{
		Plugin plugin = new TestPlugin();
		
		try(Agent agent = new Agent("",0))
		{
			agent.addPlugin(plugin);
			
			byte[] result = agent.serializeSupportedPlugins();
			
			assertEquals("com.chaos.octopus.agent.unit.TestPlugin, 1.0.0;", new String(result));
		}
	}
}
