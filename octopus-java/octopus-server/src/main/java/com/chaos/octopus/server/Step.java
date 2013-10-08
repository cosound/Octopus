package com.chaos.octopus.server;

import java.util.ArrayList;

import com.google.gson.*;

public class Step 
{
	public ArrayList<Task> tasks = new ArrayList<Task>();

	public Step(JsonObject json) 
	{	
		for (JsonElement element : json.get("tasks").getAsJsonArray()) 
		{
			JsonObject task = (JsonObject) element;

			Task taskObj = new Task();
			taskObj.pluginId = task.get("pluginId").getAsString();
			tasks.add(taskObj);
			
			System.out.println(taskObj.pluginId);
		}
	}
}
