package net.stockieslad.moreutils.lock;

import net.stockieslad.moreutils.worker.ClockWorker;

import java.util.concurrent.locks.Lock;

/**
 * A non-thread-based <STRONG>access tracker</STRONG>. Subject to implementation.
 * This is <STRONG>not</STRONG> the same as {@link Lock}! Instead, the use
 * cases are for places like in {@link ClockWorker}, where concurrent
 * modifications occur even within the same thread. The intended functionality
 * is to track access in any context; not restricted to code blocks.
 * <P> <STRONG>Concurrent:</STRONG> {@link DynamicLock}
 * <P> <STRONG>Thread-unsafe:</STRONG> {@link StaticLock}
 * @apiNote Do <STRONG>not</STRONG> use boolean negations for the lock checks.
 */
public interface AccessLock {
    /**
     * Sets or adds a lock.
     */
    void addLock();

    /**
     * Sets or removes a lock.
     */
    void removeLock();

    /**
     * Use in an if check in order to ensure that the subsequent
     * code block is thread-safe.
     * @return Whether there are no active locks.
     */
    boolean isUnlocked();

    /**
     * Intended for use in iterations to break/continue a loop in
     * specific places without having to use negations.
     * @return Whether there are any active locks.
     */
    boolean isLocked();
}
