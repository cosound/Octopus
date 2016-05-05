package com.chaos.octopus.commons.core;

import java.util.HashMap;
import java.util.UUID;

public class Task {
  public String taskId;
  public String pluginId;
  public double progress;
  public HashMap<String, String> properties = new HashMap<String, String>();

  private TaskState _State;
  private String targetAgent = null;

  public Task() {
    taskId = UUID.randomUUID().toString();
    _State = TaskState.New;
  }

  public TaskState get_State() {
    return _State;
  }

  public void set_State(TaskState state) {
    _State = state;
  }

  public boolean isQueueable() {
    return get_State() == TaskState.New;
  }

  public String getTargetAgent() {
    return targetAgent;
  }

  public void setTargetAgent(String ta) {
    targetAgent = ta;
  }
}
