package com.chaos.octopus.server;

import com.chaos.octopus.commons.core.Job;

import java.util.ArrayList;
import java.util.List;

public class ConcurrentJobQueue
{
    private List<Job> _jobs;

    public ConcurrentJobQueue()
    {
        _jobs = new ArrayList<>();
    }

    public void put(Job job)
    {
        synchronized (_jobs)
        {
            for(int i = 0; i < _jobs.size(); i++)
            {
                Job j = _jobs.get(i);

                if(j.id.equals(job.id))
                {
                    _jobs.set(i, job);
                    return;
                }
            }

            _jobs.add(job);
        }
    }

    public Iterable<Job> popAll()
    {
        ArrayList<Job> result = new ArrayList<>();

        synchronized (_jobs)
        {
            while(_jobs.size() != 0)
            {
                Job job = _jobs.get(0);
                result.add(job);
                _jobs.remove(0);
            }
        }

        return result;
    }
}
