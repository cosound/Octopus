package com.chaos.octopus.core;

import java.net.Socket;

public interface Orchestrator
{

	Socket get_Socket();

	void open();
	void taskCompleted(String serialized);
}
