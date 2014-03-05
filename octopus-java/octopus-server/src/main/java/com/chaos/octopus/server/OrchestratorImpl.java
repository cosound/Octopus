package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.chaos.octopus.server.synchronization.EnqueueJobs;
import com.chaos.octopus.server.synchronization.Synchronization;
import com.chaos.octopus.server.synchronization.UpdateJob;
import com.chaos.sdk.AuthenticatedChaosClient;
import com.chaos.sdk.Chaos;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class OrchestratorImpl implements Orchestrator, Runnable
{
    private final ConcurrentJobQueue _jobsWithUpdates;
    private boolean           _isRunning = false;
	private Thread            _thread;
	private ServerSocket      _socket;
	private int               _port;
    private Gson              _Gson;
    private AllocationHandler _AllocationHandler;
    private Synchronization   _synchronization;
    private Gson gson;

    public OrchestratorImpl(int port)
	{
        this(port, new Synchronization(), new ConcurrentJobQueue());
    }

    private OrchestratorImpl(int listeningPort, Synchronization sync, ConcurrentJobQueue queue)
    {
        _AllocationHandler = new AllocationHandler();
        _Gson = new Gson();
        _port = listeningPort;

        _jobsWithUpdates = queue;
        _synchronization = sync;
        gson = new Gson();
    }

    public static OrchestratorImpl create(OctopusConfiguration config) throws IOException
    {
        Synchronization sync = new Synchronization();
        ConcurrentJobQueue queue = new ConcurrentJobQueue();
        OrchestratorImpl leader = new OrchestratorImpl(config.getListeningPort(), sync, queue);

        Chaos chaos = new Chaos(config.getChaosApiUrl());
        AuthenticatedChaosClient client = chaos.authenticate(config.getChaosApiKey());
        sync.addSynchronizationTask(new EnqueueJobs(leader, client));
        sync.addSynchronizationTask(new UpdateJob(queue, client));

        return leader;
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

            _synchronization.synchronize(10 *1000); // synchronize every 60 seconds
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
				Socket agent = _socket.accept();
				
				String result = StreamUtilities.ReadString(agent.getInputStream());
				
				Message message = tryParseJson(result, Message.class);

                switch (message.getAction())
                {
                    case Commands.CONNECT:
                    {
                        ConnectMessage connect = tryParseJson(result, ConnectMessage.class);
                        AgentProxy     ap      = new AgentProxy(connect.get_Hostname(), connect.get_Port());

                        _AllocationHandler.addAgent(ap);
                        break;
                    }
                    case Commands.TASK_DONE:
                    {
                        TaskMessage msg = tryParseJson(result, TaskMessage.class);

                        taskCompleted(msg.getTask());

                        break;
                    }
                    case Commands.TASK_UPDATE:
                    {
                        TaskMessage taskMessage = tryParseJson(result, TaskMessage.class);

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

    private <T> T tryParseJson(String value, Class<T> type)
    {
        try
        {
            return gson.fromJson(value, type);
        }
        catch (JsonSyntaxException e)
        {
            System.err.println("Critial error: JsonSyntaxException ===");
            System.err.println(value);
            System.err.println("======================================");

            throw e;
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

        Job job = _AllocationHandler.getJob(task);

        _jobsWithUpdates.put(job);
    }

    @Override
    public int get_localListenPort()
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
