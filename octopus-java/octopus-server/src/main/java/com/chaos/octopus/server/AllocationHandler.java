package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.Task;
import com.chaos.octopus.commons.core.TaskState;

import java.util.ArrayList;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 10.10.13
 * Time: 15:33
 */
public class AllocationHandler implements Runnable, AutoCloseable
{
    private ArrayList<AgentProxy> _agents;
    private ArrayList<Job>        _Jobs;
    private Thread                _Thread;
    private boolean               _IsRunning;

    public AllocationHandler()
    {
        _agents    = new ArrayList<AgentProxy>();
        _Jobs      = new ArrayList<Job>();
        _Thread    = new Thread(this);
        _IsRunning = true;

        _Thread.start();
    }

    public void addAgent(AgentProxy agent)
    {
        _agents.add(agent);
    }

    public void enqueue(Task task)
    {
        // TODO decision logic for selecting an agent to send a task to
        for (AgentProxy agent : getAgents())
        {
            // TODO Make sure a task is only sent to one agent
            agent.enqueue(task);
        }
    }

    public void enqueue(Job job)
    {
        synchronized (_Jobs)
        {
            _Jobs.add(job);
        }
    }

    public ArrayList<AgentProxy> getAgents()
    {
        synchronized (_agents)
        {
            return _agents;
        }
    }

    @Override
    public void run()
    {
        while(_IsRunning)
        {
            synchronized (_Jobs)
            {
                for (Job job : _Jobs)
                {

                    for(Task task : job.getTasks())
                    {
                        task.set_State(TaskState.Queued);
                        enqueue(task);
                    }
                }
            }

            // TODO remove busy waiting hack, and only activate when new jobs are added, or tasks finish
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public void taskUpdate(Task task)
    {
        // TODO Review: replace with a lookup in a map, assuming tasks are retrieved more often than inserted
        for(Job job : _Jobs)
        {
            for(Step step : job.steps)
            {
                for(Task t : step.tasks)
                {
                    if(t.taskId.equals(task.taskId))
                        t.set_State(task.get_State());
                    // TODO change so entire task is updated, instead of just the state
                }
            }
        }
    }

    public void taskComplete(Task task)
    {
        // TODO Review: replace with a lookup in a map, assuming tasks are retrieved more often than inserted
        for (AgentProxy agent : getAgents())
        {
            agent.taskCompleted(task);
        }
    }

    @Override
    public void close() throws Exception
    {
        _IsRunning = false;
    }
}
