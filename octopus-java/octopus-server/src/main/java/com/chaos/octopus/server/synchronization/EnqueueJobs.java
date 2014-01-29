package com.chaos.octopus.server.synchronization;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.core.Orchestrator;
import com.chaos.sdk.AuthenticatedChaosClient;

import java.io.IOException;

public class EnqueueJobs implements SynchronizationTask
{
    private Orchestrator orchestrator;
    private AuthenticatedChaosClient client;

    public EnqueueJobs(Orchestrator orchestrator, AuthenticatedChaosClient client)
    {
        this.orchestrator = orchestrator;
        this.client = client;
    }

    @Override
    public void action()
    {
        try
        {
            for(Job job : client.jobGet())
            {
                orchestrator.enqueue(job);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
