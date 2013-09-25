package com.chaos.octopus.agent.unit;

import com.chaos.octopus.agent.Plugin;
import com.chaos.octopus.agent.PluginDefinition;

public class TestPlugin implements Plugin, PluginDefinition
{
	public boolean WasExecuted = false;
	
	public TestPlugin()
	{
	}
	
	public TestPlugin(String data)
	{

	}

	@Override
	public String get_Id()
	{
		return "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
	}

	@Override
	public Plugin create(String data)
	{
		return new TestPlugin(data);
	}

	@Override
	public void execute()
	{
		WasExecuted = true;
		
		System.out.println(get_Id() + " executed");
	}

}
