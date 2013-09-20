package com.chaos.octopus.integrationtests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.server.Server;

public class EstablishConnectionTest 
{
	@Test
	public void ConnectAgentToServer_WhenAnAgentStartsItConnectsToTheServer_ServerAddsAgentConnectionToList() throws Exception 
	{
		int port       = 20000;
		int noOfAgents = 0;
		
		try(Server server = new Server(port); 
			Agent  agent  = new Agent("localhost", port))
		{
			server.open();
			agent.open();
			
			for(int i = 1000; i > 0 && noOfAgents == 0; i--)
			{
				noOfAgents = server.get_Agents().size();
				
				Thread.sleep(1);
			}
		}
		
		assertEquals(1,  noOfAgents);
	}

/*	@Test
	public void ServerRequestsPluginList_AgentConnects_ServerRequestsPluginListFromAgent() throws Exception
	{
		int port        = 20000;
		int noOfPlugins = 0;
		
		try(Server server = new Server(port); 
			Agent  agent  = new Agent("localhost", port))
		{
			server.open();
			agent.open();
			
			for(int i = 1000; i > 0 && noOfPlugins == 0; i--)
			{
				noOfPlugins = server.get_Agents().get_Plugins().size();
				
				Thread.sleep(1);
			}
		}
		
		assertEquals(1,  noOfPlugins);
	}*/
}
