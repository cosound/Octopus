package com.chaos.octopus.commons.core;

import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskState;

import java.util.ArrayList;
import java.util.Collection;

public class Step 
{
	public Collection<Task> tasks = new ArrayList<>();
	
	public boolean isCompleted() 
	{
		for(Task task : tasks)
		{
			if (task.get_State() != TaskState.Committed &&
			    task.get_State() != TaskState.Rolledback &&
                task.get_State() != TaskState.Executed)
                return false;
		}
		
		return true;
	}

    public boolean validate()
    {
        return !tasks.isEmpty();
    }

    public Iterable<Task> getTasks()
        {
            ArrayList<Task> list = new ArrayList<>();

            for (Task task : tasks)
            {
                if(TaskState.New.equals(task.get_State()))
                    list.add(task);
            }

            return list;
    }
}
