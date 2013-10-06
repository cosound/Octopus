package com.chaos.octopus.agent;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.chaos.octopus.core.Orchestrator;

public class OrchestratorProxy implements Orchestrator
{
	private String       _Hostname;
    private int          _Port;
    private Socket       _Socket; 
    
	public OrchestratorProxy(String hostname, int port)
	{
		_Hostname = hostname;
		_Port     = port;
	}
	
	public void open() 
	{
		try
		{
			// todo extract network logic into a Proxy class that is given via constructor injection.
			_Socket = new Socket(_Hostname, _Port);
			_Socket.getOutputStream().write("ACK".getBytes());
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
			_Socket.getOutputStream().write("DONE".getBytes());
			System.out.println("wrote: DONE");
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
