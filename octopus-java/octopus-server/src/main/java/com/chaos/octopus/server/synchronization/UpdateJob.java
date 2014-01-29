package com.chaos.octopus.server.synchronization;

import com.chaos.octopus.server.ConcurrentJobBuffer;
import com.chaos.sdk.v6.dto.AuthenticatedChaosClient;

import java.io.IOException;

public class UpdateJob implements SynchronizationTask
{
    private ConcurrentJobBuffer jobs;
    private AuthenticatedChaosClient client;

    public UpdateJob(ConcurrentJobBuffer jobs, AuthenticatedChaosClient client)
    {
        this.jobs = jobs;
        this.client = client;
    }

    @Override
    public void action()
    {
        try
        {
            client.jobSet(jobs.popAll());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
