package com.chaos.octopus.server.unit;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.commons.core.Orchestrator;
import com.chaos.octopus.server.AgentProxy;
import com.chaos.octopus.server.OrchestratorImpl;
import com.chaos.sdk.v6.dto.ClusterState;
import junit.framework.Assert;
import org.junit.Test;

public class AgentProxyTest {
  @Test
  public void getState_AgentNotConnected_ReturnDisconnectedState(){
    AgentProxy ap = new AgentProxy("localhost", 2000);

    ClusterState.AgentState state = ap.getState();

    Assert.assertEquals("Disconnected", state.state);
  }

  @Test
  public void getState_AgentConnected_ReturnConnectedState() throws Exception {
    try(Orchestrator o = new OrchestratorImpl(3000);
        Agent a = new Agent("localhost", 3000, 2000, 1)){
      o.open();
      a.open();
      AgentProxy ap = new AgentProxy("localhost", 2000);

      ClusterState.AgentState state = ap.getState();

      Assert.assertEquals("Connected", state.state);
      Assert.assertEquals(1, state.parallelism);
    }
  }
}
