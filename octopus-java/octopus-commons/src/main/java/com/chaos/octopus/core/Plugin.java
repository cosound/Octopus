package com.chaos.octopus.core;

public interface Plugin
{
	String getId();
    Task get_Task();

	void execute() throws Exception;
	void rollback();
	void commit();
}
