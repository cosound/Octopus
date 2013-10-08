package com.chaos.octopus.server;

import com.chaos.octopus.core.Plugin;
import com.chaos.octopus.core.PluginDefinition;

public class PluginDefinitionImpl implements PluginDefinition {

	private String _Id;
	
	@Override
	public String get_Id() 
	{
		return _Id;
	}

	@Override
	public Plugin create(String data) {
		// TODO Auto-generated method stub
		return null;
	}

}
