package com.chaos.octopus.server.unit;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.core.Step;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskState;
import com.chaos.octopus.server.AgentProxy;
import com.chaos.octopus.server.AllocationHandler;
import com.chaos.octopus.server.exception.DisconnectedException;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 10.10.13
 * Time: 11:23
 */
public class AllocationHandlerTest
{
    @Test
    public void enqueue_JobWithOneStep_CallEnqueueWithTask() throws DisconnectedException {
        AllocationHandler ah = new AllocationHandler();
        Job job = make_JobWithOneStep();
        Task task = job.getTasks().iterator().next();
        AgentProxy agent = mock(AgentProxy.class);
        ah.addAgent(agent);

        ah.enqueue(job);

        verify(agent).enqueue(task);
    }

    @Test
    public void taskComplete_JobWithTwoStepsWhenFirstStepFinishes_CallEnqueueWithSecondTask() throws DisconnectedException {
        AllocationHandler ah = new AllocationHandler();
        Job job = new Job();
        Step step1 = new Step();
        Task task1 = new Task();
        task1.pluginId = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
        step1.tasks.add(task1);
        job.steps.add(step1);
        Step step2 = new Step();
        Task task2 = new Task();
        task2.pluginId = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
        step2.tasks.add(task2);
        job.steps.add(step2);
        AgentProxy agent = mock(AgentProxy.class);
        ah.addAgent(agent);
        ah.enqueue(job);
        task1.set_State(TaskState.Committed); // simulate the task was completed

        ah.taskComplete(task1);

        verify(agent).enqueue(task2);
    }

    @Test
    public void taskUpdate_JobWithOneStep_CallEnqueueWithTask()
    {
        AllocationHandler ah = new AllocationHandler();
        Job job = make_JobWithOneStep();
        Task task2 = new Task();
        task2.taskId = "unique id";
        task2.set_State(TaskState.Committed);
        ah.enqueue(job);

        ah.taskUpdate(task2);

        Task actual = job.steps.get(0).tasks.get(0);
        assertSame(task2, actual);
    }

    private Job make_JobWithOneStep()
    {
        Job job = new Job();
        Step step = new Step();
        Task task = new Task();
        task.taskId = "unique id";
        task.pluginId = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
        step.tasks.add(task);
        job.steps.add(step);

        return job;
    }
}
