package com.chaos.octopus;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.agent.plugin.ChaosPlugin;
import com.chaos.octopus.agent.plugin.CommandLinePlugin;
import com.chaos.octopus.commons.core.TestPlugin;
import com.chaos.octopus.server.OrchestratorImpl;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

public class Daemon implements org.apache.commons.daemon.Daemon
{
    private OrchestratorImpl leader;
    private Agent agent;

    @Override
    public void init(DaemonContext daemonContext) throws DaemonInitException, Exception
    {
    }

    @Override
    public void start() throws Exception
    {
        leader = new OrchestratorImpl(24352);
        agent = new Agent("localhost", 24352, 24352 +1);

        agent.addPlugin(new TestPlugin());
        agent.addPlugin(new CommandLinePlugin());
        agent.addPlugin(new ChaosPlugin());
        leader.open();
        agent.open();
    }

    @Override
    public void stop() throws Exception
    {
        agent.close();
        leader.close();
    }

    @Override
    public void destroy()
    {
    }
}
