package com.chaos.octopus.agent;

import java.io.IOException;
import java.io.InputStream;
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
    private Thread		 _thread;
    private Socket       _socket; 
    
    public Agent(String hostname, int port)
    {
    	_hostname         = hostname;
    	_port             = port;
    	_isRunning        = false;
    	_supportedPlugins = new ArrayList<Plugin>();
    	_thread           = new Thread(this);
    }

	public void open() 
	{
		try
		{
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
				
				String message = readString(_socket.getInputStream());
				
				switch (message)
				{
				case "supported-plugin-list":
					_socket.getOutputStream().write(serializeSupportedPlugins());
					break;
				default:
					break;
				}
			}
			catch (SocketException se)
			{
				// if the socket is closed it means the server is turned off, so we can ignore the exception
				if(!_socket.isClosed()) se.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}	
	}
	
	private String readString(InputStream inputStream) throws Exception
	{
		for(int i = 1000; i > 0; i--)
		{
			int available = inputStream.available();
			
			if(available > 0)
			{
				byte[] buffer = new byte[4096];
				
				int read = inputStream.read(buffer);
			
				return new String(buffer, 0, read);
			}
			Thread.sleep(1);
		}
		
		throw new IOException("no data received");
	}

	public void close() throws Exception
	{
		_isRunning = false;
		
		if(_socket != null) _socket.close();
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
