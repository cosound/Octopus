package com.chaos.octopus.core;

import java.net.Socket;
import java.net.SocketImpl;

public interface Orchestrator
{

	Socket get_Socket();

	void open();
	void taskCompleted(Task serialized);

	int get_Port();

	int get_ListenPort();
}
