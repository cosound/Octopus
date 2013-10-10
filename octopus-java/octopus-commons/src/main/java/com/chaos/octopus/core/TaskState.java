package com.chaos.octopus.core;

public enum TaskState 
{
	New,
    Queued,
	Executing,
	Rollingback,
	Committing,
	Executed,
	Rolledback,
	Committed
}
