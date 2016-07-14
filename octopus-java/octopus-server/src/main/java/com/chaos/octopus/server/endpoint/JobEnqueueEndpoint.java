package com.chaos.octopus.server.endpoint;

import com.chaos.octopus.commons.core.*;
import com.google.gson.Gson;

import java.util.UUID;

public class JobEnqueueEndpoint implements Endpoint {
  private JobEnqueuer jobEnqueuer;

  public JobEnqueueEndpoint(JobEnqueuer jobEnqueuer) {
    this.jobEnqueuer = jobEnqueuer;
  }

  public Response invoke(Request request) {
    Job job = new Gson().fromJson(request.queryString.get("job"), Job.class);

    if(job.id == null || "".equals(job.id))
      job.id = UUID.randomUUID().toString();

    jobEnqueuer.enqueue(job);

    return new Response();
  }
}
