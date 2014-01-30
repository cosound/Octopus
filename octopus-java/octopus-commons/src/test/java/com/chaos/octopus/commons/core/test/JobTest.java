package com.chaos.octopus.commons.core.test;

import static org.junit.Assert.*;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.core.Step;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskState;
import org.junit.Test;

import java.util.Iterator;

public class JobTest extends TestBase
{
    @Test
    public void getTasks_OneStepWithTwoTasks_ReturnAllTasks()
    {
        Job job = new Job();
        Step step = new Step();
        Task task1 = make_Task();
        Task task2 = make_Task();
        job.steps.add(step);
        step.tasks.add(task1);
        step.tasks.add(task2);

        Iterator<Task> results = job.getTasks().iterator();

        assertEquals(task1, results.next());
        assertEquals(task2, results.next());
    }

    @Test
    public void getTasks_OneStepWithTwoTasksWithOneTaskQueued_ReturnTheTaskThatIsntQueued()
    {
        Job job = new Job();
        Step step = new Step();
        Task task1 = make_Task();
        Task task2 = make_Task();
        job.steps.add(step);
        step.tasks.add(task1);
        step.tasks.add(task2);
        task1.set_State(TaskState.Queued);

        Iterator<Task> results = job.getTasks().iterator();

        assertEquals(task2, results.next());
        assertFalse(results.hasNext());
    }

    @Test
    public void getTasks_TwoStepsFirstIsCompleteSecondHasOneNewTask_ReturnTheTask()
    {
        Job job = new Job();
        Step step1 = new Step();
        Step step2 = new Step();
        Task task1 = make_Task();
        Task task2 = make_Task();
        Task task3 = make_Task();
        job.steps.add(step1);
        job.steps.add(step2);
        step1.tasks.add(task1);
        step1.tasks.add(task2);
        step2.tasks.add(task3);
        task1.set_State(TaskState.Executed);
        task2.set_State(TaskState.Executed);

        Iterator<Task> results = job.getTasks().iterator();

        assertEquals(task3, results.next());
        assertFalse(results.hasNext());
    }

	@Test
	public void getTasks_TwoStepsAllTasksAreDone_ReturnEmptyList()
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

        Iterator<Task> results = job.getTasks().iterator();

		assertFalse(results.hasNext());
	}

    @Test
    public void validate_JobIsFormattetCorrectly_ReturnTrue()
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

        boolean result = job.validate();

        assertTrue(result);
    }

    @Test
    public void validate_TasksAreMissingOnAStep_ReturnFalse()
    {
        Job  job   = new Job();
        Step step1 = new Step();
        Step step2 = new Step();
        Task task1 = make_Task();
        Task task2 = make_Task();
        job.steps.add(step1);
        job.steps.add(step2);
        step1.tasks.add(task1);
        step1.tasks.add(task2);

        boolean result = job.validate();

        assertFalse(result);
    }


    @Test
    public void isComplete_AllStepsAreComplete_ReturnTrue()
    {
        Job  job   = new Job();
        Step step1 = new Step();
        Step step2 = new Step();
        Task task1 = make_Task();
        Task task2 = make_Task();
        job.steps.add(step1);
        job.steps.add(step2);
        step1.tasks.add(task1);
        step1.tasks.add(task2);
        task1.set_State(TaskState.Committed);
        task2.set_State(TaskState.Committed);

        boolean result = job.isComplete();

        assertTrue(result);
    }

    @Test
    public void isComplete_AllStepsHaveRolledback_ReturnTrue()
    {
        Job  job   = new Job();
        Step step1 = new Step();
        Step step2 = new Step();
        Task task1 = make_Task();
        Task task2 = make_Task();
        job.steps.add(step1);
        job.steps.add(step2);
        step1.tasks.add(task1);
        step1.tasks.add(task2);
        task1.set_State(TaskState.Rolledback);
        task2.set_State(TaskState.Rolledback);

        boolean result = job.isComplete();

        assertTrue(result);
    }

    @Test
    public void isComplete_AllStepsAreIncomplete_ReturnFalse()
    {
        Job  job   = new Job();
        Step step1 = new Step();
        Step step2 = new Step();
        Task task1 = make_Task();
        Task task2 = make_Task();
        job.steps.add(step1);
        job.steps.add(step2);
        step1.tasks.add(task1);
        step1.tasks.add(task2);
        task1.set_State(TaskState.Committed);
        task2.set_State(TaskState.Executing);

        boolean result = job.isComplete();

        assertFalse(result);
    }

    @Test
    public void containsTask_JobHasOneStepAndContainTask_True()
    {
        Job  job   = new Job();
        Step step1 = new Step();
        Task task1 = make_Task();
        job.steps.add(step1);
        step1.tasks.add(task1);
        task1.taskId = "unique taskId";

        Boolean result = job.containsTask(task1.taskId);

        assertTrue(result);
    }

    @Test
    public void containsTask_JobDoesntContainTask_False()
    {
        Job  job   = new Job();
        Step step1 = new Step();
        job.steps.add(step1);

        Boolean result = job.containsTask("doesnt exist");

        assertFalse(result);
    }

    @Test
    public void containsTask_JobHasTwoStepAndContainTask_True()
    {
        Job  job   = new Job();
        Step step1 = new Step();
        Step step2 = new Step();
        Task task1 = make_Task();
        Task task2 = make_Task();
        job.steps.add(step1);
        step1.tasks.add(task1);
        step2.tasks.add(task2);
        task2.taskId = "unique taskId";

        Boolean result = job.containsTask(task1.taskId);

        assertTrue(result);
    }

    @Test
    public void replaceTask_JobHasOneStepAndContainTask_TaskIsReplaced()
    {
        Job  job   = new Job();
        Step step1 = new Step();
        Task task1 = make_Task();
        Task task2 = make_Task();
        job.steps.add(step1);
        step1.tasks.add(task1);
        task1.taskId = "unique taskId";
        task2.taskId = "unique taskId";
        task2.set_State(TaskState.Executing);

        job.replaceTask(task2);

        Task actual = step1.tasks.iterator().next();
        assertSame(task2, actual);
    }
}
