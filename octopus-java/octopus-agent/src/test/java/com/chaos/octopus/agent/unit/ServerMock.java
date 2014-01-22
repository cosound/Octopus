package com.chaos.octopus.agent.unit;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.core.Orchestrator;
import com.chaos.octopus.commons.core.Task;

public class ServerMock implements Orchestrator
{

	public boolean WasOnCompleteCalled = false;

	@Override
	public void open()
	{
		// TODO Auto-generated method stub
		
	}

	public void taskCompleted(Task serialized)
	{
		WasOnCompleteCalled = true;
	}

    @Override
    public void taskUpdate(Task task) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	public int get_ListenPort() {
		// TODO Auto-generated method stub
		return 0;
	}

    @Override
    public void enqueue(Job job)
    {

    }

    @Override
    public void close() throws Exception
    {

    }
}
