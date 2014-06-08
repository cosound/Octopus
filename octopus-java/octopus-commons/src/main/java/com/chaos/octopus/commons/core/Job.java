package com.chaos.octopus.commons.core;

import java.util.*;

public class Job 
{
	public String id;
	public ArrayList<Step> steps = new ArrayList<>();
    public String status;

    public Job()
    {
        id = "";
        status = "new";
    }

    public boolean validate()
    {
        for (Step step : steps)
        {
            if(step == null || !step.validate()) return false;
        }

        return true;
    }

    public Iterable<Task> getTasks(TaskState... criteria)
    {
        try
        {
            for(Step step : steps)
            {
                if(step.isFailed()) break;

                if(!step.isCompleted())
                    return step.getTasks(criteria);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public boolean isComplete()
    {
        for(Step step : steps)
        {
            if(step.isFailed()) return true;
            if(!step.isCompleted()) return false;
        }

        return true;
    }

    public Boolean containsTask(String taskId)
    {
        for(Step step : steps)
        {
            for(Task task : step.tasks)
            {
                if(task.taskId.equals(taskId))
                    return true;
            }
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

    public void resume()
    {
        for(Task task : getTasks(new TaskState[]{TaskState.Executing, TaskState.Queued}))
        {
            task.set_State(TaskState.New);
        }
    }

    public void print()
    {
        System.out.println("Job enqued: " + id);

        for(Step step : steps)
        {
            System.out.println("\tStep: " + step.tasks.size());

            for(Task task : step.tasks)
            {
                System.out.println("\t\t" + task.get_State() + " " + task.taskId);
            }
        }
    }
}
