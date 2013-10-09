package com.chaos.octopus.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.chaos.octopus.commons.util.StreamUtilities;
import com.chaos.octopus.core.Message;
import com.chaos.octopus.core.Task;
import com.chaos.octopus.core.TaskMessage;
import com.google.gson.Gson;

public class OrchestratorImpl implements Runnable, AutoCloseable
{
	private ArrayList<AgentProxy> _agents; 
	private boolean               _isRunning;
	private Thread                _thread;
	private ServerSocket          _socket;
	private int                   _port;
    private Gson                  _Gson;
	
	public OrchestratorImpl(int port)
	{
        _Gson      = new Gson();
		_agents    = new ArrayList<AgentProxy>();
		_port      = port;
		_isRunning = false;
	}

	public ArrayList<AgentProxy> get_Agents() 
	{
		synchronized (_agents)
		{
			return _agents;
		}
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
				
				if("connect".equals(message.getAction()))
				{	
					ConnectMessage connect = gson.fromJson(result, new ConnectMessage().getClass());

					get_Agents().add(new AgentProxy(connect.get_Hostname(), connect.get_Port()));
				}
				else if("task-done".equals(message.getAction()))
				{
                    TaskMessage msg = _Gson.fromJson(result, TaskMessage.class);

                    notifyAgentsTaskComplete(msg);
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

    private void notifyAgentsTaskComplete(TaskMessage msg)
    {
        for (AgentProxy agent : get_Agents())
        {
            agent.taskCompleted(msg.getTask());
        }
    }

    public void close() throws Exception
	{
		_isRunning = false;
		
		if(_socket != null) _socket.close();
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

	public void enqueue(Task task)
	{
		// TODO decision logic for selecting an agent to send a task to
		for (AgentProxy agent : _agents) 
		{
			agent.enqueue(task);
		}
	}

	public void enqueue(Job job) 
	{
		// TODO replace with proper queuing of jobs and task logic
		for (Step step : job.steps) 
		{
			for (Task task : step.tasks)
			{
				enqueue(task);
			}
			
//			if(!step.get_IsComplete())
//			{
//				for (Task task : step.tasks) 
//				{
//					enqueue(task.pluginId);
//				}
//				
//				break;
//			}
		}
	}
}
