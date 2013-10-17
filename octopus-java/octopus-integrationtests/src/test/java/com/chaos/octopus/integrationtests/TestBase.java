package com.chaos.octopus.integrationtests;

import com.chaos.octopus.commons.core.Task;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 09.10.13
 * Time: 17:22
 */
public class TestBase
{
    protected Task Make_TestTask()
    {
        Task task = new Task();
        task.pluginId = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";

        return task;
    }
}
