package com.chaos.octopus.server.unit;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.chaos.octopus.core.*;
import com.chaos.octopus.server.*;

public class OrchestratorImplTest
{
	@Test
	public void parsePluginList_GivenListInBinaryFormat_ReturnListOfPlugins() throws Exception
	{
		try(OrchestratorImpl leader = new OrchestratorImpl(0); )
		{
			byte[] data = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0;com.chaos.octopus.agent.unit.TestPlugin, 1.1.0;".getBytes();
			
			List<String> results =  leader.parsePluginList(data);
			
			assertEquals(2, results.size());
			assertEquals("com.chaos.octopus.agent.unit.TestPlugin, 1.0.0", results.get(0));
		}
	}
}