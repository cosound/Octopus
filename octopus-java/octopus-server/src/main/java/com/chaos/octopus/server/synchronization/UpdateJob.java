package com.chaos.octopus.server.synchronization;

import com.chaos.octopus.commons.core.Job;
import com.chaos.sdk.Chaos;

import java.util.ArrayList;
import java.util.List;

public class UpdateJob implements SynchronizationTask
{
    private List<Job> jobs;
    private Chaos chaos;

    public UpdateJob(List<Job> jobs, Chaos chaos)
    {
        this.jobs = jobs;
        this.chaos = chaos;
    }

    @Override
    public void action()
    {
        ArrayList<Job> toUpdate = new ArrayList<>();

        synchronized (jobs)
        {
            while(jobs.size() != 0)
            {
                Job job = jobs.get(0);
                toUpdate.add(job);
                jobs.remove(0);
            }
        }

        chaos.jobSet(toUpdate);
    }
}
