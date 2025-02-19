package net.stockieslad.moreutils.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Uses an atomic integer to track and update locks, ensuring that
 * only <STRONG>one access occurs at a time</STRONG>.
 * <P>
 * This is not restricted to threads or specific code blocks. And,
 * not tracked by the JVM and hence not viable candidate for
 * synchronized blocks.
 */
public class DynamicLock implements AccessLock {
    protected final AtomicInteger workingThreads = new AtomicInteger(0);

    /**
     * Adds a lock to the {@link DynamicLock#workingThreads}
     * by incrementing the value.
     */
    @Override
    public void addLock() {
        workingThreads.getAndIncrement();
    }

    /**
     * Removes a lock to the {@link DynamicLock#workingThreads}
     * by decrementing the value.
     */
    @Override
    public void removeLock() {
        workingThreads.getAndDecrement();
    }

    @Override
    public boolean isUnlocked() {
        return workingThreads.get() == 0;
    }

    @Override
    public boolean isLocked() {
        return workingThreads.get() != 0;
    }
}
