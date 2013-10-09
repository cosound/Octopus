package com.chaos.octopus.server;

import java.util.*;

import com.google.gson.*;

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
