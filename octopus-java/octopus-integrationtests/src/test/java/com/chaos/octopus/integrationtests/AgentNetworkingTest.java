package com.chaos.octopus.integrationtests;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.core.Step;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TestPlugin;
import com.chaos.octopus.server.AgentProxy;
import com.chaos.octopus.server.OrchestratorImpl;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Copyright (c) 2014 Chaos ApS. All rights reserved. See LICENSE.TXT for details.
 */
public class AgentNetworkingTest
{
    @Test
    public void agentDisconnect_NewJobIsQueued_AgentShouldBeRemovedFromAgentList() throws Exception
    {
        try(OrchestratorImpl orchestrator = new OrchestratorImpl(2000);
            Agent agent = new Agent("localhost", 2000, 2001))
        {
            agent.addPlugin(new TestPlugin());
            orchestrator.open();
            agent.open();

            final ArrayList<AgentProxy> agents = orchestrator.getAgents();

            waitUntil(new Check() {
                @Override
                public Boolean isTrue() {
                    return agents.size() == 0;
                }
            });

            agent.close();

            Job job = new Job();
            Step step = new Step();
            Task task = new Task();
            task.pluginId = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
            step.tasks.add(task);
            job.steps.add(step);
            orchestrator.enqueue(job);

            assertEquals("Agent should be removed from list after disconnect", agents.size(), 0);
        }
    }

    private void waitUntil(Check check) throws InterruptedException
    {
        for(int i = 0; i < 5000 && check.isTrue(); i++)
        {
            Thread.sleep(1);
        }
    }
}

interface Check
{
    Boolean isTrue();
}
