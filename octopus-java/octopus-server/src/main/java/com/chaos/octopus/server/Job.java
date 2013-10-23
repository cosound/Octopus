package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.server.exception.JobMalformedException;

import java.util.*;

public class Job 
{
	public String id;
	public ArrayList<Step> steps = new ArrayList<Step>();
	
	public Task nextAvailableTask()
	{
		for (Step step : steps) 
		{
			if(step.hasAvailableTasks() && step.isCompleted()) continue;
			
			return step.nextAvailableTask();
		}
		
		return null;
	}

    /**
     * Validates the job
     *
     * @return True is returned if the job is well formed, otherwise false
     */
    public boolean validate()
    {
        for (Step step : steps)
        {
            if(!step.validate()) return false;
        }

        return true;
    }
}
