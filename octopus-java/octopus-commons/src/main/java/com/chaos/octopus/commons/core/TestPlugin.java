package com.chaos.octopus.commons.core;

public class TestPlugin implements Plugin, PluginDefinition
{
    private Task _Task;
    private static int number = 0;
    private static Object numberLock = new Object();

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
        this(new Task());
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

        if(getTask().properties.containsKey("number"))
        {
            int num = Integer.parseInt(getTask().properties.get("number"));

            synchronized (numberLock)
            {
                number += num;
            }

            System.out.println(getTask().taskId + ": " + num + ", executing");
        }

        if(getTask().properties.containsKey("sleep"))
        {
            int delay = Integer.parseInt(getTask().properties.get("sleep"));

            Thread.sleep(delay);
        }

        if(getTask().properties.containsKey("number"))
        {
            int num = Integer.parseInt(getTask().properties.get("number"));

            System.out.println(getTask().taskId + ": " + num + ", executed");
        }
        else
            System.out.println(getId() + " executed");

		WasExecuted = true;
	}

    public static int getNumber()
    {
        return number;
    }

	@Override
	public void rollback()
	{
		WasRolledback = true;
	}

	@Override
	public void commit()
	{
		WasCommitted = true;
	}

}