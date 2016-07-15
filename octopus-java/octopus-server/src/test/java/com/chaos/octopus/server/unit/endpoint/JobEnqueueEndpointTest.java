package com.chaos.octopus.server.unit.endpoint;

import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.server.endpoint.JobEnqueueEndpoint;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

public class JobEnqueueEndpointTest {

  @Test
  public void invoke_GivenJobWithoutId_CreateIdAndEnqueue(){
    SpyJobQueue jobEnqueuer = new SpyJobQueue();
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

    Response<Job> response = endpoint.invoke(request);

    Assert.assertNotNull(jobEnqueuer.job);
    Assert.assertNotNull(jobEnqueuer.job.id);
    Assert.assertNotEquals("", jobEnqueuer.job.id);
    Assert.assertNotNull(response.Results.get(0).id);
  }

  @Test
  public void invoke_GivenWaitIsTrue_InvokeTaskAndWaitForDone(){
    SpyJobQueue jobEnqueuer = new SpyJobQueue();
    JobEnqueueEndpoint endpoint = new JobEnqueueEndpoint(jobEnqueuer);
    Request request = new Request("Job/Enqueue");
    request.queryString.put("wait", "true");
    Job job = new Job();
    Step step = new Step();
    Task task = new Task();
    task.pluginId = "com.chaos.octopus.agent.unit.TestPlugin, 1.0.0";
    task.properties.put("sleep", "10");
    step.tasks.add(task);
    job.steps.add(step);
    request.queryString.put("job", new Gson().toJson(job));

    Response<Job> response = endpoint.invoke(request);

    Assert.assertNotNull(jobEnqueuer.job);
    Assert.assertNotNull(jobEnqueuer.job.id);
    Assert.assertNotEquals("", jobEnqueuer.job.id);
    Assert.assertTrue(response.Results.get(0).isComplete());
  }

  class SpyJobQueue implements JobQueue {
    public Job job;

    public void enqueue(Job job) {
      job.steps.get(0).tasks.get(0).set_State(TaskState.Committed);
      this.job = job;
    }
  }
}
