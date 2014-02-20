package com.chaos.octopus.integrationtests.plugin;

import com.chaos.octopus.agent.plugin.CommandLinePlugin;
import com.chaos.octopus.commons.core.Plugin;
import com.chaos.octopus.commons.core.PluginDefinition;
import com.chaos.octopus.commons.core.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 16-10-13
 * Time: 15:17
 */
public class CommandLinePluginTest
{
    private String fileToCall = "script";

    @Before
    public void before() throws IOException
    {
        File file = new File(fileToCall);

        file.createNewFile();
        file.setExecutable(true);

        try (FileWriter writer = new FileWriter(fileToCall)) {
            writer.write("#!/bin/sh\n\r" +
                    "ECHO \"Hello World\"\n\r" +
                    "ECHO \"<PROGRESS>0.0</PROGRESS>\"\n\r" +
                    "ECHO \"<PROGRESS>0.5</PROGRESS>\"\n\r" +
                    "ECHO \"<WARNING>warning!</WARNING>\"\n\r" +
                    "ECHO \"<PROGRESS>1.0</PROGRESS>\"\n\r" +
                    "ECHO \"<STATUS>SUCCESS</STATUS>\"\n\r" +
                    "exit 0" );

        }
    }

    @After
    public void after()
    {
        File file = new File(fileToCall);

        file.delete();
    }

    @Test
    public void getId_default_ReturnId()
    {
        Task task     = makeTask();
        Plugin plugin = makeCommandLinePluginFactory().create(task);

        String result = plugin.getId();

        assertEquals("com.chaos.octopus.CommandLinePlugin, 1.0.0", result);
    }

    @Test
    public void create_GivenATask_CreateNewPluginInstance()
    {
        PluginDefinition factory = makeCommandLinePluginFactory();
        Task task = makeTask();

        Plugin result = factory.create(task);

        assertNotNull(result);
    }

    @Test
    public void getTask_WithTaskSet_ReturnTask()
    {
        Task task     = makeTask();
        Plugin plugin = makeCommandLinePluginFactory().create(task);

        Task result = plugin.getTask();

        assertEquals(task, result);
    }

    private Task makeTask()
    {
        return new Task();
    }

    private PluginDefinition makeCommandLinePluginFactory()
    {
        return new CommandLinePlugin();
    }
}
