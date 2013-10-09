package com.chaos.octopus.server.unit;

import static org.junit.Assert.*;

import com.chaos.octopus.core.Task;
import com.chaos.octopus.core.TaskState;
import org.junit.Test;

import com.chaos.octopus.server.*;

public class JobTest extends TestBase
{
	@Test
	public void nextAvailableTask_GivenOneStep_ReturnTheNextTask() 
	{
		Job job   = new Job();
		Step step = new Step();
		Task task1 = make_Task();
		Task task2 = make_Task();
		job.steps.add(step);
		step.tasks.add(task1);
		step.tasks.add(task2);
		
		Task result1 = job.nextAvailableTask();
		
		assertEquals(task1.taskId, result1.taskId);
	}

	@Test
	public void nextAvailableTask_GivenOneStepAndFirstTaskIsCommitted_ReturnTheNextTask() 
	{
		Job job   = new Job();
		Step step = new Step();
		Task task1 = make_Task();
		Task task2 = make_Task();
		job.steps.add(step);
		step.tasks.add(task1);
		step.tasks.add(task2);
		task1.set_State(TaskState.Committed);
		
		Task result1 = job.nextAvailableTask();
		
		assertEquals(task2.taskId, result1.taskId);
	}
	
	@Test
	public void nextAvailableTask_GivenMultipleStepsAndTheFirstCommitted_ReturnTheNextTask() 
	{
		Job  job   = new Job();
		Step step1 = new Step();
		Step step2 = new Step();
		Task task1 = make_Task();
		Task task2 = make_Task();
		Task task3 = make_Task();
		Task task4 = make_Task();
		job.steps.add(step1);
		job.steps.add(step2);
		step1.tasks.add(task1);
		step1.tasks.add(task2);
		step2.tasks.add(task3);
		step2.tasks.add(task4);
		task1.set_State(TaskState.Committed);
		task2.set_State(TaskState.Committed);
		
		Task result1 = job.nextAvailableTask();
		
		assertEquals(task3.taskId, result1.taskId);
	}

	@Test
	public void nextAvailableTask_GivenMultipleStepsAndAllTasksAreInProgressOrDone_ReturnNull() 
	{
		Job  job   = new Job();
		Step step1 = new Step();
		Step step2 = new Step();
		Task task1 = make_Task();
		Task task2 = make_Task();
		Task task3 = make_Task();
		Task task4 = make_Task();
		job.steps.add(step1);
		job.steps.add(step2);
		step1.tasks.add(task1);
		step1.tasks.add(task2);
		step2.tasks.add(task3);
		step2.tasks.add(task4);
		task1.set_State(TaskState.Committed);
		task2.set_State(TaskState.Executing);
		
		Task result = job.nextAvailableTask();
		
		assertNull(result);
	}

}
