/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.server.synchronization;

import com.chaos.octopus.server.ConcurrentJobQueue;
import com.chaos.sdk.AuthenticatedChaosClient;

import java.io.IOException;

public class UpdateJob implements SynchronizationTask {
  private ConcurrentJobQueue jobs;
  private AuthenticatedChaosClient client;

  public UpdateJob(ConcurrentJobQueue jobs, AuthenticatedChaosClient client) {
    this.jobs = jobs;
    this.client = client;
  }

  @Override
  public void action() {
    try {
      client.jobSet(jobs.popAll());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
