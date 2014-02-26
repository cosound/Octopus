package com.chaos.octopus.agent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.google.gson.Gson;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 16-07-13
 * Time: 14:20
 */
public class Agent implements Runnable, AutoCloseable, TaskUpdatedListener
{
    private AtomicInteger _currentQueueSize;
    private boolean _isRunning;
    private Thread _thread;
    private Map<String, PluginDefinition> _PluginDefinitions;
    private ExecutionHandler _executionHandler;
    private Orchestrator _orchestrator;
    private ServerSocket _Server;
    private Gson _Gson;
    
    public Agent(String orchestratorHostname, int orchestratorPort, int listenPort)
    {
    	this(new OrchestratorProxy(orchestratorHostname, orchestratorPort, listenPort), 4);
    }

    public Agent(String orchestratorHostname, int orchestratorPort, int listenPort, int parrallelism)
    {
        this(new OrchestratorProxy(orchestratorHostname, orchestratorPort, listenPort), parrallelism);
    }
    
    public Agent(Orchestrator orchestrator, int parrallelism)
    {
        _Gson         = new Gson();
    	_orchestrator = orchestrator;
        _currentQueueSize = new AtomicInteger(0);
    	_isRunning         = false;
    	_thread            = new Thread(this);
    	_PluginDefinitions = new HashMap<>();
    	_executionHandler = new ExecutionHandler(this, parrallelism);
    }

	public void open() throws IOException 
	{
		_orchestrator.open();
		_Server = new ServerSocket(_orchestrator.get_localListenPort());
		_isRunning = true;
		_thread.start();
	}

	public void run() 
	{
		while(_isRunning)
		{
			try
			{
				// todo refactor so the implementation doesn't depend on the socket
				try(Socket socket = _Server.accept())
				{
					String message = StreamUtilities.ReadString(socket.getInputStream());

                    Message msg = _Gson.fromJson(message, Message.class);

					switch (msg.getAction())
					{
						case Commands.LIST_SUPPORTED_PLUGINS:
                            AgentConfigurationMessage response = createAgentConfigurationMessage();

							socket.getOutputStream().write(response.toJson().getBytes());
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
				if(!_Server.isClosed()) e.printStackTrace();
			}
		}
	}

    private AgentConfigurationMessage createAgentConfigurationMessage()
    {
        AgentConfigurationMessage message = new AgentConfigurationMessage();

        message.setNumberOfSimulataniousTasks(_executionHandler.getParralelism());

        for (PluginDefinition definition : get_SupportedPlugins())
        {
            message.getSupportedPlugins().add(definition.getId());
        }

        return message;
    }

    public void close() throws Exception
	{
		_isRunning = false;
		
		if(_Server != null)_Server.close();
        if(_executionHandler != null) _executionHandler.close();
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

    // TODO CAN BE REMOVED????
	public List<PluginDefinition> get_SupportedPlugins()
	{
		List<PluginDefinition> list = new ArrayList<>();
		
		for (PluginDefinition definition : _PluginDefinitions.values())
		{
			list.add(definition);
		}
		
		return list;
	}

	public Plugin enqueue(Task task)
	{
		Plugin plugin = _PluginDefinitions.get(task.pluginId).create(task);

        _executionHandler.enqueue(plugin);
        _currentQueueSize.incrementAndGet();

		return plugin;
	}

	public void onTaskComplete(Task task)
	{
        _currentQueueSize.decrementAndGet();
        _orchestrator.taskCompleted(task);
	}

    @Override
    public void onTaskUpdate(Task task)
    {
        _orchestrator.taskUpdate(task);
    }

    public int getQueueSize()
    {
        return _currentQueueSize.get();
    }
}
