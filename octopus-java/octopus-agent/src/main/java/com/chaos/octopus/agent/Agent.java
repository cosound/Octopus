package com.chaos.octopus.agent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.chaos.octopus.core.*;
import com.google.gson.Gson;

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
    private ServerSocket _Server;
    private Gson         _Gson;
    
    public Agent(String hostname, int port, int listenPort)
    {
    	this(new OrchestratorProxy(hostname, port, listenPort));
    }
    
    public Agent(Orchestrator orchestrator)
    {
        _Gson         = new Gson();
    	_orchestrator = orchestrator;
    	
    	_isRunning         = false;
    	_thread            = new Thread(this);
    	_queue             = new LinkedList<Plugin>();
    	_PluginDefinitions = new HashMap<String, PluginDefinition>();
    	set_executionHandler(new ExecutionHandler(this, _queue));
    }

	public void open() throws IOException 
	{
		_orchestrator.open();
		_Server = new ServerSocket(_orchestrator.get_ListenPort());
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
				try(Socket socket = _Server.accept())
				{
					String message = StreamUtilities.ReadString(socket.getInputStream());

                    Message msg = _Gson.fromJson(message, Message.class);

					switch (msg.getAction())
					{
						case Commands.LIST_SUPPORTED_PLUGINS:
							socket.getOutputStream().write(serializeSupportedPlugins());
							break;
						case Commands.ENQUEUE_TASK:
                            TaskMessage enqueueTask = _Gson.fromJson(message, TaskMessage.class);

							enqueue(enqueueTask.getTask());
							socket.getOutputStream().write(_Gson.toJson(new Message("OK")).getBytes());
							break;
						default:
							break;
					}
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				if(!_Server.isClosed()) e.printStackTrace();
			}
			
			
		}	
	}
	
	public void close() throws Exception
	{
		_isRunning = false;
		
		if(_Server != null)_Server.close();
		_executionHandler.close();
	}

	public byte[] serializeSupportedPlugins()
	{
		StringBuilder sb = new StringBuilder();
		
		for (PluginDefinition definition : get_SupportedPlugins())
		{
			sb.append(String.format("%s;", definition.getId()));
		}
		
		return sb.toString().getBytes();
	}

	public void addPlugin(PluginDefinition pluginFactory)
	{
		_PluginDefinitions.put(pluginFactory.getId(), pluginFactory);
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

	public Plugin enqueue(Task task)
	{
		Plugin plugin = _PluginDefinitions.get(task.pluginId).create(task);
		
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
		_orchestrator.taskCompleted(plugin.get_Task());
	}
}
