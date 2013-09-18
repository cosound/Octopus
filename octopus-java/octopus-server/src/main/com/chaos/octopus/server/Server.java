package com.chaos.octopus.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server implements Runnable, AutoCloseable
{
	private ArrayList<Socket> _agents; 
	private boolean           _isRunning;
	private Thread            _thread;
	private ServerSocket      _socket;
	private int               _port;
	
	public Server(int port)
	{
		_agents    = new ArrayList<Socket>();
		_port      = port;
		_isRunning = false;
	}

	public ArrayList<Socket> get_Agents() 
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
				String result = readString(agent);

				if("ACK".equals(result))
				{
					get_Agents().add(agent);
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
			}
		}
	}

	private String readString(Socket agent) throws IOException
	{
		byte[] buffer = new byte[100];
		
		int read = agent.getInputStream().read(buffer);
		
		return new String(buffer, 0, read);
	}

	public void close() throws Exception
	{
		_isRunning = false;
		
		if(_socket != null) _socket.close();
		
		for (Socket agent : get_Agents())
		{
			if(agent != null) agent.close();
		}
	}
}
