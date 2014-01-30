package com.chaos.octopus.commons.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chaos.octopus.commons.core.Step;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskState;

import java.util.Iterator;

public class StepTest extends TestBase
{
	@Test
	public void getTasks_GivenAStepWithMultipleNewTasks_ReturnThemAll()
	{
		Step step  = new Step();
		Task task1 = make_Task();
		Task task2 = make_Task();
		step.tasks.add(task1);
		step.tasks.add(task2);
		
		Iterator<Task> results = step.getTasks().iterator();
		
		assertEquals(task1, results.next());
        assertEquals(task2, results.next());
        assertFalse(results.hasNext());
	}
	
	@Test
	public void getTasks_GivenAStepWithMultipleOneFinishedAndOneNewTask_ReturnTheNewTask()
	{
		Step step = new Step();
		Task task1 = make_Task();
		Task task2 = make_Task();
		step.tasks.add(task1);
		step.tasks.add(task2);
		task1.set_State(TaskState.Committed);

        Iterator<Task> results = step.getTasks().iterator();

        assertEquals(task2, results.next());
        assertFalse(results.hasNext());
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
