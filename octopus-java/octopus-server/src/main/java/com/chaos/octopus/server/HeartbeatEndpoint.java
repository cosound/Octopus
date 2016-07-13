package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.Endpoint;
import com.chaos.octopus.commons.core.Request;
import com.chaos.octopus.commons.core.Response;

public class HeartbeatEndpoint implements Endpoint {
  @Override
  public Response invoke(Request request) {
    return new Response();
  }
}
