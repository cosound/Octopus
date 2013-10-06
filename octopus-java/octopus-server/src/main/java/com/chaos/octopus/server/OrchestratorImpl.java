package com.chaos.octopus.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.chaos.octopus.commons.util.StreamUtilities;

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
		while(_isRunning)
		{
			try
			{
				Socket agent = _socket.accept();
				String result = StreamUtilities.ReadString(agent.getInputStream());
				
				if("ACK".equals(result))
				{
					get_Agents().add(new AgentProxy(agent));
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
		
		for (AgentProxy agent : get_Agents())
		{
			if(agent != null) agent.get_Socket().close();
		}
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
}
