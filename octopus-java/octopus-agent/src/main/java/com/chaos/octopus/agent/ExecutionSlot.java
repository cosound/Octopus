package com.chaos.octopus.agent;

import java.util.ArrayList;
import java.util.List;

import com.chaos.octopus.commons.core.Plugin;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskState;

public class ExecutionSlot implements Runnable
{
	private Thread _thread;
	private Plugin _plugin;
	private List<TaskCompleteListener> _taskCompleteListeners;
	private List<TaskUpdatedListener> _taskUpdateListeners;

	public ExecutionSlot(Plugin plugin)
	{
		_taskCompleteListeners = new ArrayList<TaskCompleteListener>();
        _taskUpdateListeners   = new ArrayList<TaskUpdatedListener>();
		_thread = new Thread(this);
		_plugin = plugin;
		
		_thread.start();
	}

	@Override
	public void run()
	{
		try
		{
            _plugin.getTask().set_State(TaskState.Executing);
            System.out.println(_plugin.getTask().taskId + " " + _plugin.getTask().get_State());
            onTaskUpdated(_plugin.getTask());
			_plugin.execute();
            _plugin.getTask().set_State(TaskState.Executed);
            System.out.println(_plugin.getTask().taskId + " " + _plugin.getTask().get_State());
            onTaskUpdated(_plugin.getTask());
            _plugin.getTask().set_State(TaskState.Committing);
            onTaskUpdated(_plugin.getTask());
			_plugin.commit();
            _plugin.getTask().set_State(TaskState.Committed);
            onTaskUpdated(_plugin.getTask());
		}
		catch(Exception e)
		{
            _plugin.getTask().set_State(TaskState.Rollingback);
            System.out.println(_plugin.getTask().taskId + " " + _plugin.getTask().get_State());
            onTaskUpdated(_plugin.getTask());
			_plugin.rollback();
            _plugin.getTask().set_State(TaskState.Rolledback);
            System.out.println(_plugin.getTask().taskId + " " + _plugin.getTask().get_State());
            onTaskUpdated(_plugin.getTask());
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

    public void addTaskUpdateListener(TaskUpdatedListener listener)
    {
        _taskUpdateListeners.add(listener);
    }
	
	public Plugin getPlugin()
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

    private void onTaskUpdated(Task task)
    {
        for (TaskUpdatedListener callback : _taskUpdateListeners)
        {
            callback.onTaskUpdate(task);
        }
    }
}
