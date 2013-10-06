package com.chaos.octopus.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;

public class AgentProxy implements Runnable, AutoCloseable
{
	private Socket _Socket;
	private List<String> _SupportedPlugins;
	private Thread _Thread;
	private boolean _IsRunning;
	
	public AgentProxy(Socket socket) throws IOException
	{
		set_Socket(socket);
		_Thread = new Thread(this);
		
		_IsRunning = true;
		_Thread.start();
	}

	public Socket get_Socket()
	{
		return _Socket;
	}

	public void set_Socket(Socket _Socket)
	{
		this._Socket = _Socket;
	}

	public List<String> get_SupportedPlugins()
	{
		if(_SupportedPlugins == null)
		{
			try
			{
				get_Socket().getOutputStream().write(Commands.LIST_SUPPORTED_PLUGINS.getBytes());
				
				String plugins = StreamUtilities.ReadString(get_Socket().getInputStream());
				
				_SupportedPlugins = new ArrayList<String>();
				
				for (String s : plugins.split(";"))
				{
					_SupportedPlugins.add(s);					
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

	public void enqueue(String task) 
	{
		try
		{
			get_Socket().getOutputStream().write((Commands.ENQUEUE_TASK + ":" + task).getBytes());
			
			// TODO no data received is thrown when the run() loop catches the data instead, add appropriate synchronization
			String response = StreamUtilities.ReadString(get_Socket().getInputStream());
			
			System.out.println("enqueue: "+response);
			
			if(!"OK".equals(response)) throw new IOException("Agent didnt queue task");
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() 
	{
		while(_IsRunning)
		{
			try 
			{
				if(get_Socket().getInputStream().available() == 0) continue;
				
				String result = StreamUtilities.ReadString(get_Socket().getInputStream());
				
				if("DONE".equals(result))
				{
					// TODO task is complete, do what is necessary with the result
					System.out.println("Task is done");
				}
				
				System.out.println("Orchestrator: " + result);
			} 
			catch (IOException ioe) 
			{
				if(get_Socket().isClosed()) return;
				
				ioe.printStackTrace();
			}
			catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void close() throws Exception 
	{
		_IsRunning = false;
		
		get_Socket().close();
	}
}
