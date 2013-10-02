package com.chaos.octopus.agent;

import java.io.IOException;
import java.util.*;

import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.chaos.octopus.core.Orchestrator;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 16-07-13
 * Time: 14:20
 */
public class Agent implements Runnable, AutoCloseable
{
    private boolean      _isRunning;
    private Thread		 _thread;
    private Map<String, PluginDefinition> _PluginDefinitions;
    private Queue<Plugin> _queue;
    private ExecutionHandler _executionHandler;
    private Orchestrator _orchestrator;
    
    public Agent(String hostname, int port)
    {
    	this(new OrchestratorProxy(hostname, port));
    }
    
    public Agent(Orchestrator orchestrator)
    {
    	_orchestrator = orchestrator;
    	
    	_isRunning         = false;
    	_thread            = new Thread(this);
    	_queue             = new LinkedList<Plugin>();
    	_PluginDefinitions = new HashMap<String, PluginDefinition>();
    	set_executionHandler(new ExecutionHandler(this, _queue));
    }

	public void open() 
	{
		_orchestrator.open();
		_isRunning = true;
		_thread.start();
	}

	public void run() 
	{
		while(_isRunning)
		{
			try
			{
				// todo refactor so the implementation doesnt depend on the socket
				int available = _orchestrator.get_Socket().getInputStream().available();
				
				if(available == 0) continue;
				
				String message = StreamUtilities.ReadString(_orchestrator.get_Socket().getInputStream());
				
				switch (message)
				{
					case Commands.LIST_SUPPORTED_PLUGINS:
						_orchestrator.get_Socket().getOutputStream().write(serializeSupportedPlugins());
						break;
					default:
						break;
				}
			}
			catch (IOException e)
			{
				// if the socket is closed it means the server is turned off, so we can ignore the exception
				if(!_orchestrator.get_Socket().isClosed()) e.printStackTrace();
			} 
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}	
	}
	
	public void close() throws Exception
	{
		_isRunning = false;
		
		// todo remove direct calls to the socket
		if(_orchestrator.get_Socket() != null) _orchestrator.get_Socket().close();
	}

	public byte[] serializeSupportedPlugins()
	{
		StringBuilder sb = new StringBuilder();
		
		for (PluginDefinition definition : get_SupportedPlugins())
		{
			sb.append(String.format("%s;", definition.get_Id()));
		}
		
		return sb.toString().getBytes();
	}

	public void addPlugin(PluginDefinition pluginFactory)
	{
		_PluginDefinitions.put(pluginFactory.get_Id(), pluginFactory);	
	}

	public List<PluginDefinition> get_SupportedPlugins()
	{
		List<PluginDefinition> list = new ArrayList<PluginDefinition>();
		
		for (PluginDefinition definition : _PluginDefinitions.values())
		{
			list.add(definition);
		}
		
		return list;
	}

	public Plugin enqueue(String payload)
	{
		String pluginId = payload.split(";")[0];
		Plugin plugin   = _PluginDefinitions.get(pluginId).create(payload);
		
		_queue.add(plugin);

		return plugin;
	}

	public Queue<Plugin> get_queue()
	{
		return _queue;
	}

	public ExecutionHandler get_executionHandler()
	{
		return _executionHandler;
	}

	private void set_executionHandler(ExecutionHandler executionHandler)
	{
		_executionHandler = executionHandler;
	}

	public void onTaskComplete(Plugin plugin)
	{
		_orchestrator.taskCompleted(null);
	}
}
