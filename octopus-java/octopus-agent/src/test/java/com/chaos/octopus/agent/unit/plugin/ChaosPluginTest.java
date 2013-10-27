package com.chaos.octopus.agent.unit.plugin;

import com.chaos.octopus.agent.plugin.ChaosPlugin;
import com.chaos.octopus.commons.core.Task;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 23-10-13
 * Time: 18:49
 */
public class ChaosPluginTest
{
    @Test
    public void getId_NewInstanse_ReturnId()
    {
        Task task          = new Task();
        ChaosPlugin plugin = new ChaosPlugin(task);

        String result = plugin.getId();

        assertEquals("com.chaos.octopus.plugins.ChaosPlugin, 1.0.0", result);
    }

    @Test
    public void getTask_NewInstanse_ReturnTaskThatWasPassedIn()
    {
        Task task          = new Task();
        ChaosPlugin plugin = new ChaosPlugin(task);

        Task result = plugin.getTask();

        assertEquals(task, result);
    }

    @Test
    public void getAction_GivenObjectCreate_ReturnObjectCreate()
    {
        Task task          = new Task();
        ChaosPlugin plugin = new ChaosPlugin(task);
        task.properties.put("action", "object.create");

        String result = plugin.getAction();

        assertEquals("object.create", result);
    }

    @Test
    public void getInputXml_GivenObjectCreate_ReturnObjectCreate()
    {
        Task task          = new Task();
        ChaosPlugin plugin = new ChaosPlugin(task);
        task.properties.put("input-xmlfilepath", "./somefile.xml");

        String result = plugin.getInputXml();

        assertEquals("./somefile.xml", result);
    }

    @Test
    public void getChaosLocation_GivenObjectCreate_ReturnObjectCreate()
    {
        Task task          = new Task();
        ChaosPlugin plugin = new ChaosPlugin(task);
        task.properties.put("chaos-location", "https://api.chaos-systems.com");

        String result = plugin.getChaosLocation();

        assertEquals("https://api.chaos-systems.com", result);
    }
}
