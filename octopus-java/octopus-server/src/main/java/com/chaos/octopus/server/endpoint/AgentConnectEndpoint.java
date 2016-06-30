package com.chaos.octopus.server.endpoint;

import com.chaos.octopus.commons.core.Endpoint;
import com.chaos.octopus.commons.core.Request;
import com.chaos.octopus.commons.core.Response;
import com.chaos.octopus.commons.exception.ConnectException;
import com.chaos.octopus.server.AgentProxy;
import com.chaos.octopus.server.AllocationHandler;

public class AgentConnectEndpoint implements Endpoint {

  private AllocationHandler allocationHandler;

  public AgentConnectEndpoint(AllocationHandler allocationHandler) {
    this.allocationHandler = allocationHandler;
  }

  public Response invoke(Request request) {
    String hostname = request.queryString.get("hostname");
    int port = Integer.parseInt(request.queryString.get("port"));

    try {
      AgentProxy ap = new AgentProxy(hostname, port);
      ap.InitializeAgent();

      allocationHandler.addAgent(ap);
    } catch (ConnectException e) {
      System.err.println("Connection to Agent could not be established, hostname: " + hostname + ", port: " + port);
      e.printStackTrace();
    }

    return new Response();
  }
}
