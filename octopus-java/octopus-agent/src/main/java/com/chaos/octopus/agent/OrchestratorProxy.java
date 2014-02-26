package com.chaos.octopus.agent;

import java.io.IOException;
import java.net.*;
import java.net.ConnectException;

import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.util.Commands;
import com.google.gson.Gson;

public class OrchestratorProxy implements Orchestrator
{
	private String _Hostname;
    private int    _Port;
    private int _localListenPort;
    private Gson   _Gson;
    private String _localHostAddress;

    public OrchestratorProxy(String hostname, int port, int listenPort)
	{
        _Gson       = new Gson();
		_Hostname   = hostname;
		_Port       = port;
        _localHostAddress = getHostAddress();
		_localListenPort = listenPort;
	}

    private String getHostAddress()
    {
        try
        {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();

            System.out.println("Local IP: " + hostAddress);

            return hostAddress;
        }
        catch (UnknownHostException e)
        {
            throw new com.chaos.octopus.commons.exception.ConnectException("Could not determine local host address", e);
        }
    }

    public void open()
	{
        ConnectMessage msg = new ConnectMessage(_localHostAddress, _localListenPort);

        SendMessage(msg);
	}

	public void taskCompleted(Task task)
	{
        TaskMessage msg = new TaskMessage(Commands.TASK_DONE, task);

        SendMessage(msg);
    }

    @Override
    public void taskUpdate(Task task)
    {
        TaskMessage msg = new TaskMessage(Commands.TASK_UPDATE, task);

        SendMessage(msg);
    }

    private void SendMessage(Message msg)
    {
        try
        {
            try(Socket socket = new Socket(_Hostname, _Port))
            {
                socket.getOutputStream().write(msg.toJson().getBytes());
            }
        }
        catch(ConnectException e)
        {
            throw new com.chaos.octopus.commons.exception.ConnectException("Connection to Orchestrator could not be established, check hostname and port", e);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

	@Override
	public int get_localListenPort()
	{
		return _localListenPort;
	}

    @Override
    public void enqueue(Job job)
    {

    }

    @Override
    public void close() throws Exception
    {

    }
}
