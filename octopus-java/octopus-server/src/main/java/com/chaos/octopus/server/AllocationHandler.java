package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.core.Step;
import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskState;

import java.util.ArrayList;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 10.10.13
 * Time: 15:33
 */
public class AllocationHandler implements AutoCloseable
{
    private ArrayList<AgentProxy> _agents;
    private ArrayList<Job>        _Jobs;
    private boolean               _IsRunning;

    public AllocationHandler()
    {
        _agents    = new ArrayList<AgentProxy>();
        _Jobs      = new ArrayList<Job>();
    }

    public void addAgent(AgentProxy agent)
    {
        _agents.add(agent);
    }

    public void enqueue(Job job)
    {
        synchronized (_Jobs)
        {
            for(Job j : _Jobs)
            {
                if(j.id.equals(job.id)) return;
            }

            _Jobs.add(job);

            enqueueNextTaskOnAgent();
        }
    }

    public ArrayList<AgentProxy> getAgents()
    {
        synchronized (_agents)
        {
            return _agents;
        }
    }

    private void enqueueNextTaskOnAgent()
    {
        for (Job job : _Jobs)
        {
            for(Task task : job.getTasks())
            {
                // TODO only enqueue if there are available ressources
                enqueue(task);
            }
        }
    }

    public void enqueue(Task task)
    {
        // TODO decision logic for selecting an agent to send a task to
        for (AgentProxy agent : getAgents())
        {
            // TODO Make sure a task is only sent to one agent
            task.set_State(TaskState.Queued);
            agent.enqueue(task);
        }
    }

    public void taskUpdate(Task task)
    {
        Job job = getJob(task);

        job.replaceTask(task);

    }

    public void taskComplete(Task task)
    {
        for (AgentProxy agent : getAgents())
        {
            agent.taskCompleted(task);
        }

        enqueueNextTaskOnAgent();
    }

    @Override
    public void close() throws Exception
    {
        _IsRunning = false;
    }

    public Job getJob(Task task) throws ArrayIndexOutOfBoundsException
    {
        Job result = null;

        synchronized (_Jobs)
        {
            for(Job job : _Jobs)
            {
                if(job.containsTask(task.taskId))
                    result = job;
            }
        }

        if(result != null)
            return result;

        throw new ArrayIndexOutOfBoundsException("Job containing given task not found");
    }
}
