/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskState;
import com.chaos.octopus.commons.exception.ConnectException;
import com.chaos.sdk.v6.dto.ClusterState;

import java.util.ArrayList;
import java.util.List;

public class AllocationHandler implements AutoCloseable {
  private ArrayList<AgentProxy> _agents = new ArrayList<>();
  private ArrayList<Job> _Jobs = new ArrayList<>();

  public void addAgent(AgentProxy agent) {
    synchronized (_agents){
      _agents.add(agent);

      enqueueNextTaskOnAgent();
    }
  }

  public void removeAgent(AgentProxy agent) {
    synchronized (_agents){
      _agents.remove(agent);
    }
  }

  public void enqueue(Job job) {
    synchronized (_Jobs) {
      for (Job j : _Jobs)
        if (j.id.equals(job.id)) return;

      job.print();
      job.resume();

      _Jobs.add(job);

      enqueueNextTaskOnAgent();
    }
  }

  public void dequeue(Job job) {
    synchronized (_Jobs) {
      _Jobs.remove(job);
    }
  }

  private void enqueueNextTaskOnAgent() {
    synchronized (_Jobs) {
      for (Job job : _Jobs)
        for (Task task : job.getTasks(TaskState.isQueueable())) {
          task.setTargetAgent(job.targetAgent);
          enqueue(task);
        }
    }
  }

  private Object enqueueLock = new Object();

  public void enqueue(Task task) {
    synchronized (enqueueLock) {
      for (int i = 0; i < _agents.size(); i++) {
        AgentProxy agent = _agents.get(i);

        if(task.getTargetAgent() != null && !agent.getHostname().equals(task.getTargetAgent())) continue;
        if(agent.isQueueFull()) continue;

        enqueueOrDisconnectAgent(task, agent);

        task.set_State(TaskState.Queued);

        return;
      }
    }
  }

  private void enqueueOrDisconnectAgent(Task task, AgentProxy agent) {
    try {
      agent.enqueue(task);
    } catch (ConnectException e) {
      removeAgent(agent);
    }
  }

  public ArrayList<AgentProxy> getAgents() {
      return _agents;
  }

  public synchronized void taskUpdate(Task task) {
    Job job = getJob(task);

    if (!job.isComplete())
      job.replaceTask(task);
  }

  public synchronized void taskComplete(Task task) {
    taskUpdate(task);

    for (AgentProxy agent : getAgents())
      agent.taskCompleted(task);

    Job job = getJob(task);

    if (task.get_State() == TaskState.Rolledback) {
      job.status = "failed";

      dequeue(job);
    }

    if (job.isComplete())
      dequeue(job);

    enqueueNextTaskOnAgent();
  }

  @Override
  public void close() throws Exception {
  }

  public Job getJob(Task task) throws ArrayIndexOutOfBoundsException {
    synchronized (_Jobs) {
      for (Job job : _Jobs)
        if (job.containsTask(task.taskId))
          return job;
    }

    throw new ArrayIndexOutOfBoundsException("Job containing given key not found");
  }

  public int getQueued() {
    return _Jobs.size();
  }

  public List<ClusterState.AgentState> getAgentStates() {
    List<ClusterState.AgentState> states = new ArrayList<>();

    for (int i = 0; i < getAgents().size(); i++){
      try{
        states.add(getAgents().get(i).getAgentState());
      }
      catch (Error error) {
        _agents.remove(i);
        i--;
      }
    }

    return states;
  }
}
