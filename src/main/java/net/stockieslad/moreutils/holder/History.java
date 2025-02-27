package net.stockieslad.moreutils.holder;

import net.stockieslad.moreutils.holder.series.Series;

import java.util.function.Function;

/**
 * Handles mutations to it's value by storing any new values in a new holder
 * instead of changing the current value. Accessible via {@link History#prev()}.
 * <P>
 * History is immutable, it cannot be changed once a value or reference is set.
 * Each overwrite will be placed into another instance. If the modified field
 * is the stored value, the previous reference will lead to the instance that
 * it came from.
 * <P>
 * In effect, history dynamically caches variables in a functional style. This
 * specifically is designed for math operations like the sequences, where the
 * previous value is a requisite for the next.
 * @param <T> Type of the stored value
 */
public final class History<T> implements AbstractHolder<T> {
    private final T value;
    private final AbstractHolder<T> prev;

    /**
     * Any holder with a previous reference must always have a
     * value to go with it. A constructor that just takes in a
     * previous reference is not provided as doing that defeats the
     * entire idea of {@link AbstractHolder}
     * @param value Initial value
     * @param prev Initial previous reference
     */
    public History(T value, AbstractHolder<T> prev) {
        this.value = value;
        this.prev = prev != null ? prev : this;
    }

    /**
     * Creates a new unlinked history
     * @param value Initial value
     */
    public History(T value) {
        this(value, null);
    }

    /**
     * Use this if the value is not available/known during
     * object construction. It's important however that the
     * value is eventually {@link AbstractHolder#set(Object)}.
     */
    public History() {
        this(null, null);
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public AbstractHolder<T> set(T newValue) {
        return new History<>(newValue, this);
    }

    @Override
    public <X> AbstractHolder<X> transform(Function<T, X> mutator) {
        return new History<>(mutator.apply(this.value));
    }

    @Override
    public AbstractHolder<T> fromArray(T[] newValues) {
        @SuppressWarnings("unchecked")
        AbstractHolder<T>[] array = new AbstractHolder[newValues.length];
        for (int i = 0; i < newValues.length; i++) {
            array[i] = new Series<>(newValues[i],
                    i == 0 ? this : array[i - 1]);
        }
        return array[array.length - 1];
    }

    @Override
    public AbstractHolder<T> prev() {
        return prev;
    }

    @Override
    public AbstractHolder<T> setPrev(AbstractHolder<T> prev) {
        return new History<>(prev.get(), this);
    }

    @Override
    public void addLock() {
    }

    @Override
    public void removeLock() {
    }

    @Override
    public boolean isUnlocked() {
        return true;
    }

    @Override
    public boolean isLocked() {
        return false;
    }
}
