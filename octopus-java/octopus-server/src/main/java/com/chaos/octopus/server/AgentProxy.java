package com.chaos.octopus.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.*;

import com.chaos.octopus.commons.core.AgentConfigurationMessage;
import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.chaos.octopus.commons.core.Message;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskMessage;
import com.chaos.octopus.commons.exception.DisconnectException;
import com.google.gson.Gson;

public class AgentProxy
{
    private Gson              _Gson;
    private List<String>      _SupportedPlugins;
    private String            _Hostname;
    private int               _Port;
    private int               _MaxNumberOfSimultaneousTasks;
    private Map<String, Task> _AllocatedTasks;

	public AgentProxy(String hostname, int port)
	{
        _AllocatedTasks = new HashMap<>();
        _Gson           = new Gson();
        _Hostname       = hostname;
		_Port           = port;
        InitializeAgent();
	}

	public List<String> get_SupportedPlugins()
	{
		return _SupportedPlugins;
	}

    private void InitializeAgent()
    {
        InitializeAgent(10);
    }

    private void InitializeAgent(int retries)
    {
        String msg = _Gson.toJson(new Message(Commands.LIST_SUPPORTED_PLUGINS));
        String responseString = sendMessage(msg);

        AgentConfigurationMessage response = AgentConfigurationMessage.create(responseString);

        InitializeAgent(response);
    }


    private String sendMessage(String msg)
    {
        return sendMessage(msg, 10);
    }

    private String sendMessage(String msg, int retries)
    {
        try
        {
            try(Socket socket = new Socket(_Hostname, _Port))
            {
                OutputStream out = socket.getOutputStream();

                out.write(msg.getBytes());
                out.flush();

                InputStream in = socket.getInputStream();
                return StreamUtilities.ReadString(in);
            }

        }
        catch (Exception e)
        {
            if(retries > 0)
            {
                sleep(500);
                InitializeAgent(--retries);
            }

            // TODO This exception should be handled at a higher level
            System.err.println("Couldn't connect to: " + _Hostname + ":" + _Port);
            e.printStackTrace();
        }

        return null;
    }

    private void sleep(int millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e1)
        {

        }
    }

    private void InitializeAgent(AgentConfigurationMessage response)
    {
        _SupportedPlugins = new ArrayList<>();

        for (String pluginId : response.getSupportedPlugins())
        {
            _SupportedPlugins.add(pluginId);
        }

        _MaxNumberOfSimultaneousTasks = response.getNumberOfSimulataniousTasks();
    }

    public void set_SupportedPlugins(List<String> supportedPlugins)
	{
		_SupportedPlugins = supportedPlugins;
	}

	private Object _EnqueueBlock = new Object();
	
	public void enqueue(Task task) throws DisconnectException
    {
		synchronized (_EnqueueBlock) 
		{
			try
			{
				try(Socket socket = new Socket(_Hostname, _Port))
				{
                    String msg = _Gson.toJson(new TaskMessage(Commands.ENQUEUE_TASK, task));
					socket.getOutputStream().write(msg.getBytes());

                    Message response = _Gson.fromJson(StreamUtilities.ReadString(socket.getInputStream()), Message.class);

					if(!response.getAction().equals("OK")) throw new IOException("Agent didnt queue task");

                    _AllocatedTasks.put(task.taskId, task);
				}
			} 
			catch (ConnectException e)
			{
                throw new DisconnectException("Agent Disconnected", e);
			}
            catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
    public void taskCompleted(Task task)
    {
        _AllocatedTasks.remove(task.taskId);
    }

    public boolean isQueueFull()
    {
        synchronized (_EnqueueBlock)
        {
            return _MaxNumberOfSimultaneousTasks - _AllocatedTasks.size() == 0;
        }
    }
}
