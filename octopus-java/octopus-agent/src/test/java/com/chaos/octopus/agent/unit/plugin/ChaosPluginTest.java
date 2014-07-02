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
        task.properties.put("action", "object.createFromJson");

        String result = plugin.getAction();

        assertEquals("object.createFromJson", result);
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

    @Test
    public void getApiKey_GivenObjectCreate_ReturnApiKey()
    {
        Task task          = new Task();
        ChaosPlugin plugin = new ChaosPlugin(task);
        task.properties.put("chaos-apikey", "b22058bb0c7b2fe4bd3cbffe99fe456b396cbe2083be6c0fdcc50b706d8b4270");

        String result = plugin.getApiKey();

        assertEquals("b22058bb0c7b2fe4bd3cbffe99fe456b396cbe2083be6c0fdcc50b706d8b4270", result);
    }

    @Test
    public void getObjectTypeId_GivenObjectCreate_ReturnObjectTypeId()
    {
        Task task          = new Task();
        ChaosPlugin plugin = new ChaosPlugin(task);
        task.properties.put("chaos-objecttypeid", "1");

        int result = plugin.getObjectTypeId();

        assertEquals(1, result);
    }

    @Test
    public void getFolderId_GivenObjectCreate_ReturnFolderId()
    {
        Task task          = new Task();
        ChaosPlugin plugin = new ChaosPlugin(task);
        task.properties.put("chaos-folderid", "1");

        int result = plugin.getFolderId();

        assertEquals(1, result);
    }

    @Test
    public void getMetadataSchemaId_GivenObjectCreate_ReturnMetadataSchemaId()
    {
        Task task          = new Task();
        ChaosPlugin plugin = new ChaosPlugin(task);
        task.properties.put("chaos-metadataschemaid", "30393865-6330-3632-2D33-6664662D3131");

        String result = plugin.getMetadataSchemaId();

        assertEquals("30393865-6330-3632-2D33-6664662D3131", result);
    }
}
