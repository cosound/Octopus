package com.chaos.octopus.agent;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chaos.octopus.core.TestPlugin;

public class ExecutionSlotTest
{
	@Test
	public void executed_GivenAPluginThatExecutesSuccessfully_ShouldCallTheCommitMethodOnThePlugin() throws InterruptedException
	{
		TestPlugin plugin = new TestPlugin();
		
		ExecutionSlot slot   = new ExecutionSlot(plugin);
		
		for(int i = 1000; i > 0 && !plugin.WasCommitted; i--)
		{
			Thread.sleep(1);
		}
		
		assertTrue(plugin.WasCommitted);
	}
	
	@Test
	public void rollback_GivenAPluginThatFailsDuringExecution_callTheRollbackMethodOnThePlugin() throws InterruptedException
	{
		TestPlugin plugin = new TestPlugin(true);
		
		ExecutionSlot slot   = new ExecutionSlot(plugin);
		
		for(int i = 1000; i > 0 && !plugin.WasRolledback; i--)
		{
			Thread.sleep(1);
		}
		
		assertTrue(plugin.WasRolledback);
	}
}
