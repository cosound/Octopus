package com.chaos.octopus.commons.core.message;

import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.chaos.sdk.v6.dto.ClusterState;

public class AgentStateMessage extends Message{
  private ClusterState.AgentState state;

  public AgentStateMessage() {
    setAction(Commands.AGENT_STATE);
  }

  public AgentStateMessage(ClusterState.AgentState state) {
    this();
    setAction(Commands.AGENT_STATE);
    setState(state);
  }

  public static AgentStateMessage createFromJson(String json) {
    return StreamUtilities.ReadJson(json, AgentStateMessage.class);
  }

  public ClusterState.AgentState getState() {
    return state;
  }

  public void setState(ClusterState.AgentState state) {
    this.state = state;
  }
}
