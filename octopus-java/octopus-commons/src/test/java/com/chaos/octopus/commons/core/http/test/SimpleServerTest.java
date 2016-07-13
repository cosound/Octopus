package com.chaos.octopus.commons.core.http.test;

import com.chaos.octopus.commons.core.Endpoint;
import com.chaos.octopus.commons.core.Request;
import com.chaos.octopus.commons.core.Response;
import com.chaos.octopus.commons.http.SimpleServer;
import org.junit.Test;

public class SimpleServerTest {
  @Test
  public void nothing() throws InterruptedException {
    SimpleServer ss = new SimpleServer(8080);
    ss.addEndpoint("test", new TestEndpoint());

    //Thread.sleep(100000);
  }

  private class TestEndpoint implements Endpoint {
    public Response invoke(Request requeste) {
      System.out.println("Routed to TestEndpoint");

      return new Response();
    }
  }
}
