package com.chaos.octopus.commons.core;

import com.chaos.octopus.commons.util.StreamUtilities;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 09.10.13
 * Time: 17:25
 */
public class TaskMessage extends Message
{
    private Task task;

    public TaskMessage(){}
    public TaskMessage(String action, Task task)
    {
        setAction(action);
        setTask(task);
    }

    public static TaskMessage createFromJson(String json)
    {
        return StreamUtilities.ReadJson(json, TaskMessage.class);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
