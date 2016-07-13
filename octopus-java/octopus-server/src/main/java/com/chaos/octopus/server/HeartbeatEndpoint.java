package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.Endpoint;
import com.chaos.octopus.commons.core.Request;
import com.chaos.octopus.commons.core.Response;
import com.chaos.sdk.v6.dto.ClusterState;

public class HeartbeatEndpoint implements Endpoint {
  private AllocationHandler allocationHandler;

  public HeartbeatEndpoint(AllocationHandler allocationHandler) {
    this.allocationHandler = allocationHandler;
  }

  public Response invoke(Request request) {
    ClusterState cs = new ClusterState();
    cs.jobsInQueue = allocationHandler.getQueued();
    cs.agents = allocationHandler.getAgentStates();

    return new Response<>(cs);
  }
}
