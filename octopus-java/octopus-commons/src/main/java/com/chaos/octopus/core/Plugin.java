package com.chaos.octopus.core;

public interface Plugin
{
	String get_Id();

	void execute() throws Exception;
	void rollback();
	void commit();
}
