package net.stockieslad.moreutils.worker;

import net.stockieslad.moreutils.lock.DynamicLock;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClockWorker extends DynamicLock implements AbstractClockWorker {
    private final Queue<Runnable> tasks;
    private boolean queued;

    public ClockWorker() {
        tasks = new ConcurrentLinkedQueue<>();
        queued = false;
    }

    @Override
    public void run() {
        if (queued && isUnlocked()) {
            addLock();
            queued = false;
            while (!tasks.isEmpty())
                tasks.poll().run();
            removeLock();
        }
    }

    @Override
    public void schedule() {
        queued = true;
    }

    @Override
    public void addTask(Runnable runnable) {
        schedule();
        tasks.add(runnable);
    }

    @Override
    public boolean isQueued() {
        return queued;
    }
}
