package com.chaos.octopus.agent;

public class ExecutionSlot implements Runnable
{
	private Thread _thread;
	private Plugin _plugin;
	
	public ExecutionSlot(ExecutionHandler executionHandler, Plugin plugin)
	{
		_thread = new Thread(this);
		_plugin = plugin;
		
		_thread.start();
	}

	@Override
	public void run()
	{
		_plugin.execute();
	}
}
