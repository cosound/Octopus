package com.chaos.octopus.server.unit.Synchronization;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.core.Orchestrator;
import com.chaos.octopus.server.synchronization.EnqueueJobs;
import com.chaos.sdk.AuthenticatedChaosClient;
import org.junit.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class EnqueueJobsTest
{
    @Test
    public void action_GetNewAvailableJobs_EnqueueOnOchestrator() throws Exception
    {
        Orchestrator orchestrator = mock(Orchestrator.class);
        AuthenticatedChaosClient chaos = mock(AuthenticatedChaosClient.class);
        EnqueueJobs enqueueJobs = new EnqueueJobs(orchestrator, chaos);
        Job job = new Job();
        when(chaos.jobGet()).thenReturn(Arrays.asList(job));

        enqueueJobs.action();

        verify(chaos).jobGet();
        verify(orchestrator).enqueue(job);
    }
}
