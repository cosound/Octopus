package com.chaos.octopus.server.unit.Synchronization;

import com.chaos.octopus.server.AllocationHandler;
import com.chaos.octopus.server.synchronization.Heartbeat;
import com.chaos.sdk.*;
import com.chaos.sdk.v6.dto.ClusterState;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class HeartbeatTest {
  @Test
  public void action() {
    ChaosMock chaos = new ChaosMock();
    Heartbeat hb = new Heartbeat(new AllocationHandler(), chaos);

    hb.action();

    assertTrue(chaos.wasHeartbeatSet);
  }

  @Test
  public void sdfsdf() throws IOException {
    Chaos chaos = new Chaos("https://dev-api.cosound.dk/v6");

    AuthenticatedChaosClient authenticate = chaos.authenticate("90f4183870e5d60bbb1b595c10f0c48a4edb17a1befeaee3e4146a9d492f0c84");
    ClusterState state = new ClusterState();
    state.jobsInQueue = 1;
    state.agents = new ArrayList<>();
    ClusterState.AgentState agentState = new ClusterState.AgentState();
    agentState.state = "Disconnected";
    state.agents.add(agentState);
    authenticate.set(state);
  }

  private class ChaosMock implements HeartbeatGateway {
    public boolean wasHeartbeatSet = false;

    @Override
    public void set(ClusterState heartbeat) {
      wasHeartbeatSet = true;
    }
  }
}
