package com.chaos.octopus.server.synchronization;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Synchronization extends TimerTask
{
    private List<SynchronizationTask> tasks;
    private Timer timer;

    public Synchronization(SynchronizationTask... tasks)
    {
        this.tasks = Arrays.asList(tasks);
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
}
