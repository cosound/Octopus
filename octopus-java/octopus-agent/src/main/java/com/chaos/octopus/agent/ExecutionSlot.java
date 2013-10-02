package com.chaos.octopus.agent;

import java.util.ArrayList;
import java.util.List;

public class ExecutionSlot implements Runnable
{
	private Thread _thread;
	private Plugin _plugin;
	private List<TaskCompleteListener> _taskCompleteListeners;
	
	public ExecutionSlot(Plugin plugin)
	{
		_taskCompleteListeners = new ArrayList<TaskCompleteListener>();
		_thread = new Thread(this);
		_plugin = plugin;
		
		_thread.start();
	}

	@Override
	public void run()
	{
		try
		{
			_plugin.execute();
			
			_plugin.commit();
		}
		catch(Exception e)
		{
			_plugin.rollback();
		}
		finally
		{
			onTaskComplete();
		}
	}
	
	public void addTaskCompleteListener(TaskCompleteListener callback)
	{
		_taskCompleteListeners.add(callback);
	}
	
	public Plugin get_Plugin()
	{
		return _plugin;
	}
	
	private void onTaskComplete()
	{
		for (TaskCompleteListener callback : _taskCompleteListeners)
		{
			callback.onTaskComplete(this);
		}
	}
}
