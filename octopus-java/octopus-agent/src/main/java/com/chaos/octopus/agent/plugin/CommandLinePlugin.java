package com.chaos.octopus.agent.plugin;

import com.chaos.octopus.commons.core.Plugin;
import com.chaos.octopus.commons.core.PluginDefinition;
import com.chaos.octopus.commons.core.Task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())))
        {
            String line;

            while ((line = reader.readLine ()) != null)
            {
                Pattern r = Pattern.compile("<PROGRESS>(.*?)</PROGRESS>", Pattern.CASE_INSENSITIVE);
                Matcher m = r.matcher(line);

                if(m.find())
                {
                    getTask().progress = Double.parseDouble(m.group(1));
                }

                r = Pattern.compile("<STATUS>(.*?)</STATUS>", Pattern.CASE_INSENSITIVE);
                m = r.matcher(line);

                if(m.find())
                {
                    setOrAppend("status", m.group(1));
                }

                r = Pattern.compile("<WARNING>(.*?)</WARNING>", Pattern.CASE_INSENSITIVE);
                m = r.matcher(line);

                if(m.find())
                {
                    setOrAppend("warning", m.group(1));
                    System.err.println ("warning: " + m.group(1));
                }

                r = Pattern.compile("<EXCEPTION>(.*?)</EXCEPTION>", Pattern.CASE_INSENSITIVE);
                m = r.matcher(line);

                if(m.find())
                {
                    setOrAppend("exception", m.group(1));
                    System.err.println("exception: " + m.group(1));
                }
            }
        }

        process.waitFor();
    }

    private void setOrAppend(String key, String value)
    {
        if(getTask().properties.containsKey(key))
            value = getTask().properties.get(key) + " [###] " + value;

        getTask().properties.put(key, value);
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
