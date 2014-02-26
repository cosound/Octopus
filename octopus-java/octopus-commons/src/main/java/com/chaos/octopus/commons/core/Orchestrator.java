package com.chaos.octopus.commons.core;

public interface Orchestrator extends AutoCloseable
{
	void open();
	void taskCompleted(Task task);
    void taskUpdate(Task task);

	int get_localListenPort();

    void enqueue(Job job);
}
