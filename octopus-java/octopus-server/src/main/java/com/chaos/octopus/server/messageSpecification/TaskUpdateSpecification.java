/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.server.messageSpecification;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskMessage;
import com.chaos.octopus.server.AllocationHandler;
import com.chaos.octopus.server.ConcurrentJobQueue;

public class TaskUpdateSpecification {
  private final AllocationHandler allocationHandler;
  private final ConcurrentJobQueue jobsWithUpdates;

  public TaskUpdateSpecification(AllocationHandler allocationHandler, ConcurrentJobQueue jobsWithUpdates) {
    this.allocationHandler = allocationHandler;
    this.jobsWithUpdates = jobsWithUpdates;
  }

  public void invoke(String json) {
    TaskMessage taskMessage = TaskMessage.createFromJson(json);

    Task task = taskMessage.getTask();

    try {
      Job job = allocationHandler.getJob(task);
      jobsWithUpdates.put(job);

      allocationHandler.taskUpdate(task);
    } catch (ArrayIndexOutOfBoundsException e) {
      // No job found
    }
  }
}
