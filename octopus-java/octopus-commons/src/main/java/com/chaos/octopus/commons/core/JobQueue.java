package com.chaos.octopus.commons.core;

public interface JobQueue {
  void enqueue(Job job);
}
