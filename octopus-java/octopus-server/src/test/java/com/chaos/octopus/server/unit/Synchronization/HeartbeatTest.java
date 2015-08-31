package com.chaos.octopus.server.unit.Synchronization;

import com.chaos.octopus.server.synchronization.Heartbeat;
import com.chaos.sdk.*;
import junit.framework.Assert;
import org.junit.*;

import java.io.IOException;

public class HeartbeatTest {
  @Test
  public void action() {
    ChaosMock chaos = new ChaosMock();
    Heartbeat hb = new Heartbeat(chaos);

    hb.action();

    Assert.assertTrue(chaos.wasHeartbeatSet);
  }

  @Ignore
  public void sdfsdf() throws IOException {
    Chaos chaos = new Chaos("https://dev-api.cosound.dk/v6");

    AuthenticatedChaosClient authenticate = chaos.authenticate("90f4183870e5d60bbb1b595c10f0c48a4edb17a1befeaee3e4146a9d492f0c84");
    authenticate.set();
  }

  private class ChaosMock implements HeartbeatGateway {
    public boolean wasHeartbeatSet = false;

    @Override
    public void set() {
      wasHeartbeatSet = true;
    }
  }
}
