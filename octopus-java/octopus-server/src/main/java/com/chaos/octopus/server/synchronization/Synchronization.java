/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.server.synchronization;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Synchronization extends TimerTask {
    private List<SynchronizationTask> tasks;
    private ScheduledExecutorService timer;

    public Synchronization(SynchronizationTask... tasks) {
        this.tasks = new ArrayList<>();

        for (SynchronizationTask task : tasks)
            addSynchronizationTask(task);
    }

    public void addSynchronizationTask(SynchronizationTask task) {
        tasks.add(task);
    }

    public void synchronize(int period) {
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleWithFixedDelay(this, 0, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        synchronize();
    }

    public void synchronize() {
        for (SynchronizationTask task : tasks) {
            try {
                task.action();
            } catch (Exception e) {
                System.err.println("Synchronization (" + task.getClass().getName()+ ") - " + e.getMessage());
            }
        }
    }

    public void stop() {
        if (timer != null) timer.shutdownNow();
    }
}
