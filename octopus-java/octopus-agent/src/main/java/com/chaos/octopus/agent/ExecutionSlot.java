package com.chaos.octopus.agent;

public class ExecutionSlot implements Runnable
{
	private Thread _thread;
	private Plugin _plugin;
	private ExecutionHandler _executionHandler;
	
	public ExecutionSlot(ExecutionHandler executionHandler, Plugin plugin)
	{
		_thread = new Thread(this);
		_plugin = plugin;
		_executionHandler = executionHandler;
		
		_thread.start();
	}

	@Override
	public void run()
	{
		_plugin.execute();
		
		_executionHandler.taskComplete(this);
	}
}
