package com.chaos.octopus.server.unit.Synchronization;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.server.ConcurrentJobQueue;
import com.chaos.octopus.server.synchronization.UpdateJob;
import com.chaos.sdk.AuthenticatedChaosClient;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class UpdateJobTest
{
    @Test
    public void action_SendUpdatedJobsToChaos_CallChaosWithJobList() throws IOException
    {
        ConcurrentJobQueue jobs = make_JobList();
        AuthenticatedChaosClient chaos = mock(AuthenticatedChaosClient.class);
        UpdateJob updateJob = new UpdateJob(jobs, chaos);

        updateJob.action();

        verify(chaos).jobSet(any(List.class));
    }

    @Test
    public void action_SendUpdatedJobsToChaos_JobsAreRemovedFromJobList()
    {
        ConcurrentJobQueue jobs = make_JobList();
        AuthenticatedChaosClient chaos = mock(AuthenticatedChaosClient.class);
        UpdateJob updateJob = new UpdateJob(jobs, chaos);

        updateJob.action();

        assertFalse(jobs.popAll().iterator().hasNext());
    }

    private ConcurrentJobQueue make_JobList()
    {
        ConcurrentJobQueue jobs = new ConcurrentJobQueue();
        jobs.put(new Job());
        jobs.put(new Job());

        return jobs;
    }
}
