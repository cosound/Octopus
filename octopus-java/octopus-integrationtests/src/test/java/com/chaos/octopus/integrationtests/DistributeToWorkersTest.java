package com.chaos.octopus.integrationtests;

import com.chaos.octopus.core.Task;
import com.chaos.octopus.server.*;
import org.junit.Test;

public class DistributeToWorkersTest extends TestBase
{
	@Test
	public void enqueue_GivenJobWithTwoSteps_FirstStepHasToCompleteBeforeTheSecondCanBegin() throws Exception
    {
        try (OrchestratorImpl orchestrator = new OrchestratorImpl(20000))
        {
            Job  job = new Job();
            Step step1 = new Step();
            Step step2 = new Step();
            Task task1  = Make_TestTask();
            Task task2  = Make_TestTask();
            step1.tasks.add(task1);
            step2.tasks.add(task2);
            job.steps.add(step1);
            job.steps.add(step2);


        }
	}
}
