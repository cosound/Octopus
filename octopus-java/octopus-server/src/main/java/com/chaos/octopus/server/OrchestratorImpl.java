package com.chaos.octopus.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.chaos.octopus.server.synchronization.EnqueueJobs;
import com.chaos.octopus.server.synchronization.Synchronization;
import com.chaos.octopus.server.synchronization.UpdateJob;
import com.chaos.sdk.Chaos;
import com.google.gson.Gson;

public class OrchestratorImpl implements Orchestrator, Runnable
{
    private final Chaos _chaos;
    private final List<Job> _jobsWithUpdates;
    private boolean           _isRunning;
	private Thread            _thread;
	private ServerSocket      _socket;
	private int               _port;
    private Gson              _Gson;
    private AllocationHandler _AllocationHandler;
    private Synchronization   _synchronization;

    public OrchestratorImpl(int port)
	{
        _AllocationHandler = new AllocationHandler();
        _Gson  = new Gson();
		_port = port;
		_isRunning = false;
        _chaos = new Chaos("http://api.cosound.chaos-systems.com");  // TODO move to config file
        _jobsWithUpdates = new ArrayList<>(); // todo encapsulate job updates in a class
        _synchronization = new Synchronization(new EnqueueJobs(this, _chaos), new UpdateJob(_jobsWithUpdates, _chaos));
        _synchronization.synchronize(60 *1000); // synchronize every 60 seconds
	}

	public ArrayList<AgentProxy> getAgents()
	{
        return _AllocationHandler.getAgents();
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
		Gson gson = new Gson();
		
		while(_isRunning)
		{
			try
			{
				Socket agent = _socket.accept();
				
				String result = StreamUtilities.ReadString(agent.getInputStream());
				
				Message message = gson.fromJson(result, new Message().getClass());

                switch (message.getAction())
                {
                    case Commands.CONNECT:
                    {
                        ConnectMessage connect = gson.fromJson(result, new ConnectMessage().getClass());
                        AgentProxy     ap      = new AgentProxy(connect.get_Hostname(), connect.get_Port());

                        _AllocationHandler.addAgent(ap);

                        break;
                    }
                    case Commands.TASK_DONE:
                    {
                        TaskMessage msg = _Gson.fromJson(result, TaskMessage.class);

                        taskCompleted(msg.getTask());

                        break;
                    }
                    case Commands.TASK_UPDATE:
                    {
                        TaskMessage taskMessage = _Gson.fromJson(result, TaskMessage.class);

                        taskUpdate(taskMessage.getTask());

                        break;
                    }
                }
			}
			catch (SocketException se)
			{
				// if the socket is closed it means the server is turned off, so we can ignore the exception
				if(!_socket.isClosed()) se.printStackTrace();
				
			}
			catch (IOException | InterruptedException e)
			{
				e.printStackTrace();
			}
        }
	}

    @Override
    public void taskCompleted(Task task)
    {
        _AllocationHandler.taskComplete(task);
    }

    @Override
    public void taskUpdate(Task task)
    {
        _AllocationHandler.taskUpdate(task);
    }

    @Override
    public int get_ListenPort()
    {
        return _port;
    }

    @Override
    public void enqueue(Job job)
    {
        _AllocationHandler.enqueue(job);
    }

    public void close() throws Exception
	{
		_isRunning = false;
		
		if(_socket != null) _socket.close();
        if(_AllocationHandler != null) _AllocationHandler.close();
	}

	public List<String> parsePluginList(byte[] data)
	{
		String s = new String(data);
		ArrayList<String> pluginDefinitions = new ArrayList<String>();
		
		for (String id : s.split(";"))
		{
			pluginDefinitions.add(id);
		}
		
		return pluginDefinitions;
	}


    public Synchronization get_synchronization()
    {
        return _synchronization;
    }
}
