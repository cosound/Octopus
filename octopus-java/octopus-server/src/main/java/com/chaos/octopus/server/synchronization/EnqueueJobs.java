/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.server.synchronization;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.core.Orchestrator;
import com.chaos.sdk.AuthenticatedChaosClient;

import java.io.IOException;

public class EnqueueJobs implements SynchronizationTask {
  private Orchestrator orchestrator;
  private AuthenticatedChaosClient client;

  public EnqueueJobs(Orchestrator orchestrator, AuthenticatedChaosClient client) {
    this.orchestrator = orchestrator;
    this.client = client;
  }

  @Override
  public void action() {
    try {
      for (Job job : client.jobGet()) {
        enqueueNewJob(job);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void enqueueNewJob(Job job) throws IOException {
    boolean isValidNewJob = job.validate() && !job.isComplete();

    if (isValidNewJob) orchestrator.enqueue(job);
    else client.jobSet(job);
  }
}
