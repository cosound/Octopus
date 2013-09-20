package com.chaos.octopus.agent.unit;

import com.chaos.octopus.agent.Plugin;

public class TestPlugin implements Plugin
{

	@Override
	public String get_Id()
	{
		return "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
	}

}
