package com.chaos.octopus.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.chaos.octopus.commons.util.StreamUtilities;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OrchestratorImpl implements Runnable, AutoCloseable
{
	private ArrayList<AgentProxy> _agents; 
	private boolean               _isRunning;
	private Thread                _thread;
	private ServerSocket          _socket;
	private int                   _port;
	
	public OrchestratorImpl(int port)
	{
		_agents    = new ArrayList<AgentProxy>();
		_port      = port;
		_isRunning = false;
	}

	public ArrayList<AgentProxy> get_Agents() 
	{
		synchronized (_agents)
		{
			return _agents;
		}
	}

	public void open() 
	{
		try
		{
			_isRunning = true;
			_socket = new ServerSocket(_port);
			
			_thread = new Thread(this);
			_thread.start();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		Gson gson = new Gson();
		
		while(_isRunning)
		{
			try
			{
				Socket agent = _socket.accept();
				
				String result = StreamUtilities.ReadString(agent.getInputStream());
				
				Message message = gson.fromJson(result, new Message().getClass());
				
				if("connect".equals(message.get_Action()))
				{	
					ConnectMessage connect = gson.fromJson(result, new ConnectMessage().getClass());

					get_Agents().add(new AgentProxy(connect.get_Hostname(), connect.get_Port()));
				}
				else if("task-done".equals(message.get_Action()))
				{
					// TODO task is complete, do what is necessary with the result
					System.out.println("Task is done");
				}
			} 
			catch (SocketException se)
			{
				// if the socket is closed it means the server is turned off, so we can ignore the exception
				if(!_socket.isClosed()) se.printStackTrace();
				
			}
			catch (IOException e)
			{
				e.printStackTrace();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e)
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

	public List<PluginDefinition> parsePluginList(byte[] data)
	{
		String s = new String(data);
		ArrayList<PluginDefinition> pluginDefinitions = new ArrayList<PluginDefinition>();
		
		for (String id : s.split(";"))
		{
			pluginDefinitions.add(new PluginDefinition(id));
		}
		
		return pluginDefinitions;
	}

	public void enqueue(String task)
	{
		// TODO decision logic for selecting an agent to send a task to
		_agents.get(0).enqueue(task);
	}
}