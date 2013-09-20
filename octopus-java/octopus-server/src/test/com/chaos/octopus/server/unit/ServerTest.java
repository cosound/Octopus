package com.chaos.octopus.server.unit;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.chaos.octopus.server.*;

public class ServerTest
{
	@Test
	public void parsePluginList_GivenListInBinaryFormat_ReturnListOfPlugins() throws Exception
	{
		try(Leader leader = new Leader(0); )
		{
			byte[] data = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0;com.chaos.octopus.agent.unit.TestPlugin, 1.1.0;".getBytes();
			
			List<PluginDefinition> results =  leader.parsePluginList(data);
			
			assertEquals(2, results.size());
			assertEquals("com.chaos.octopus.agent.unit.TestPlugin, 1.0.0", results.get(0).get_Id());
		}
	}
}