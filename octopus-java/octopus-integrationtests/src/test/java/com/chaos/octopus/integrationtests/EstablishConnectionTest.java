package com.chaos.octopus.integrationtests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.commons.core.TestPlugin;
import com.chaos.octopus.server.*;

public class EstablishConnectionTest extends TestBase
{
	@Test
	public void ConnectAgentToServer_WhenAnAgentStartsItConnectsToTheServer_ServerAddsAgentConnectionToList() throws Exception 
	{
		int port       = 20000;
		int noOfAgents = 0;
		
		try(OrchestratorImpl leader = new OrchestratorImpl(port); 
			Agent  agent  = new Agent("localhost", port, 20001))
		{
			agent.addPlugin(new TestPlugin());
			leader.open();
			agent.open();
			
			for(int i = 1000; i > 0 && noOfAgents != 1; i--)
			{
				noOfAgents = leader.getAgents().size();
				
				Thread.sleep(1);
			}
		}

		assertEquals(1,  noOfAgents);
	}

	@Test
	public void ConnectAgentToServer_TwoAgentsAreStarted_ServerAddsAgentConnectionToList() throws Exception 
	{
		int port       = 20000;
		int noOfAgents = 0;
		
		try(OrchestratorImpl leader = new OrchestratorImpl(port); 
			Agent  agent1  = new Agent("localhost", port, 20001);
			Agent  agent2  = new Agent("localhost", port, 20002))
		{
			agent1.addPlugin(new TestPlugin());
			agent2.addPlugin(new TestPlugin());
			leader.open();
			agent1.open();
			agent2.open();
			
			for(int i = 1000; i > 0 && noOfAgents != 2; i--)
			{
				noOfAgents = leader.getAgents().size();
				
				Thread.sleep(1);
			}
		}

		assertEquals(2,  noOfAgents);
	}
	
	@Test
	public void ServerRequestsPluginList_AgentConnects_ServerRequestsPluginListFromAgent() throws Exception
	{
		int port        = 20000;
		int noOfPlugins = 0;

		try(OrchestratorImpl leader = new OrchestratorImpl(port); 
			Agent  agent  = new Agent("localhost", port, 20001))
		{
            leader.open();
			TestPlugin pl = new TestPlugin();
			agent.addPlugin(pl);
			agent.open();
			
			for(int i = 1000; i > 0 && noOfPlugins != 1; i--)
			{
				if(leader.getAgents().size() > 0)
					noOfPlugins = leader.getAgents().get(0).get_SupportedPlugins().size();
				
				Thread.sleep(1);
			}
		}

		assertEquals(1,  noOfPlugins);
	}
	
	@Test
	public void ServerRequestsPluginList_TwoAgentsConnect_ServerRequestsPluginListFromAgents() throws Exception
	{
		int port                  = 20000;
		int noOfPluginsFromAgent1 = 0;
		int noOfPluginsFromAgent2 = 0;
		
		try(OrchestratorImpl leader = new OrchestratorImpl(port); 
			Agent  agent1 = new Agent("localhost", port, 20001);
			Agent  agent2 = new Agent("localhost", port, 20002))
		{
			agent1.addPlugin(new TestPlugin());
			agent2.addPlugin(new TestPlugin());
			leader.open();
			agent1.open();
			agent2.open();
			
			for(int i = 1000; i > 0 && noOfPluginsFromAgent1 != 1; i--)
			{
				if(leader.getAgents().size() == 2)
				{
					noOfPluginsFromAgent1 = leader.getAgents().get(0).get_SupportedPlugins().size();
					noOfPluginsFromAgent2 = leader.getAgents().get(1).get_SupportedPlugins().size();
				}
				
				Thread.sleep(1);
			}
		}

		assertEquals(1, noOfPluginsFromAgent1);
		assertEquals(1, noOfPluginsFromAgent2);
	}
}
