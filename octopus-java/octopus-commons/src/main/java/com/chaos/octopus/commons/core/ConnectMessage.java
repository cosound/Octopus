package com.chaos.octopus.commons.core;

public class ConnectMessage extends Message
{
	private String hostname;
	private int    port;

    public ConnectMessage(){}
    public ConnectMessage(String hostname, int port)
    {
        setAction("connect");
        this.hostname = hostname;
        this.port     = port;
    }

	public String get_Hostname() 
	{
		return hostname;
	}

	public int get_Port() 
	{
		return port;
	}
}
