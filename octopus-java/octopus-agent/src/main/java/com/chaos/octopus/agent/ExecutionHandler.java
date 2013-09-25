
package com.chaos.octopus.agent;

import java.util.Queue;

public class ExecutionHandler implements Runnable, AutoCloseable
{
	private Queue<Plugin> _queue;
	private Thread _thread;
	private ExecutionSlot[] _executionSlots;
	private boolean _isRunning;
	
	public ExecutionHandler(Queue<Plugin> queue)
	{
		_isRunning      = true;
		_executionSlots = new ExecutionSlot[4];
			
		_queue = queue;
		
		_thread = new Thread(this);
		_thread.start();
	}

	@Override
	public void run()
	{
		while(_isRunning)
		{
			try
			{
				Thread.sleep(100);
				
				int index = getIndexOfEmptySlot();
				
				if(index != -1 && !_queue.isEmpty())
				{
					Plugin plugin = _queue.poll();
					
					_executionSlots[index] = new ExecutionSlot(this, plugin);
				}
			} 
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private int getIndexOfEmptySlot()
	{
		for (int i = 0; i < _executionSlots.length; i++)
		{
			if(_executionSlots[i] == null) return i;
		}
		
		return -1;
	}
	
	@Override
	public void close() throws Exception
	{
		_isRunning = false;
	}
}
