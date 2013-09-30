package com.chaos.octopus.agent;

public interface Plugin
{
	String get_Id();

	void execute() throws Exception;
	void rollback();
	void commit();
}
