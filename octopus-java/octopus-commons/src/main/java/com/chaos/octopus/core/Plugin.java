package com.chaos.octopus.core;

public interface Plugin
{
	String getId();
    Task getTask();

	void execute() throws Exception;
	void rollback();
	void commit();
}
