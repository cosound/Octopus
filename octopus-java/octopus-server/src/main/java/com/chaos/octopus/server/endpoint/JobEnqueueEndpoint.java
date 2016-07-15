package com.chaos.octopus.server.endpoint;

import com.chaos.octopus.commons.core.*;
import com.google.gson.Gson;

import java.util.UUID;

public class JobEnqueueEndpoint implements Endpoint {
  private JobQueue jobQueue;

  public JobEnqueueEndpoint(JobQueue jobQueue) {
    this.jobQueue = jobQueue;
  }

  public Response invoke(Request request) {
    Job job = new Gson().fromJson(request.queryString.get("job"), Job.class);

    if(job.id == null || "".equals(job.id))
      job.id = UUID.randomUUID().toString();

    jobQueue.enqueue(job);

    boolean shouldWait = request.queryString.containsKey("wait") && "true".equals(request.queryString.get("wait"));

    while (shouldWait && !job.isComplete()){
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) { }
    }

    Response<Job> response = new Response();
    response.Results.add(job);

    return response;
  }
}
