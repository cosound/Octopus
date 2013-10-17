package com.chaos.octopus.commons.core;

public interface PluginDefinition
{
	String getId();
	Plugin create(Task data);
}
