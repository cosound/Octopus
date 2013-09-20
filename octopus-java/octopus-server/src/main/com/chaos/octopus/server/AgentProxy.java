package com.chaos.octopus.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
				get_Socket().getOutputStream().write("supported-plugin-list".getBytes());
				
				String plugins = readString(get_Socket().getInputStream());
				
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
	
	public void set_SupportedPlugins(List<String> supportedPlugins)
	{
		this._SupportedPlugins = supportedPlugins;
	}
}
