package com.chaos.octopus.agent.plugin;

import com.chaos.octopus.commons.core.Plugin;
import com.chaos.octopus.commons.core.Task;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 23-10-13
 * Time: 17:06
 */
public class ChaosPlugin implements Plugin
{
    private Task _Task;

    @Override
    public String getId()
    {
        return "com.chaos.octopus.plugins.ChaosPlugin, 1.0.0";
    }

    public ChaosPlugin(Task task)
    {
        setTask(task);
    }

    @Override
    public Task getTask()
    {
        return _Task;
    }

    private void setTask(Task task)
    {
        _Task = task;
    }

    @Override
    public void execute() throws Exception
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void rollback()
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void commit()
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
