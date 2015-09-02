package com.chaos.octopus.agent.action;

import com.chaos.octopus.agent.ExecutionHandler;
import com.chaos.octopus.commons.core.message.AgentStateMessage;
import com.chaos.octopus.commons.util.NetworkingUtil;
import com.chaos.sdk.v6.dto.ClusterState;

import java.io.IOException;
import java.io.OutputStream;

public class AgentStateAction implements AgentAction {
  private final ExecutionHandler executionHandler;

  public AgentStateAction(ExecutionHandler executionHandler) {
    this.executionHandler = executionHandler;
  }

  public void invoke(String message, OutputStream out) throws IOException {
    ClusterState.AgentState state = getState();

    NetworkingUtil.send(new AgentStateMessage(state), out);
  }

  public ClusterState.AgentState getState(){
    ClusterState.AgentState state = new ClusterState.AgentState();
    state.runningSize = executionHandler.getQueueSize() > executionHandler.getParallelism()
                        ? executionHandler.getParallelism()
                        : executionHandler.getQueueSize();
    state.queueSize = executionHandler.getQueueSize();
    state.parallelism = executionHandler.getParallelism();

    return state;
  }
}
