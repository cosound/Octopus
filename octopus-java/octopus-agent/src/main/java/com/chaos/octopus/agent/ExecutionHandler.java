/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.agent;

import com.chaos.octopus.commons.core.Plugin;
import com.chaos.octopus.commons.core.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutionHandler implements AutoCloseable, TaskCompleteListener {
  private TaskStatusChangeListener _taskStatusChangedListener;
  private ExecutorService _pool;
  private int parallelism;
  private AtomicInteger _currentQueueSize = new AtomicInteger(0);

  public ExecutionHandler(TaskStatusChangeListener agent, int parallelism) {
    this.parallelism = parallelism;

    _pool = Executors.newFixedThreadPool(parallelism);
    _taskStatusChangedListener = agent;
  }

  public void onTaskComplete(Task task) {
    _currentQueueSize.decrementAndGet();
    _taskStatusChangedListener.onTaskComplete(task);
  }

  public void enqueue(Plugin plugin) {
    _currentQueueSize.incrementAndGet();
    ExecutionSlot slot = new ExecutionSlot(plugin);
    slot.addTaskCompleteListener(this);
    slot.addTaskUpdateListener(_taskStatusChangedListener);

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
  public int getQueueSize() {
    return _currentQueueSize.get();
  }
}
