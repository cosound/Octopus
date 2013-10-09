package com.chaos.octopus.server;

import com.chaos.octopus.core.Plugin;
import com.chaos.octopus.core.PluginDefinition;
import com.chaos.octopus.core.Task;

public class PluginDefinitionImpl implements PluginDefinition {

	private String _Id;
	
	@Override
	public String getId()
	{
		return _Id;
	}

	@Override
	public Plugin create(Task data) {
		// TODO Auto-generated method stub
		return null;
	}

}
