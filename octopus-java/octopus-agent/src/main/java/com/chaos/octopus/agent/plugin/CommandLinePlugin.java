package com.chaos.octopus.agent.plugin;

import com.chaos.octopus.core.Plugin;
import com.chaos.octopus.core.PluginDefinition;
import com.chaos.octopus.core.Task;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 16-10-13
 * Time: 15:00
 */
public class CommandLinePlugin implements Plugin, PluginDefinition
{
    private Task _Task;

    public CommandLinePlugin()
    {
    }

    public CommandLinePlugin(Task task)
    {
        this();

        _Task = task;
    }

    private String getCommandLine()
    {
        return _Task.properties.get("commandline");
    }

    @Override
    public String getId()
    {
        return "com.chaos.octopus.CommandLinePlugin, 1.0.0";
    }

    @Override
    public Plugin create(Task task)
    {
        return new CommandLinePlugin(task);
    }

    @Override
    public Task getTask()
    {
        return _Task;
    }

    @Override
    public void execute() throws Exception
    {
        Process process = Runtime.getRuntime().exec(getCommandLine());

        process.waitFor();
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
