package com.chaos.octopus.core;

public class TestPlugin implements Plugin, PluginDefinition
{
    private Task _Task;

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
	
	public TestPlugin(Task task)
	{
        _Task = task;
	}

	@Override
	public String getId()
	{
		return "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
	}

    @Override
    public Task getTask()
    {
        return _Task;
    }

    @Override
	public Plugin create(Task task)
	{
		return new TestPlugin(task);
	}

	@Override
	public void execute() throws Exception
	{
		if(_shouldFailing) throw new Exception("TestPlugin failed in execute");
		
		WasExecuted = true;
		
		System.out.println(getId() + " executed");
	}

	@Override
	public void rollback()
	{
		WasRolledback = true;
		
		System.out.println(getId() + " rolledback");
	}

	@Override
	public void commit()
	{
		WasCommitted = true;
		
		System.out.println(getId() + " committed");
	}

}