package com.chaos.octopus.core;

public interface PluginDefinition
{
	String get_Id();
	Plugin create(String data);
}
