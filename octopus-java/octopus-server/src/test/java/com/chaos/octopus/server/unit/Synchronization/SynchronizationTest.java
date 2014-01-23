package com.chaos.octopus.server.unit.Synchronization;

import com.chaos.octopus.server.synchronization.Synchronization;
import com.chaos.octopus.server.synchronization.SynchronizationTask;
import org.junit.Test;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SynchronizationTest
{
    @Test
    public void synchronize_Default_CallAllSynchronizationTasks()
    {
        SynchronizationTask[] tasks = new SynchronizationTask[]{mock(SynchronizationTask.class),mock(SynchronizationTask.class)};
        Synchronization synch = new Synchronization(tasks);

        synch.synchronize();

        verify(tasks[0]).action();
        verify(tasks[1]).action();
    }

    @Test
    public void synchronize_Given10_ShouldCallAllTasksEvery10ms() throws InterruptedException
    {
        SynchronizationTask[] tasks = new SynchronizationTask[]{mock(SynchronizationTask.class),mock(SynchronizationTask.class)};
        Synchronization synch = new Synchronization(tasks);

        synch.synchronize(10);

        Thread.sleep(20); // wait long enough for the tasks to be run at least twice

        verify(tasks[0], atLeast(2)).action();
        verify(tasks[1], atLeast(2)).action();
    }
}
