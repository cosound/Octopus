package com.chaos.octopus.agent.unit.action;

import com.chaos.octopus.agent.ExecutionHandler;
import com.chaos.octopus.agent.TaskStatusChangeListener;
import com.chaos.octopus.agent.endpoint.StateGetEndpoint;
import com.chaos.octopus.commons.core.*;
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

    Response<AgentStateResult> response = new StateGetEndpoint(eh).invoke(new Request("State/Get"));

    assertEquals(0, response.Results.get(0).agentState.runningSize);
    assertEquals(0, response.Results.get(0).agentState.queueSize);
  }

  @Test
  public void getState_OneTask_ReturnOneExecutingTask() throws Exception {
    ExecutionHandler eh = new ExecutionHandler(new TaskStatusChangeListener() {
      public void onTaskComplete(Task task) {

      }

      public void onTaskUpdate(Task task) {

      }
    }, 1);
    Plugin p = new TestPlugin().create(Make_TestTaskThatTake10msToExecute());
    eh.enqueue(p);

    for (int i = 1000; i > 0 && p.getTask().get_State() != TaskState.Executing; i--)
      Thread.sleep(1);

    Response<AgentStateResult> response = new StateGetEndpoint(eh).invoke(new Request("State/Get"));

    assertEquals("queueSize", 1, response.Results.get(0).agentState.queueSize);
    assertEquals("runningSize", 1, response.Results.get(0).agentState.runningSize);
  }

  @Test
  public void getState_OneExecutingAndOneQueuedTask_ReturnOneExecutingTask() throws Exception {
    ExecutionHandler eh = new ExecutionHandler(new TaskStatusChangeListener() {
      public void onTaskComplete(Task task) {

      }

      public void onTaskUpdate(Task task) {

      }
    }, 1);
    Plugin p = new TestPlugin().create(Make_TestTaskThatTake10msToExecute());
    eh.enqueue(p);
    eh.enqueue(new TestPlugin().create(Make_TestTaskThatTake10msToExecute()));

    for (int i = 1000; i > 0 && p.getTask().get_State() != TaskState.Executing; i--)
      Thread.sleep(1);

    Response<AgentStateResult> response = new StateGetEndpoint(eh).invoke(new Request("State/Get"));

    assertEquals(1, response.Results.get(0).agentState.runningSize);
    assertEquals(2, response.Results.get(0).agentState.queueSize);
  }

  protected Task Make_TestTaskThatTake10msToExecute() {
    Task task = new Task();
    task.pluginId = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
    task.properties.put("sleep", "10");

    return task;
  }
}
