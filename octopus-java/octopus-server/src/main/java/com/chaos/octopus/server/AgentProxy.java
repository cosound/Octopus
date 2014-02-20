package com.chaos.octopus.server;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.*;

import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.chaos.octopus.commons.core.Message;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskMessage;
import com.chaos.octopus.commons.exception.DisconnectException;
import com.google.gson.Gson;

public class AgentProxy
{
    private Gson         _Gson;
	private List<String> _SupportedPlugins;
    private String       _Hostname;
    private int          _Port;
    private int _MaxNumberOfSimultaneousTasks;
    private Map<String, Task> _AllocatedTasks;

	public AgentProxy(String hostname, int port) 
	{
        _AllocatedTasks = new HashMap<String, Task>();
        _Gson           = new Gson();
        _Hostname       = hostname;
		_Port           = port;
		
		_MaxNumberOfSimultaneousTasks = 4;
	}

	public List<String> get_SupportedPlugins()
	{
		if(_SupportedPlugins == null)
		{
			try
			{
				try(Socket socket = new Socket(_Hostname, _Port))
				{
                    String msg = _Gson.toJson(new Message(Commands.LIST_SUPPORTED_PLUGINS));
                    socket.getOutputStream().write(msg.getBytes());
					
					String plugins = StreamUtilities.ReadString(socket.getInputStream());
					
					_SupportedPlugins = new ArrayList<String>();
					
					for (String s : plugins.split(";"))
					{
						_SupportedPlugins.add(s);					
					}
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
	
	public int get_MaxNumberOfSimultaniousTasks()
	{
		return _MaxNumberOfSimultaneousTasks - _AllocatedTasks.size();
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
