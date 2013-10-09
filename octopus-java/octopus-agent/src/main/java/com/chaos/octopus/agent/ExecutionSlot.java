package com.chaos.octopus.agent;

import java.util.ArrayList;
import java.util.List;

import com.chaos.octopus.core.Plugin;
import com.chaos.octopus.core.TaskState;

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
            _plugin.get_Task().set_State(TaskState.Executing);
			_plugin.execute();
            _plugin.get_Task().set_State(TaskState.Executed);
            _plugin.get_Task().set_State(TaskState.Committing);
			_plugin.commit();
            _plugin.get_Task().set_State(TaskState.Committed);
		}
		catch(Exception e)
		{
            _plugin.get_Task().set_State(TaskState.Rollingback);
			_plugin.rollback();
            _plugin.get_Task().set_State(TaskState.Rolledback);
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
