package com.chaos.octopus.server.endpoint;

import com.chaos.octopus.commons.core.Endpoint;
import com.chaos.octopus.commons.core.Request;
import com.chaos.octopus.commons.core.Response;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.chaos.octopus.server.OrchestratorImpl;

public class TaskCompleteEndpoint implements Endpoint {
  private OrchestratorImpl orchestrator;

  public TaskCompleteEndpoint(OrchestratorImpl orchestrator) {
    this.orchestrator = orchestrator;
  }

  public Response invoke(Request request) {
    String taskJson = request.queryString.get("task");
    Task task = StreamUtilities.ReadJson(taskJson, Task.class);

    orchestrator.taskCompleted(task);

    return new Response();
  }
}
