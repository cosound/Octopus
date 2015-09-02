package com.chaos.octopus.agent.unit;

import static org.junit.Assert.*;

import java.util.List;

import com.chaos.octopus.commons.core.*;
import org.junit.Test;

import com.chaos.octopus.agent.Agent;

public class AgentTest {
  @Test
  public void addPlugin_Default_PluginIsAddedToSupporedPlugins() throws Exception {
    PluginDefinition plugin = new TestPlugin();
    ServerMock mock = new ServerMock();

    try (Agent agent = new Agent(mock, 1)) {
      agent.addPlugin(plugin);

      List<PluginDefinition> plugins = agent.get_SupportedPlugins();
      assertEquals(1, plugins.size());
      assertEquals(plugin, plugins.get(0));
    }
  }

  @Test
  public void serializeSupportedPlugins_AgentSupportASinglePlugin_ReturnByteArray() throws Exception {
    PluginDefinition plugin = new TestPlugin();
    ServerMock mock = new ServerMock();

    try (Agent agent = new Agent(mock, 1)) {
      agent.addPlugin(plugin);

      byte[] result = agent.serializeSupportedPlugins();

      assertEquals("com.chaos.octopus.agent.unit.TestPlugin, 1.0.0;", new String(result));
    }
  }

  @Test
  public void enqueueTask_GivenATask_ShouldBeAddedToTheQueue() throws Exception {
    ServerMock mock = new ServerMock();

    try (Agent agent = new Agent(mock, 1)) {
      agent.addPlugin(new TestPlugin());

      agent.enqueue(Make_TestTaskThatTake10msToExecute());

      assertEquals(1, agent.getQueueSize());
    }
  }

  @Test
  public void executeTask_AgentIsStartedAndTaskQueued_TaskShouldBeExecuted() throws Exception {
    ServerMock mock = new ServerMock();

    try (Agent agent = new Agent(mock, 1)) {
      TestPlugin factory = new TestPlugin();
      agent.addPlugin(factory);

      TestPlugin plugin = (TestPlugin) agent.enqueue(Make_TestTaskThatTake10msToExecute());

      assertFalse(plugin.WasExecuted);

      for (int i = 1000; i > 0 && !plugin.WasExecuted; i--) {
        Thread.sleep(1);
      }

      assertTrue(plugin.WasExecuted);
    }
  }

  @Test
  public void onTaskComplete_APluginIsExecuted_ShouldNotifyServer() throws Exception {
    ServerMock mock = new ServerMock();

    try (Agent agent = new Agent(mock, 1)) {
      TestPlugin factory = new TestPlugin();
      agent.addPlugin(factory);

      TestPlugin plugin = (TestPlugin) agent.enqueue(Make_TestTaskThatTake10msToExecute());

      assertFalse(plugin.WasExecuted);

      for (int i = 1000; i > 0 && !mock.WasOnCompleteCalled; i--)
        Thread.sleep(1);

      assertTrue(mock.WasOnCompleteCalled);
    }
  }

  @Test
  public void getState_NoTasksQueued_ReturnEmptyState() throws Exception {
    ServerMock mock = new ServerMock();
    try(Agent a = new Agent(mock, 1)){
      assertEquals(0, a.getState().runningSize);
    }
  }

  @Test
  public void getState_OneTask_ReturnOneExecutingTask() throws Exception {
    ServerMock mock = new ServerMock();
    try(Agent a = new Agent(mock, 1)){
      a.addPlugin(new TestPlugin());
      Plugin p = a.enqueue(Make_TestTaskThatTake10msToExecute());

      for (int i = 1000; i > 0 && p.getTask().get_State() != TaskState.Executing; i--)
        Thread.sleep(1);

      assertEquals(1, a.getState().runningSize);
      assertEquals(1, a.getState().queueSize);
    }
  }

  @Test
  public void getState_OneExecutingAndOneQueuedTask_ReturnOneExecutingTask() throws Exception {
    ServerMock mock = new ServerMock();
    try(Agent a = new Agent(mock, 1)){
      a.addPlugin(new TestPlugin());
      Plugin p = a.enqueue(Make_TestTaskThatTake10msToExecute());
      a.enqueue(Make_TestTaskThatTake10msToExecute());

      for (int i = 1000; i > 0 && p.getTask().get_State() != TaskState.Executing; i--)
        Thread.sleep(1);

      assertEquals(1, a.getState().runningSize);
      assertEquals(2, a.getState().queueSize);
    }
  }

  protected Task Make_TestTaskThatTake10msToExecute() {
    Task task = new Task();
    task.pluginId = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
    task.properties.put("sleep", "10");

    return task;
  }
}
