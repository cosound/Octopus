package com.chaos.octopus.agent.unit;

import static org.junit.Assert.*;

import java.util.List;

import com.chaos.octopus.commons.core.Task;
import org.junit.Test;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.commons.core.PluginDefinition;
import com.chaos.octopus.commons.core.TestPlugin;

public class AgentTest
{
	@Test
	public void addPlugin_Default_PluginIsAddedToSupporedPlugins() throws Exception
	{
		PluginDefinition plugin = new TestPlugin();
        ServerMock mock = new ServerMock();

        try(Agent agent = new Agent(mock, 1))
		{
			agent.addPlugin(plugin);
			
			List<PluginDefinition> plugins = agent.get_SupportedPlugins();
			assertEquals(1, plugins.size());
			assertEquals(plugin, plugins.get(0));
		}
	}
	
	@Test
	public void serializeSupportedPlugins_AgentSupportASinglePlugin_ReturnByteArray() throws Exception
	{
		PluginDefinition plugin = new TestPlugin();
        ServerMock mock = new ServerMock();

        try(Agent agent = new Agent(mock, 1))
		{
			agent.addPlugin(plugin);
			
			byte[] result = agent.serializeSupportedPlugins();
			
			assertEquals("com.chaos.octopus.agent.unit.TestPlugin, 1.0.0;", new String(result));
		}
	}
	
	@Test
	public void enqueueTask_GivenATask_ShouldBeAddedToTheQueue() throws Exception
	{
        ServerMock mock = new ServerMock();

        try(Agent agent = new Agent(mock, 1))
		{
            agent.addPlugin(new TestPlugin());
			
			agent.enqueue(Make_TestTaskThatTake10msToExecute());

			assertEquals(1, agent.getQueueSize());
		}
	}

	@Test
	public void executeTask_AgentIsStartedAndTaskQueued_TaskShouldBeExecuted() throws Exception
	{
        ServerMock mock = new ServerMock();

        try(Agent agent = new Agent(mock, 1))
		{
			TestPlugin factory = new TestPlugin();
			agent.addPlugin(factory);
			
			TestPlugin plugin = (TestPlugin) agent.enqueue(Make_TestTaskThatTake10msToExecute());

			assertFalse(plugin.WasExecuted);
			
			for(int i = 1000; i > 0 && !plugin.WasExecuted; i--)
			{
				Thread.sleep(1);
			}
			
			assertTrue(plugin.WasExecuted);
		}
	}

	@Test
	public void onTaskComplete_APluginIsExecuted_ShouldNotifyServer() throws Exception
	{
		ServerMock mock = new ServerMock();
		
		try(Agent agent = new Agent(mock, 1))
		{
			TestPlugin factory = new TestPlugin();
			agent.addPlugin(factory);
			
			TestPlugin plugin = (TestPlugin) agent.enqueue(Make_TestTaskThatTake10msToExecute());

			assertFalse(plugin.WasExecuted);
			
			for(int i = 1000; i > 0 && !mock.WasOnCompleteCalled; i--)
			{
				Thread.sleep(1);
			}
			
			assertTrue(mock.WasOnCompleteCalled);
		}
	}

    protected Task Make_TestTaskThatTake10msToExecute()
    {
        Task task = new Task();
        task.pluginId = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
        task.properties.put("sleep", "10");

        return task;
    }
}
