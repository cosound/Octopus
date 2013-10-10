package com.chaos.octopus.integrationtests;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.core.Task;
import com.chaos.octopus.core.TaskState;
import com.chaos.octopus.core.TestPlugin;
import com.chaos.octopus.server.*;
import junit.framework.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DistributeToWorkersTest extends TestBase
{
	@Test
	public void enqueue_GivenJobWithTwoSteps_FirstStepHasToCompleteBeforeTheSecondCanBegin() throws Exception
    {
        try (OrchestratorImpl orchestrator = new OrchestratorImpl(20000);
             Agent agent = new Agent("localhost", 20000, 20001))
        {
            agent.addPlugin(new TestPlugin());
            orchestrator.open();
            agent.open();

            Job  job = new Job();
            Step step1 = new Step();
            Step step2 = new Step();
            Task task1 = Make_TestTask();
            Task task2 = Make_TestTask();
            Task task3 = Make_TestTask();
            Task task4 = Make_TestTask();
            task1.properties.put("number", "2");
            task2.properties.put("number", "4");
            task3.properties.put("number", "8");
            task4.properties.put("number", "1");

            step1.tasks.add(task1);
            step1.tasks.add(task2);
            step1.tasks.add(task3);
            step2.tasks.add(task4);
            job.steps.add(step1);
            job.steps.add(step2);

            orchestrator.enqueue(job);

            for(int i = 1000; i > 0 && !step1.isCompleted() && task4.get_State() == TaskState.New; i--)
            {
                Thread.sleep(1);
            }

            System.out.println(task1.get_State());
            System.out.println(task2.get_State());
            System.out.println(task3.get_State());
            System.out.println(task4.get_State());

            assertEquals(TaskState.Committed, task1.get_State());
            assertEquals(TaskState.Committed, task2.get_State());
            assertEquals(TaskState.Committed, task3.get_State());
            // number is 14 if second step hasnt run
            assertEquals(14, TestPlugin.getNumber());
            assertFalse(step2.isCompleted());
        }
	}
}
