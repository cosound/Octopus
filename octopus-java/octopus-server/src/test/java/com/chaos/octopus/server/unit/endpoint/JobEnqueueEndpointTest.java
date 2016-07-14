package com.chaos.octopus.server.unit.endpoint;

import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.server.endpoint.JobEnqueueEndpoint;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

public class JobEnqueueEndpointTest {

  @Test
  public void invoke_GivenJobWithoutId_CreateIdAndEnqueue(){
    SpyJobEnqueuer jobEnqueuer = new SpyJobEnqueuer();
    JobEnqueueEndpoint endpoint = new JobEnqueueEndpoint(jobEnqueuer);
    Request request = new Request("Job/Enqueue");
    Job job = new Job();
    Step step = new Step();
    Task task = new Task();
    task.pluginId = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
    task.properties.put("sleep", "10");
    step.tasks.add(task);
    job.steps.add(step);
    request.queryString.put("job", new Gson().toJson(job));

    endpoint.invoke(request);

    Assert.assertNotNull(jobEnqueuer.job);
    Assert.assertNotNull(jobEnqueuer.job.id);
    Assert.assertNotEquals("", jobEnqueuer.job.id);
  }

  class SpyJobEnqueuer implements JobEnqueuer{
    public Job job;

    public void enqueue(Job job) {
      this.job = job;
    }
  }
}
