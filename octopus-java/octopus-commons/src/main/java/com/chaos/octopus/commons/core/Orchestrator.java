package com.chaos.octopus.commons.core;

import java.net.ConnectException;

public interface Orchestrator extends AutoCloseable
{
	void open() throws ConnectException;
	void taskCompleted(Task task);
    void taskUpdate(Task task);

	int get_localListenPort();

    void enqueue(Job job);
}
