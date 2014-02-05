package com.chaos.octopus.integrationtests;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.server.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class DistributeToWorkersTest extends TestBase
{
//	@Test
//	public void enqueue_GivenJobWithTwoSteps_FirstStepHasToCompleteBeforeTheSecondCanBegin() throws Exception
//    {
//        try (OrchestratorImpl orchestrator = new OrchestratorImpl(20000);
//             Agent agent = new Agent("localhost", 20000, 20001))
//        {
//            agent.addPlugin(new TestPlugin());
//            orchestrator.open();
//            agent.open();
//
//            Job  job = new Job();
//            Step step1 = new Step();
//            Step step2 = new Step();
//            Task task1 = Make_TestTaskThatTake10msToExecute();
//            Task task2 = Make_TestTaskThatTake10msToExecute();
//            Task task3 = Make_TestTaskThatTake10msToExecute();
//            Task task4 = Make_TestTaskThatTake10msToExecute();
//            task1.properties.put("number", "2");
//            task2.properties.put("number", "4");
//            task3.properties.put("number", "8");
//            task3.properties.put("sleep", "10");
//            task4.properties.put("number", "1");
//
//            step1.tasks.add(task1);
//            step1.tasks.add(task2);
//            step1.tasks.add(task3);
//            step2.tasks.add(task4);
//            job.steps.add(step1);
//            job.steps.add(step2);
//
//            orchestrator.enqueue(job);
//
//            for(int i = 1000; i > 0 && !step1.isCompleted(); i--)
//            {
//                Thread.sleep(1);
//            }
//
//            assertEquals(TaskState.Committed, task1.get_State());
//            assertEquals(TaskState.Committed, task2.get_State());
//            assertEquals(TaskState.Committed, task3.get_State());
//            // number is 14 if second step hasnt run
//            assertEquals(14, TestPlugin.getNumber());
//            assertTrue(step1.isCompleted());
//            assertFalse(step2.isCompleted());
//
//            for(int i = 1000; i > 0 && !step2.isCompleted(); i--)
//            {
//                Thread.sleep(1);
//            }
//
//            assertEquals(TaskState.Committed, task1.get_State());
//            assertEquals(TaskState.Committed, task2.get_State());
//            assertEquals(TaskState.Committed, task3.get_State());
//            assertEquals(TaskState.Committed, task4.get_State());
//            assertEquals(15, TestPlugin.getNumber());
//            assertTrue(step1.isCompleted());
//            assertTrue(step2.isCompleted());
//        }
//	}
}
