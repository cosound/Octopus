package com.chaos.octopus.commons.core;

/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
public enum TaskState {
  New,
  Queued,
  Executing,
  Rollingback,
  Committing,
  Executed,
  Rolledback,
  Committed;

  public static TaskState[] isQueueable() {
    return new TaskState[]{
            TaskState.New
    };
  }

  public static TaskState[] isCompleted() {
    return new TaskState[]{
            TaskState.Committed,
            TaskState.Rolledback
    };
  }

  public boolean isIn(TaskState... criteria) {
    if (criteria.length == 0) return true;

    for (TaskState state : criteria) {
      if (state == this) return true;
    }

    return false;
  }
}
