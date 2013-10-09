package com.chaos.octopus.core;

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
