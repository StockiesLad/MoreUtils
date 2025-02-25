package net.stockieslad.moreutils.lock;

/**
 * @apiNote <STRONG>This is not thread-safe!</STRONG>
 * <P>
 * A tool for multiple handling loops simultaneously whilst also providing
 * more structure, efficiency and capability.
 * <pre>
 * {@code
 * public static List<Thing> collect(Thing subVal, List<Thing> values, StaticLock lock) {
 *     if (FILTER_CONDITION) return values;
 *     values.add(subVal)
 *     var entries = subVal.getEntries();
 *     for (var val : entries) {
 *         collect(val, values, lock);
 *         // Since the lock instance is the same through all the loops
 *         // they are locked at the exact same time, instead of having
 *         // to manually check for the condition in every single one
 *         // and 'waiting' for all of them to finish up.
 *         if (lock.isLocked())
 *             return values;
 *         if (values.size() >= 10) {
 *             lock.addLock();
 *             return values;
 *         }
 *     }
 *     return values;
 * }
 * </pre>
 */
public class StaticLock implements AccessLock {
    protected boolean notLocked = true;

    @Override
    public void addLock() {
        notLocked = false;
    }

    @Override
    public void removeLock() {
        notLocked = true;
    }

    @Override
    public boolean isUnlocked() {
        return notLocked;
    }

    @Override
    public boolean isLocked() {
        return !notLocked;
    }
}
