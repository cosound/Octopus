package com.chaos.octopus.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;

public class AgentProxy
{
	private List<String> _SupportedPlugins;
	private String _Hostname;
	private int    _Port;
	private int    _AvailableSlots;
	
	public AgentProxy(String hostname, int port) 
	{
		_Hostname = hostname;
		_Port     = port;
		
		_AvailableSlots = 4;
	}

	public List<String> get_SupportedPlugins()
	{
		if(_SupportedPlugins == null)
		{
			try
			{
				try(Socket socket = new Socket(_Hostname, _Port))
				{
					socket.getOutputStream().write(Commands.LIST_SUPPORTED_PLUGINS.getBytes());
					
					String plugins = StreamUtilities.ReadString(socket.getInputStream());
					
					_SupportedPlugins = new ArrayList<String>();
					
					for (String s : plugins.split(";"))
					{
						_SupportedPlugins.add(s);					
					}
				}
			} 
			catch (IOException e)
			{
				return new ArrayList<String>();
			} 
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return _SupportedPlugins;
	}
		
	public void set_SupportedPlugins(List<String> supportedPlugins)
	{
		_SupportedPlugins = supportedPlugins;
	}

	private Object _EnqueueBlock = new Object();
	
	public void enqueue(String task) 
	{
		synchronized (_EnqueueBlock) 
		{
			_AvailableSlots--;
			
			try
			{
				try(Socket socket = new Socket(_Hostname, _Port))
				{
					socket.getOutputStream().write((Commands.ENQUEUE_TASK + ":" + task).getBytes());
					
					// TODO no data received is thrown when the run() loop catches the data instead, add appropriate synchronization
					String response = StreamUtilities.ReadString(socket.getInputStream());
					
					System.out.println("enqueue: "+response);
					
					if(!"OK".equals(response)) throw new IOException("Agent didnt queue task");
				}
			} 
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int get_AvailableSlots() 
	{
		return _AvailableSlots;
	}
}
