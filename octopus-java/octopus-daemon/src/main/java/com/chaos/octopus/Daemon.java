package com.chaos.octopus;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.agent.plugin.ChaosPlugin;
import com.chaos.octopus.agent.plugin.CommandLinePlugin;
import com.chaos.octopus.commons.core.OctopusConfiguration;
import com.chaos.octopus.commons.core.TestPlugin;
import com.chaos.octopus.server.OrchestratorImpl;
import com.chaos.octopus.server.synchronization.EnqueueJobs;
import com.chaos.octopus.server.synchronization.Synchronization;
import com.chaos.octopus.server.synchronization.UpdateJob;
import com.chaos.sdk.AuthenticatedChaosClient;
import com.chaos.sdk.Chaos;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

public class Daemon implements org.apache.commons.daemon.Daemon
{
    private OrchestratorImpl leader;
    private Agent agent;
    private OctopusConfiguration config;

    @Override
    public void init(DaemonContext daemonContext) throws DaemonInitException, Exception
    {
    }

    @Override
    public void start() throws Exception
    {
        config = new OctopusConfiguration();

        if(config.getIsAgent())
        {
            agent = new Agent(config.getOrchestratorIp(), config.getOrchestratorPort(), config.getListeningPort(), config.getNumberOfParallelTasks());
            agent.addPlugin(new TestPlugin());
            agent.addPlugin(new CommandLinePlugin());
            agent.addPlugin(new ChaosPlugin());

            agent.open();
        }
        else
        {
            leader = OrchestratorImpl.create(config);
            leader.open();
        }
    }

    @Override
    public void stop() throws Exception
    {
        if(config.getIsAgent())
            agent.close();
        else
            leader.close();
    }

    @Override
    public void destroy()
    {
    }
}
