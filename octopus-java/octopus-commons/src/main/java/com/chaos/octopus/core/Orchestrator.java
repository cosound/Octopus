package com.chaos.octopus.core;

public interface Orchestrator
{
	void open();
	void taskCompleted(Task task);
    void taskUpdate(Task task);

	int get_ListenPort();
}
