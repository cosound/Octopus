package com.chaos.octopus.commons.core;

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
