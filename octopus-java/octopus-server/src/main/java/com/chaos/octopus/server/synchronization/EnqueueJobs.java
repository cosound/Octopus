package com.chaos.octopus.server.synchronization;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.core.Orchestrator;
import com.chaos.sdk.Chaos;

public class EnqueueJobs implements SynchronizationTask
{
    private Orchestrator orchestrator;
    private Chaos chaos;

    public EnqueueJobs(Orchestrator orchestrator, Chaos chaos)
    {
        this.orchestrator = orchestrator;
        this.chaos = chaos;
    }

    @Override
    public void action()
    {
        for(Job job : chaos.jobGet())
        {
            orchestrator.enqueue(job);
        }
    }
}
