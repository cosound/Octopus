package com.chaos.octopus.agent.endpoint;

import com.chaos.octopus.agent.ExecutionHandler;
import com.chaos.octopus.commons.core.*;
import com.chaos.sdk.v6.dto.ClusterState;

public class StateGetEndpoint implements Endpoint {
  private ExecutionHandler executionHandler;

  public StateGetEndpoint(ExecutionHandler executionHandler) {
    this.executionHandler = executionHandler;
  }

  public Response invoke(Request request) {
    ClusterState.AgentState state = new ClusterState.AgentState();
    state.runningSize = executionHandler.getQueueSize() > executionHandler.getParallelism()
        ? executionHandler.getParallelism()
        : executionHandler.getQueueSize();
    state.queueSize = executionHandler.getQueueSize();
    state.parallelism = executionHandler.getParallelism();
    state.ramUsage = (int) ((int) Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

    Response<AgentStateResult> response = new Response<>();
    response.Results.add(new AgentStateResult(state));

    return response;
  }
}
