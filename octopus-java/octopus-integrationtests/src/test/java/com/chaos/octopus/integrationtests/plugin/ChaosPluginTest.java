package com.chaos.octopus.integrationtests.plugin;

import com.chaos.octopus.agent.plugin.ChaosPlugin;
import com.chaos.octopus.commons.core.Task;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 27-10-13
 * Time: 22:28
 */
public class ChaosPluginTest
{
    private String inputFilePath = "output.xml";

    @Before
    public void before() throws IOException
    {
        File file = new File(inputFilePath);

        file.createNewFile();

        try (FileWriter writer = new FileWriter(inputFilePath)) {
            writer.write("<xml>sample output</xml>");
        }
    }

    @After
    public void after()
    {
        File file = new File(inputFilePath);

        file.delete();
    }

    @Ignore
    public void execute_GivenObjectCreate_UpdateProgress() throws Exception
    {
        Task task          = new Task();
        ChaosPlugin plugin = new ChaosPlugin(task);
        task.properties.put("action", "object.createFromJson");
        task.properties.put("input-xmlfilepath", "output.xml");
        task.properties.put("chaos-location", "http://api.cosound.chaos-systems.com");
        task.properties.put("chaos-apikey", "b22058bb0c7b2fe4bd3cbffe99fe456b396cbe2083be6c0fdcc50b706d8b4270");
        task.properties.put("chaos-objecttypeid", "4");
        task.properties.put("chaos-folderid", "7");
        task.properties.put("chaos-metadataschemaid", "30393865-6330-3632-2D33-6664662D3131");

        plugin.execute();

        assertEquals("1.0", String.valueOf(plugin.getTask().progress));
    }
}
