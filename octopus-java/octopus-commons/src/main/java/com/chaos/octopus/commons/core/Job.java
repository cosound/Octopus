package com.chaos.octopus.commons.core;

import java.util.*;

public class Job 
{
	public String id;
	public ArrayList<Step> steps = new ArrayList<>();

    public boolean validate()
    {
        for (Step step : steps)
        {
            if(!step.validate()) return false;
        }

        return true;
    }

    public Iterable<Task> getTasks()
    {
        for(Step step : steps)
        {
            if(!step.isCompleted())
                return step.getTasks();
        }

        return new ArrayList<>();
    }

    public boolean isComplete()
    {
        for(Step step : steps)
        {
            if(!step.isCompleted()) return false;
        }

        return true;
    }

    public Boolean containsTask(String taskId)
    {
        for(Task task : getTasks())
        {
            if(task.taskId.equals(taskId))
                return true;
        }

        return false;
    }

    public void replaceTask(Task replace)
    {
        for(Step step : steps)
        {
            if(step.containsTask(replace.taskId))
            {
                step.replaceTask(replace.taskId, replace);

                break;
            }
        }
    }
}
