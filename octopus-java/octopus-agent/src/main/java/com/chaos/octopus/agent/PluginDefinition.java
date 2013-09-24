package com.chaos.octopus.agent;

public interface PluginDefinition
{
	String get_Id();
	Plugin create(String data);
}
