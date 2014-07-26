package com.chaos.octopus.commons.core;

import java.util.ArrayList;

/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
public class Step {
  public ArrayList<Task> tasks = new ArrayList<>();

  public boolean isCompleted() {
    for (Task task : tasks)
      if (task.get_State() != TaskState.Committed && task.get_State() != TaskState.Rolledback && task.get_State() != TaskState.Executed)
        return false;

    return true;
  }

  public boolean isFailed() {
    for (Task task : tasks)
      if (task.get_State() == TaskState.Rolledback || task.get_State() == TaskState.Rollingback)
        return true;

    return false;
  }

  public boolean isFinished() {
    for (Task task : tasks)
      if (task.get_State() != TaskState.Committed)
        return false;

    return true;
  }

  public boolean validate() {
    return !tasks.isEmpty();
  }

  public Iterable<Task> getTasks(TaskState... criteria) {
    ArrayList<Task> list = new ArrayList<>();

    for (Task task : tasks)
      //if(task.isQueueable() || task.get_State() == TaskState.Executing || task.get_State() == TaskState.Queued)
      if (task.get_State().isIn(criteria))
        list.add(task);

    return list;
  }

  public boolean containsTask(String taskId) {
    for (Task task : tasks)
      if (task.taskId.equals(taskId))
        return true;

    return false;
  }

  public void replaceTask(String taskId, Task replace) {
    for (int i = 0; i < tasks.size(); i++) {
      Task task = tasks.get(i);

      if (task.taskId.equals(taskId) && task.get_State() != TaskState.Committed)
        tasks.set(i, replace);
    }
  }
}
