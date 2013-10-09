package com.chaos.octopus.core;

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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
