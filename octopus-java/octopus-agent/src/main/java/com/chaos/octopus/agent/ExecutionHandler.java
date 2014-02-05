
package com.chaos.octopus.agent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.chaos.octopus.commons.core.Plugin;

public class ExecutionHandler implements AutoCloseable, TaskCompleteListener
{
	private Agent _agent;
    private ExecutorService _pool;

	public ExecutionHandler(Agent agent)
	{
        _pool = Executors.newFixedThreadPool(4);
		_agent = agent;
	}

	@Override
	public void onTaskComplete(ExecutionSlot completedTask)
	{
        _agent.onTaskComplete(completedTask.getPlugin().getTask());
	}

    public void enqueue(Plugin plugin)
    {
        ExecutionSlot slot = new ExecutionSlot(plugin);
        slot.addTaskCompleteListener(this);
        slot.addTaskUpdateListener(_agent);

        _pool.execute(slot);
    }

    @Override
    public void close() throws Exception
    {
        _pool.shutdown();
    }
}
