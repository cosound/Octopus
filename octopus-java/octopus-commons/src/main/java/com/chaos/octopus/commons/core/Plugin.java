package com.chaos.octopus.commons.core;

public interface Plugin
{
	String getId();
    Task getTask();

	void execute() throws Exception;
	void rollback();
	void commit();
}
