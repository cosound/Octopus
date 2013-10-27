package com.chaos.octopus.integrationtests.plugin;

import com.chaos.octopus.agent.plugin.ChaosPlugin;
import com.chaos.octopus.commons.core.Task;
import org.junit.Test;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 27-10-13
 * Time: 22:28
 */
public class ChaosPluginTest
{
    @Test
    public void execute_GivenObjectCreate_ReturnObjectCreate() throws Exception
    {
        Task task          = new Task();
        ChaosPlugin plugin = new ChaosPlugin(task);
        task.properties.put("action", "object.create");
        task.properties.put("chaos-location", "http://api.cosound.chaos-systems.com");

        plugin.execute();
    }
}
