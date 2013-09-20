package com.chaos.octopus.agent;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<Plugin> _supportedPlugins;
    
    public Agent(String hostname, int port)
    {
    	_hostname  = hostname;
    	_port      = port;
    	_isRunning = false;
    	_supportedPlugins = new ArrayList<Plugin>();
    }

	public void open() 
	{
		try
		{
			try(Socket socket = new Socket(_hostname, _port))
			{
				byte[] message = "ACK".getBytes();
				socket.getOutputStream().write(message);
			}
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
		}	
	}

	public void close() throws Exception
	{
		_isRunning = false;
	}

	public byte[] serializeSupportedPlugins()
	{
		StringBuilder sb = new StringBuilder();
		
		for (Plugin plugin : get_SupportedPlugins())
		{
			sb.append(String.format("%s;", plugin.get_Id()));
		}
		
		return sb.toString().getBytes();
	}

	public void addPlugin(Plugin plugin)
	{
		_supportedPlugins.add(plugin);		
	}

	public List<Plugin> get_SupportedPlugins()
	{
		return _supportedPlugins;
	}
}
