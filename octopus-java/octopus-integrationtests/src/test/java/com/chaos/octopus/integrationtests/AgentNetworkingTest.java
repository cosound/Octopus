package com.chaos.octopus.integrationtests;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.core.Step;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TestPlugin;
import com.chaos.octopus.server.AgentProxy;
import com.chaos.octopus.server.OrchestratorImpl;
import com.chaos.octopus.commons.exception.ConnectException;
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
            orchestrator.open();

            agent.addPlugin(new TestPlugin());
            agent.open();

            final ArrayList<AgentProxy> agents = orchestrator.getAgents();

            TestUtils.waitUntil(new Check() {
                    @Override
                    public Boolean isTrue() {
                        return agents.size() == 1;
                    }
                });
            assertEquals("Agent should be added", 1, agents.size());

            agent.close();

            Job job = make_Job();
            orchestrator.enqueue(job);

            assertEquals("Agent should be removed from list after disconnect", 0, agents.size());
        }
    }

    private Job make_Job()
    {
        Job job = new Job();
        Step step = new Step();
        Task task = new Task();
        task.pluginId = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
        step.tasks.add(task);
        job.steps.add(step);

        return job;
    }

    @Test(expected = ConnectException.class)
    public void agentConnectFailed_OrchestratorNotStarted_ThrowException() throws Exception
    {
        try(Agent agent = new Agent("localhost", 2000, 2001))
        {
            agent.open();
        }
    }

}

