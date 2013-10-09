package com.chaos.octopus.core;

public interface PluginDefinition
{
	String getId();
	Plugin create(Task data);
}
