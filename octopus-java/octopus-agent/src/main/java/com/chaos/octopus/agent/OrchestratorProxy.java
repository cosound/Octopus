package com.chaos.octopus.agent;

import java.io.IOException;
import java.net.Socket;

import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.util.Commands;
import com.google.gson.Gson;

public class OrchestratorProxy implements Orchestrator
{
	private String _Hostname;
    private int    _Port;
    private int    _ListenPort;
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
        ConnectMessage msg = new ConnectMessage(_Hostname, _ListenPort);

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

    private void SendMessage(Message msg) {
        try
        {
            try(Socket socket = new Socket(_Hostname, _Port))
            {
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
	public int get_ListenPort() 
	{
		return _ListenPort;
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
