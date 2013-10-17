package com.chaos.octopus.integrationtests.plugin;

import com.chaos.octopus.agent.plugin.CommandLinePlugin;
import com.chaos.octopus.commons.core.Plugin;
import com.chaos.octopus.commons.core.PluginDefinition;
import com.chaos.octopus.commons.core.Task;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 16-10-13
 * Time: 15:17
 */
public class CommandLinePluginTest
{
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

    @Test
    public void execute_ExecuteScriptWithProgress_ProgressShouldBeUpdated() throws Exception
    {
        Task task     = makeTask();
        task.properties.put("commandline", "..\\doc\\sample-batch.bat");
        Plugin plugin = makeCommandLinePluginFactory().create(task);

        plugin.execute();

        assertEquals((int)1.0, (int)task.progress);
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
