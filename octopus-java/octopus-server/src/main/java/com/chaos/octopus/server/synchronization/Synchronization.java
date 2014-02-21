package com.chaos.octopus.server.synchronization;

import java.util.*;

public class Synchronization extends TimerTask
{
    private List<SynchronizationTask> tasks;
    private Timer timer;

    public Synchronization(SynchronizationTask... tasks)
    {
        this.tasks = new ArrayList<>();

        for(SynchronizationTask task : tasks)
        {
            this.tasks.add(task);
        }
    }

    public void synchronize()
    {
        for(SynchronizationTask task : tasks)
        {
            task.action();
        }
    }

    public void synchronize(int period)
    {
        timer = new Timer("synchronization", true);

        timer.schedule(this, 0, period);
    }

    @Override
    public void run()
    {
        synchronize();
    }

    public void addSynchronizationTask(SynchronizationTask task)
    {
        tasks.add(task);
    }
}
