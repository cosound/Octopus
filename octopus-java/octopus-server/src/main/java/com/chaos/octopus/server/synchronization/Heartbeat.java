package com.chaos.octopus.server.synchronization;

import com.chaos.octopus.server.AllocationHandler;
import com.chaos.sdk.HeartbeatGateway;
import com.chaos.sdk.v6.dto.ClusterState;

public class Heartbeat implements SynchronizationTask {
  private final AllocationHandler allocationHandler;
  private final HeartbeatGateway gateway;

  public Heartbeat(AllocationHandler allocationHandler, HeartbeatGateway gateway) {
    this.allocationHandler = allocationHandler;
    this.gateway = gateway;
  }

  public void action() {
    ClusterState state = new ClusterState();
    state.queueSize = allocationHandler.getQueued();
		state.agents = allocationHandler.getAgentStates();

    gateway.set(state);
  }
}
