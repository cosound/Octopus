/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.agent;

import com.chaos.octopus.commons.core.Plugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutionHandler implements AutoCloseable, TaskCompleteListener {
  private Agent _agent;
  private ExecutorService _pool;
  private int parallelism;

  public ExecutionHandler(Agent agent, int parallelism) {
    this.parallelism = parallelism;

    _pool = Executors.newFixedThreadPool(parallelism);
    _agent = agent;
  }

  @Override
  public void onTaskComplete(ExecutionSlot completedTask) {
    _agent.onTaskComplete(completedTask.getPlugin().getTask());
  }

  public void enqueue(Plugin plugin) {
    ExecutionSlot slot = new ExecutionSlot(plugin);
    slot.addTaskCompleteListener(this);
    slot.addTaskUpdateListener(_agent);

    _pool.execute(slot);
  }

  @Override
  public void close() throws Exception {
    _pool.shutdown();
    _pool.awaitTermination(5000, TimeUnit.MILLISECONDS);
  }

  public int getParallelism() {
    return parallelism;
  }
}
