package net.stockieslad.moreutils.holder;

import net.stockieslad.moreutils.lock.StaticLock;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * A virtual pointer that wraps a single value. Allows one to treat values
 * like in C/C++.
 * <P>
 * Allows a single value to be modifiable through functions without using
 * return types. Can be used in place of consumers inside method params.
 * @apiNote See {@link AtomicReference} for a concurrent alternative.
 * @param <T> Type of stored object
 */
public class Pointer<T> extends StaticLock implements ConsumingHolder<T> {
    private T value;

    /**
     * Creates a pointer toward the given value.
     * @param value the value this points to.
     */
    public Pointer(T value) {
        this.value = value;
    }

    /**
     * Creates an empty pointer; points to nothing (null).
     */
    public Pointer() {
        this(null);
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public AbstractHolder<T> set(T newValue) {
        if (isUnlocked())
            this.value = newValue;
        return this;
    }

    @Override
    public <X> AbstractHolder<X> transform(Function<T, X> mutator) {
        return new Pointer<>(mutator.apply(this.value));
    }

    @Override
    public AbstractHolder<T> prev() {
        return this;
    }

    @Override
    public AbstractHolder<T> setPrev(AbstractHolder<T> prev) {
        set(prev.get());
        return this;
    }
}
