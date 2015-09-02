package com.chaos.octopus.agent.action;

import java.io.IOException;
import java.io.OutputStream;

public interface AgentAction {
  void invoke(String message, OutputStream out) throws IOException;
}
