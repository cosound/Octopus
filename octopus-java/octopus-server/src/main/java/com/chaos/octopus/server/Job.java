package com.chaos.octopus.server;

import com.chaos.octopus.core.Task;

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
}
