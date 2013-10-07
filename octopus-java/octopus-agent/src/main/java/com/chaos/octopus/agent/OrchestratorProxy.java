package com.chaos.octopus.agent;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.chaos.octopus.core.Orchestrator;

public class OrchestratorProxy implements Orchestrator
{
	private String       _Hostname;
    private int          _Port;
    private int          _ListenPort;
    private Socket       _Socket; 
    
	public OrchestratorProxy(String hostname, int port, int listenPort)
	{
		_Hostname   = hostname;
		_Port       = port;
		_ListenPort = listenPort;
	}
	
	public void open() 
	{
		try
		{
			// todo extract network logic into a Proxy class that is given via constructor injection.
			try(Socket socket = new Socket(_Hostname, _Port))
			{
				String format = String.format("{\"action\":\"connect\",\"hostname\":\"%s\",\"port\":\"%s\"}", _Hostname, _ListenPort);
				socket.getOutputStream().write(format.getBytes());
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
	
	public Socket get_Socket()
	{
		return _Socket;
	}

	public void set_Socket(Socket _socket)
	{
		_Socket = _socket;
	}
	
	public void taskCompleted(String serialized)
	{
		
		try 
		{
			try(Socket socket = new Socket(_Hostname, _Port))
			{
				socket.getOutputStream().write("{\"action\":\"task-done\"}".getBytes());
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int get_Port() 
	{
		return _Port;
	}
	
	@Override
	public int get_ListenPort() 
	{
		return _ListenPort;
	}
	
}
