package com.chaos.octopus.agent.endpoint;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.commons.core.Endpoint;
import com.chaos.octopus.commons.core.Request;
import com.chaos.octopus.commons.core.Response;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.util.StreamUtilities;

public class TaskEnqueueEndpoint implements Endpoint {
  private Agent agent;

  public TaskEnqueueEndpoint(Agent agent) {
    this.agent = agent;
  }

  public Response invoke(Request request) {
    String taskJson = request.queryString.get("task");

    Task task = StreamUtilities.ReadJson(taskJson, Task.class);

    agent.enqueue(task);

    // todo add proper OK response
    return new Response();
  }
}
