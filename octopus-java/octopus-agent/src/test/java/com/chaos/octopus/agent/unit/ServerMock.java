package com.chaos.octopus.agent.unit;

import java.net.Socket;

import com.chaos.octopus.core.Orchestrator;

public class ServerMock implements Orchestrator
{

	public boolean WasOnCompleteCalled = false;

	@Override
	public Socket get_Socket()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void open()
	{
		// TODO Auto-generated method stub
		
	}

	public void taskCompleted(String serialized)
	{
		WasOnCompleteCalled = true;
	}
}
