package com.chaos.octopus.server.synchronization;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Synchronization extends TimerTask
{
    private List<SynchronizationTask> tasks;
    private ScheduledExecutorService timer;

    public Synchronization(SynchronizationTask... tasks)
    {
        this.tasks = new ArrayList<>();

        for(SynchronizationTask task : tasks)
        {
            addSynchronizationTask(task);
        }
    }

    public void addSynchronizationTask(SynchronizationTask task)
    {
        tasks.add(task);
    }

    public void synchronize(int period)
    {
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleWithFixedDelay(this, 0, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run()
    {
        synchronize();
    }

    public void synchronize()
    {
        for(SynchronizationTask task : tasks)
        {
            task.action();
        }
    }
}
