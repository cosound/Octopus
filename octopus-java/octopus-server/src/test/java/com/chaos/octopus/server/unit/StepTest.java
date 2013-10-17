package com.chaos.octopus.server.unit;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chaos.octopus.server.Step;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskState;

public class StepTest extends TestBase
{

	@Test
	public void nextAvailableTask_GivenAStepWithMultipleTasks_ReturnTheFirstTaskThatHasntStarted() 
	{
		Step step  = new Step();
		Task task1 = make_Task();
		Task task2 = make_Task();
		step.tasks.add(task1);
		step.tasks.add(task2);
		
		Task result = step.nextAvailableTask();
		
		assertEquals(task1.taskId, result.taskId);
	}
	
	@Test
	public void nextAvailableTask_GivenAStepWithMultipleTasks_ReturnTheFirstTaskThatHasntStarted2() 
	{
		Step step = new Step();
		Task task1 = make_Task();
		Task task2 = make_Task();
		step.tasks.add(task1);
		step.tasks.add(task2);
		task1.set_State(TaskState.Committed);
		
		Task result = step.nextAvailableTask();
		
		assertEquals(task2.taskId, result.taskId);
	}
	
	@Test
	public void isCompleted_GivenAStepWhereAllTasksAreCommitted_ReturnTrue() 
	{
		Step step = new Step();
		Task task1 = make_Task();
		Task task2 = make_Task();
		Task task3 = make_Task();
		step.tasks.add(task1);
		step.tasks.add(task2);
		step.tasks.add(task3);
		task1.set_State(TaskState.Committed);
		task2.set_State(TaskState.Committed);
		task3.set_State(TaskState.Committed);
		
		boolean result = step.isCompleted();
		
		assertTrue(result);
	}
	
	@Test
	public void isCompleted_GivenAStepWhereAllTasksAreRolledback_ReturnTrue() 
	{
		Step step = new Step();
		Task task1 = make_Task();
		Task task2 = make_Task();
		Task task3 = make_Task();
		step.tasks.add(task1);
		step.tasks.add(task2);
		step.tasks.add(task3);
		task1.set_State(TaskState.Rolledback);
		task2.set_State(TaskState.Rolledback);
		task3.set_State(TaskState.Rolledback);
		
		boolean result = step.isCompleted();
		
		assertTrue(result);
	}
}
