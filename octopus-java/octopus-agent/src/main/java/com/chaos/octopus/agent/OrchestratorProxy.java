package com.chaos.octopus.agent;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.core.Message;
import com.chaos.octopus.core.Orchestrator;
import com.chaos.octopus.core.Task;
import com.chaos.octopus.core.TaskMessage;
import com.google.gson.Gson;

public class OrchestratorProxy implements Orchestrator
{
	private String _Hostname;
    private int    _Port;
    private int    _ListenPort;
    private Socket _Socket;
    private Gson   _Gson;
    
	public OrchestratorProxy(String hostname, int port, int listenPort)
	{
        _Gson       = new Gson();
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
	
	public void taskCompleted(Task task)
	{
		
		try 
		{
            try(Socket socket = new Socket(_Hostname, _Port))
			{
                TaskMessage msg = new TaskMessage(Commands.TASK_DONE, task);

				socket.getOutputStream().write(msg.toJson().getBytes());
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
