package com.chaos.octopus.core;

public class TestPlugin implements Plugin, PluginDefinition
{
	public boolean WasExecuted   = false;
	public boolean WasRolledback = false;
	public boolean WasCommitted  = false;
	
	private boolean _shouldFailing = false;
	
	public TestPlugin()
	{
		this(false);
	}
	
	public TestPlugin(boolean shouldFailing)
	{
		_shouldFailing = shouldFailing;
	}
	
	public TestPlugin(String data)
	{

	}

	@Override
	public String get_Id()
	{
		return "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
	}

	@Override
	public Plugin create(String data)
	{
		return new TestPlugin(data);
	}

	@Override
	public void execute() throws Exception
	{
		if(_shouldFailing) throw new Exception("TestPlugin failed in execute");
		
		WasExecuted = true;
		
		System.out.println(get_Id() + " executed");
	}

	@Override
	public void rollback()
	{
		WasRolledback = true;
		
		System.out.println(get_Id() + " rolledback");
	}

	@Override
	public void commit()
	{
		WasCommitted = true;
		
		System.out.println(get_Id() + " committed");
	}

}