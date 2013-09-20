package com.chaos.octopus.integrationtests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.agent.unit.TestPlugin;
import com.chaos.octopus.server.Leader;

public class EstablishConnectionTest 
{
	@Test
	public void ConnectAgentToServer_WhenAnAgentStartsItConnectsToTheServer_ServerAddsAgentConnectionToList() throws Exception 
	{
		int port       = 20000;
		int noOfAgents = 0;
		
		try(Leader leader = new Leader(port); 
			Agent  agent  = new Agent("localhost", port))
		{
			agent.addPlugin(new TestPlugin());
			leader.open();
			agent.open();
			
			for(int i = 1000; i > 0 && noOfAgents == 0; i--)
			{
				noOfAgents = leader.get_Agents().size();
				
				Thread.sleep(1);
			}
		}

		assertEquals(1,  noOfAgents);
	}

	@Test
	public void ServerRequestsPluginList_AgentConnects_ServerRequestsPluginListFromAgent() throws Exception
	{
		int port        = 20000;
		int noOfPlugins = 0;
		
		try(Leader leader = new Leader(port); 
			Agent  agent  = new Agent("localhost", port))
		{
			agent.addPlugin(new TestPlugin());
			
			leader.open();
			agent.open();
			
			for(int i = 10; i > 0 && noOfPlugins == 0; i--)
			{
				if(leader.get_Agents().size() > 0)
					noOfPlugins = leader.get_Agents().get(0).get_SupportedPlugins().size();
				
				Thread.sleep(100);
			}
		}

		assertEquals(1,  noOfPlugins);
	}
}
