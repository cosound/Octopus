/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.Job;

import java.util.ArrayList;
import java.util.HashMap;

public class ConcurrentJobQueue {
  private HashMap<String, Job> _hashJobs;

  public ConcurrentJobQueue() {
    _hashJobs = new HashMap<>();
  }

  public void put(Job job) {
    synchronized (_hashJobs) {
      _hashJobs.put(job.id, job);
    }
  }

  public Iterable<Job> popAll() {
    ArrayList<Job> result = new ArrayList<>();

    synchronized (_hashJobs.values()) {
      result.addAll(_hashJobs.values());

      _hashJobs.clear();
    }

    return result;
  }

  public boolean contains(String id) {
    return _hashJobs.containsKey(id);
  }
}
