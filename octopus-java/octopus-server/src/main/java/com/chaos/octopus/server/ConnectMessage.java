package com.chaos.octopus.server;

public class ConnectMessage extends Message 
{
	private String hostname;
	private String port     = "0";
	
	public String get_Hostname() 
	{
		return hostname;
	}

	public int get_Port() 
	{
		return Integer.parseInt(port);
	}
}
