package com.chaos.octopus.server.unit.Synchronization;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.server.synchronization.UpdateJob;
import com.chaos.sdk.Chaos;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UpdateJobTest
{
    @Test
    public void action_SendUpdatedJobsToChaos_CallChaosWithJobList()
    {
        List<Job> jobs = make_JobList();
        Chaos chaos = mock(Chaos.class);
        UpdateJob updateJob = new UpdateJob(jobs, chaos);

        updateJob.action();

        verify(chaos).jobSet(any(List.class));
    }

    @Test
    public void action_SendUpdatedJobsToChaos_JobsAreRemovedFromJobList()
    {
        List<Job> jobs = make_JobList();
        Chaos chaos = mock(Chaos.class);
        UpdateJob updateJob = new UpdateJob(jobs, chaos);

        updateJob.action();

        assertEquals(0, jobs.size());
    }

    @Test
    public void action_DueToConcurrencyATempListShouldBeSendInsteadOfTheOriginalWriteableList_CallChausWithJobList()
    {
        List<Job> jobs = make_JobList();
        Chaos chaos = mock(Chaos.class);
        UpdateJob updateJob = new UpdateJob(jobs, chaos);

        updateJob.action();

        verify(chaos, never()).jobSet(same(jobs));
    }

    private List<Job> make_JobList()
    {
        ArrayList jobs = new ArrayList();
        jobs.add(new Job());
        jobs.add(new Job());

        return jobs;
    }
}
