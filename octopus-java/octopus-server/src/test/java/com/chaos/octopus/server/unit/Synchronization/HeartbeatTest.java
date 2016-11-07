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

  private class ChaosMock implements HeartbeatGateway {
    public boolean wasHeartbeatSet = false;

    @Override
    public void set(ClusterState heartbeat) {
      wasHeartbeatSet = true;
    }
  }
}
