package com.chaos.octopus.agent.action;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.commons.core.message.Message;
import com.chaos.octopus.commons.core.message.TaskMessage;
import com.chaos.octopus.commons.util.NetworkingUtil;
import com.chaos.octopus.commons.util.StreamUtilities;

import java.io.IOException;
import java.io.OutputStream;

public class EnqueueTaskAction implements AgentAction {

  private final Agent agent;

  public EnqueueTaskAction(Agent agent) {
    this.agent = agent;
  }

  public void invoke(String message, OutputStream out) throws IOException {
    TaskMessage enqueueTask = StreamUtilities.ReadJson(message, TaskMessage.class);

    agent.enqueue(enqueueTask.getTask());

    NetworkingUtil.send(Message.createWithAction("OK").toJson(), out);
  }
}
