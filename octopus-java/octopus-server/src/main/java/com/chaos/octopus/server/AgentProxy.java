package com.chaos.octopus.server;

import java.io.IOException;
import java.util.*;

import com.chaos.octopus.commons.core.AgentConfigurationMessage;
import com.chaos.octopus.commons.exception.DisconnectError;
import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.NetworkingUtil;
import com.chaos.octopus.commons.core.Message;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskMessage;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.google.gson.Gson;

public class AgentProxy
{
    private List<String>      _SupportedPlugins;
    private int               _MaxNumberOfSimultaneousTasks;
    private Map<String, Task> _AllocatedTasks;
    private NetworkingUtil _network;

	public AgentProxy(String hostname, int port)
	{
        _AllocatedTasks = new HashMap<>();
        _network = new NetworkingUtil(hostname, port);
        InitializeAgent();
	}

	public List<String> get_SupportedPlugins()
	{
		return _SupportedPlugins;
	}

    private void InitializeAgent()
    {
        String msg = new Message(Commands.LIST_SUPPORTED_PLUGINS).toJson();
        String responseString = _network.sendWithReply(msg);

        AgentConfigurationMessage response = AgentConfigurationMessage.create(responseString);

        InitializeAgent(response);
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
	
	public void enqueue(Task task) throws DisconnectError
    {
		synchronized (_EnqueueBlock) 
		{
			try
			{
                String msg = new TaskMessage(Commands.ENQUEUE_TASK, task).toJson();
                String response = _network.sendWithReply(msg);
                Message parsedResponse = StreamUtilities.ReadJson(response, Message.class);

                if(!parsedResponse.getAction().equals("OK")) throw new IOException("Agent didnt queue task");
                _AllocatedTasks.put(task.taskId, task);
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
