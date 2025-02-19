package net.stockieslad.moreutils.worker;

import net.stockieslad.moreutils.lock.AccessLock;

/**
 * A tool to execute statements around a clock, it's used to modify specific sets on data
 * in an orderly manner. Such that traditionally single threaded operations can be handled
 * in a safe manner through multiple threads.
 */
public interface AbstractClockWorker extends Runnable, AccessLock {
    /**
     * Schedules an execution for the next iteration;
     * contingent upon the clock being opened.
     */
    void schedule();

    /**
     *
     * @param runnable Executable Args
     */
    void addTask(Runnable runnable);

    /**
     * @return Whether this worker has been scheduled to execute in the next
     * iteration.
     */
    boolean isQueued();

    /**
     *
     * @param runnable Executable args
     * @return Whether the statement had been executed
     */
    default boolean runIfQueued(Runnable runnable) {
        var queued = isQueued();
        if (queued)
            runnable.run();
        return queued;
    }
}
