package com.chaos.octopus.server;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gson.*;

public class Task 
{
	public String taskId;
	public String pluginId;
	public double progress;
	public HashMap<String, String> properties = new HashMap<String, String>();

}
