package com.chaos.octopus.server;

public enum TaskState 
{
	New,
	Executing,
	Rollingback,
	Committing,
	Executed,
	Rolledback,
	Committed,
}
