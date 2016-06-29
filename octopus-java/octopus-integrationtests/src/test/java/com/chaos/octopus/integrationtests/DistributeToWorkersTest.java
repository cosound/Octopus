package com.chaos.octopus.integrationtests;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.server.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class DistributeToWorkersTest extends TestBase
{

  // todo occasional bug
    @Test
    public void distributeTasksAmongstWorkers_GivenAJobWithMoreTasksThanOneWorkerCanHandle_ExecuteOnOtherWorkers() throws Exception
    {
        try(OrchestratorImpl orch = new OrchestratorImpl(2000);
            Agent agent1 = new Agent("localhost", 2000, 2001, 1);
            Agent agent2 = new Agent("localhost", 2000, 2002, 1))
        {
            orch.open();
            agent1.addPlugin(new TestPlugin());
            agent2.addPlugin(new TestPlugin());
            agent1.open();
            agent2.open();

            TestUtils.waitUntil(new Check() {
                @Override
                public Boolean isTrue() {return orch.getAgents().size() == 2;}
            });

            Job job = make_JobWithTwoTasks();
            orch.enqueue(job);

            TestUtils.waitUntil(new Check() {
                @Override
                public Boolean isTrue() { return agent1.getQueueSize() == 1 && agent2.getQueueSize() == 1;}
            });

            assertEquals("One key should be queued", 1, agent1.getQueueSize());
            assertEquals("One key should be queued", 1, agent2.getQueueSize());
        }
    }

    private Job make_JobWithTwoTasks()
    {
        Job job = new Job();
        Step step = new Step();
        step.tasks.add(make_Task());
        step.tasks.add(make_Task());
        job.steps.add(step);

        return job;
    }

    private Task make_Task()
    {
        Task task = new Task();
        task.pluginId = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
        task.properties.put("sleep", "1000");

        return task;
    }
}
