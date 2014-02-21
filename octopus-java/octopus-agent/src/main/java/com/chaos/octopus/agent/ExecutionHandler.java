
package com.chaos.octopus.agent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.chaos.octopus.commons.core.Plugin;

public class ExecutionHandler implements AutoCloseable, TaskCompleteListener
{
	private Agent _agent;
    private ExecutorService _pool;
    private int parrallelism;

    public ExecutionHandler(Agent agent, int parrallelism)
	{
        this.parrallelism = parrallelism;

        _pool = Executors.newFixedThreadPool(parrallelism);
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
        _pool.awaitTermination(5000, TimeUnit.MILLISECONDS);
    }

    public int getParralelism()
    {
        return parrallelism;
    }
}
