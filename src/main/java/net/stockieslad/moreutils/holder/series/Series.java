package net.stockieslad.moreutils.holder.series;

import net.stockieslad.moreutils.event.Event;
import net.stockieslad.moreutils.holder.AbstractHolder;
import net.stockieslad.moreutils.holder.ConsumingHolder;
import net.stockieslad.moreutils.holder.History;
import net.stockieslad.moreutils.lock.StaticLock;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.function.Function;

/**
 * Opposite to {@link History}, A series doesn't create a new head at
 * a mutation and returns it. Instead, the current instance is the head
 * of the series. All the old values will be dumped into the previous
 * reference.
 * <P>
 *     Although similar in functionality to a history, it is not used
 *     for the same thing. It's design concept is more than just
 *     dynamic caching, representing more of a data structure. In a
 *     series, the user decides the order, rather than it being fixed
 *     to chronological order.
 * </P>
 * <P>
 *     An example usage is in {@link Event}, where a link is sorted
 *     according to priority. Unlike other linked nodes, series is
 *     one directional as a variable must only be accessed
 *     from a next, leading instance. The replacement for this
 *     functionality is contingent upon the order the series is in.
 *     This way, the object's position is clamped with algorithms,
 *     rather than found with iteration.
 * </P>
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class Series<T> extends StaticLock implements ConsumingHolder<T> {
    private static final VarHandle PREV_HANDLE;

    static {
        SeriesBuffer.init();
        try {
            PREV_HANDLE = MethodHandles.lookup()
                    .findVarHandle(Series.class, "prev", AbstractHolder.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private T value;
    private AbstractHolder<T> prev;

    /**
     * Any holder with a previous series must always have a
     * value to go with it. A constructor that just takes in a
     * previous series is not provided as doing that defeats the
     * entire idea of {@link AbstractHolder}.
     * @param value Initial value
     * @param prev Initial previous series
     */
    public Series(T value, AbstractHolder<T> prev) {
        this.value = value;
        this.prev = prev;
    }

    /**
     * Creates a new unlinked, single series.
     * @param value Initial value
     */
    public Series(T value) {
        this(value, null);
    }

    /**
     * Use this if the value is not available/known during
     * object construction. It's important however that the
     * value is eventually {@link AbstractHolder#set(Object)}.
     */
    public Series() {
        this(null, null);
    }

    Series<T> setAll(T value, AbstractHolder<T> prev) {
        this.value = value;
        this.prev = prev;
        return this;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public AbstractHolder<T> set(T newValue) {
        if (notLocked) {
            this.prev = SeriesBuffer.get(this.value, this.prev); // Reuse from pool
            this.value = newValue;
        }
        return this;
    }

    @Override
    public <X> AbstractHolder<X> transform(Function<T, X> mutator) {
        return new Series<>(mutator.apply(this.value));
    }

    @Override
    public AbstractHolder<T> fromArray(T[] newValues) {
        @SuppressWarnings("unchecked")
        AbstractHolder<T>[] array = new AbstractHolder[newValues.length];
        for (int i = 0; i < newValues.length; i++) {
            array[i] = new Series<>(newValues[i],
                    i == 0 ? this.prev : array[i - 1]);
        }
        this.prev = array[newValues.length - 1];
        return this;
    }

    @Override
    public AbstractHolder<T> prev() {
        PREV_HANDLE.getAcquire(this);
        return prev != null ? prev : this;
    }

    @Override
    public AbstractHolder<T> setPrev(AbstractHolder<T> prev) {
        this.prev = prev;
        return prev;
    }
}