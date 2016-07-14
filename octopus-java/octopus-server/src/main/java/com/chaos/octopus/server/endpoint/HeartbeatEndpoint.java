package com.chaos.octopus.server.endpoint;

import com.chaos.octopus.commons.core.Endpoint;
import com.chaos.octopus.commons.core.Request;
import com.chaos.octopus.commons.core.Response;
import com.chaos.octopus.server.AllocationHandler;
import com.chaos.sdk.v6.dto.ClusterState;

public class HeartbeatEndpoint implements Endpoint {
  private AllocationHandler allocationHandler;
  private final int port;
  private final String hostname;

  public HeartbeatEndpoint(AllocationHandler allocationHandler, int port, String hostname) {
    this.allocationHandler = allocationHandler;
    this.port = port;
    this.hostname = hostname;
  }

  public Response invoke(Request request) {
    ClusterState cs = new ClusterState();
    cs.port = port;
    cs.hostname = hostname;
    cs.queueSize = allocationHandler.getQueued();
    cs.ramUsage = (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    cs.agents = allocationHandler.getAgentStates();

    for (ClusterState.AgentState as: cs.agents) {
      cs.runningSize += as.runningSize;
      cs.parallelism += as.parallelism;
    }

    cs.hasAvailableSlots = cs.runningSize < cs.parallelism;

    return new Response<>(cs);
  }
}
