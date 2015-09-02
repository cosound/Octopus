package com.chaos.octopus.agent.unit.action;

import com.chaos.octopus.agent.ExecutionHandler;
import com.chaos.octopus.agent.TaskStatusChangeListener;
import com.chaos.octopus.agent.action.AgentStateAction;
import com.chaos.octopus.commons.core.Plugin;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskState;
import com.chaos.octopus.commons.core.TestPlugin;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AgentStateActionTest{
  @Test
  public void getState_NoTasksQueued_ReturnEmptyState() throws Exception {
    ExecutionHandler eh = new ExecutionHandler(new TaskStatusChangeListener() {
      public void onTaskComplete(Task task) {

      }

      public void onTaskUpdate(Task task) {

      }
    }, 1);
    AgentStateAction esa = new AgentStateAction(eh);

    assertEquals(0, esa.getState().runningSize);
    assertEquals(0, esa.getState().queueSize);
  }

  @Test
  public void getState_OneTask_ReturnOneExecutingTask() throws Exception {
    ExecutionHandler eh = new ExecutionHandler(new TaskStatusChangeListener() {
      public void onTaskComplete(Task task) {

      }

      public void onTaskUpdate(Task task) {

      }
    }, 1);
    AgentStateAction esa = new AgentStateAction(eh);
    Plugin p = new TestPlugin().create(Make_TestTaskThatTake10msToExecute());
    eh.enqueue(p);

    for (int i = 1000; i > 0 && p.getTask().get_State() != TaskState.Executing; i--)
      Thread.sleep(1);

    assertEquals(1, esa.getState().runningSize);
    assertEquals(1, esa.getState().queueSize);
  }

  @Test
  public void getState_OneExecutingAndOneQueuedTask_ReturnOneExecutingTask() throws Exception {
    ExecutionHandler eh = new ExecutionHandler(new TaskStatusChangeListener() {
      public void onTaskComplete(Task task) {

      }

      public void onTaskUpdate(Task task) {

      }
    }, 1);
    AgentStateAction esa = new AgentStateAction(eh);
    Plugin p = new TestPlugin().create(Make_TestTaskThatTake10msToExecute());
    eh.enqueue(p);
    eh.enqueue(new TestPlugin().create(Make_TestTaskThatTake10msToExecute()));

    for (int i = 1000; i > 0 && p.getTask().get_State() != TaskState.Executing; i--)
      Thread.sleep(1);

    assertEquals(1, esa.getState().runningSize);
    assertEquals(2, esa.getState().queueSize);
  }

  protected Task Make_TestTaskThatTake10msToExecute() {
    Task task = new Task();
    task.pluginId = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
    task.properties.put("sleep", "10");

    return task;
  }
}
