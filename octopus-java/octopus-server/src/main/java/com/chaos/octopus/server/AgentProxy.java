package com.chaos.octopus.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;

public class AgentProxy
{
	private Socket _Socket;
	private List<String> _SupportedPlugins;
	
	public AgentProxy(Socket socket) throws IOException
	{
		set_Socket(socket);
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
		this._SupportedPlugins = supportedPlugins;
	}
}
