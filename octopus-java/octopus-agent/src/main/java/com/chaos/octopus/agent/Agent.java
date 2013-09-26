package com.chaos.octopus.agent;

import java.io.IOException;
import java.net.*;
import java.util.*;
import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 16-07-13
 * Time: 14:20
 */
public class Agent implements Runnable, AutoCloseable
{
	private String       _hostname;
    private int          _port;
    private boolean      _isRunning;
    private Thread		 _thread;
    private Socket       _socket; 
    private Map<String, PluginDefinition> _PluginDefinitions;
    private Queue<Plugin> _queue;
    private ExecutionHandler _executionHandler;
    
    public Agent(String hostname, int port)
    {
    	_hostname          = hostname;
    	_port              = port;
    	_isRunning         = false;
    	_thread            = new Thread(this);
    	_queue             = new LinkedList<Plugin>();
    	_PluginDefinitions = new HashMap<String, PluginDefinition>();
    	set_executionHandler(new ExecutionHandler(_queue));
    }

	public void open() 
	{
		try
		{
			// todo extract network logic into a Proxy class that is given via constructor injection.
			_socket = new Socket(_hostname, _port);
			_socket.getOutputStream().write("ACK".getBytes());
			
			_isRunning = true;
			_thread.start();
		} 
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void run() 
	{
		while(_isRunning)
		{
			try
			{
				int available = _socket.getInputStream().available();
				
				if(available == 0) continue;
				
				String message = StreamUtilities.ReadString(_socket.getInputStream());
				
				switch (message)
				{
					case Commands.LIST_SUPPORTED_PLUGINS:
						_socket.getOutputStream().write(serializeSupportedPlugins());
						break;
					default:
						break;
				}
			}
			catch (IOException e)
			{
				// if the socket is closed it means the server is turned off, so we can ignore the exception
				if(!_socket.isClosed()) e.printStackTrace();
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
		
		if(_socket != null) _socket.close();
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
}
