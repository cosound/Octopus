package com.chaos.octopus.server;

import java.util.*;

import com.google.gson.*;

public class Job 
{
	public String id;
	public ArrayList<Step> steps = new ArrayList<Step>();		
	
	public Job(JsonObject json) 
	{
		id = json.get("id").getAsString();
		
		JsonArray array = json.get("steps").getAsJsonArray();
		
		for (JsonElement element : array) 
		{
			steps.add(new Step((JsonObject) array.get(0)));
		}
	}
}
