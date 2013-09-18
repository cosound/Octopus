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
		int port = 20000;
		int noOfAgentsConnected = 0;
		
		try(Server server = new Server(port); 
			Agent  agent  = new Agent("localhost", port))
		{
			server.open();
			agent.open();
			
			for(int i = 1000; i > 0 && noOfAgentsConnected == 0; i--)
			{
				noOfAgentsConnected = server.get_Agents().size();
				
				Thread.sleep(1);
			}
		}
		
		assertEquals(1,  noOfAgentsConnected);
	}

}
