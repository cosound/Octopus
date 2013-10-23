package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskState;

import java.util.ArrayList;

public class Step 
{
	public ArrayList<Task> tasks = new ArrayList<Task>();

	public boolean hasAvailableTasks() 
	{
		for(Task task : tasks)
		{
			if(task.get_State() == TaskState.New) return false;
		}
		
		return true;
	}
	
	public boolean isCompleted() 
	{
		for(Task task : tasks)
		{
			if(task.get_State() != TaskState.Committed && 
			   task.get_State() != TaskState.Rolledback) return false;
		}
		
		return true;
	}

	public Task nextAvailableTask() 
	{
		for(Task task : tasks)
		{
			if(task.get_State() == TaskState.New) return task;
		}
		
		return null;
	}

    /**
     * Validates the Step for correctness
     *
     * @return True is returned if all the Step is Valid otherwise false
     */
    public boolean validate()
    {
        return !tasks.isEmpty();
    }
}
