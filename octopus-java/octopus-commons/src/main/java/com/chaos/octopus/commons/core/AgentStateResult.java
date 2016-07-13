package com.chaos.octopus.commons.core;

import com.chaos.sdk.v6.dto.ClusterState;

public class AgentStateResult {
  public ClusterState.AgentState agentState;

  public AgentStateResult(ClusterState.AgentState agentState) {
    this.agentState = agentState;
  }
}
