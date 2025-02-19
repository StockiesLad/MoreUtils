package net.stockieslad.moreutils.holder;

import net.stockieslad.moreutils.event.AbstractEvent;
import net.stockieslad.moreutils.lock.AccessLock;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Stores a single value along with an optional previous reference. In short,
 * it is a single direction chain of nodes that are iterable. Furthermore,
 * there are a set of functions to access specific values via
 * {@link AbstractHolder#prev()}.
 * @param <T> Type of the stored value
 */
public interface AbstractHolder<T> extends Supplier<T>, AccessLock, Iterable<AbstractHolder<T>> {
    /**
     * A simple getter; returns the value without any side effects.
     * @apiNote Return value is <STRONG>not null-safe</STRONG>!
     * @return The internally stored value.
     */
    @Override
    T get();

    /**
     * Changes the stored value with the given one. Since mutability is
     * not guaranteed, the relevant instance is returned. This could be
     * different from the one this function was called on.
     * @param newValue The next value
     * @return The next holder
     */
    AbstractHolder<T> set(T newValue);

    /**
     * Functional alternative to {@link AbstractHolder#get()} and
     * {@link AbstractHolder#set(T)} in single statement.
     * @param mutator A pass-through function
     * @return The next holder (if relevant)
     */
    default AbstractHolder<T> bind(Function<T, T> mutator) {
        return set(mutator.apply(get()));
    }

    /**
     * Wraps the transformed value into a new holder; supports
     * different types at the cost of losing the previous reference.
     * @param function a type transformer.
     * @return A new holder of the new value.
     * @param <X> The new type
     */
    <X> AbstractHolder<X> transform(Function<T, X> function);

    /**
     * Performs a basic check to see if the stored value is null;
     * returns false if null.
     * @return Whether the stored value is not null.
     */
    default boolean hasValue() {
        return get() != null;
    }

    /**
     * Gets the previous reference similar to a node. However, this is
     * <STRONG>not</STRONG> supposed to be a regular storage. The
     * intended purpose is dynamic caching of variables from a previous
     * calculation result; applicative to math. Hence, the lack of a
     * .next() reference.
     * @apiNote Ensure null-safe
     * @return A reference to its previous state.
     */
    AbstractHolder<T> prev();

    /**
     * Sets the {@link AbstractHolder#prev()} return value. Used for
     * branching multiple results from a certain value. Primary applications
     * are math-related. However, this is used {@link AbstractEvent} for
     * {@link Series} uses it to relate event listeners together according
     * to priority.
     * @apiNote Ensure null-safe
     * @param prev The previous reference.
     * @return The newly-redundant previous reference (if applicable).
     */
    AbstractHolder<T> setPrev(AbstractHolder<T> prev);

    /**
     * Searches for the latest instance where the internal holder's stored
     * value was modified (different to the current one). If unavailable,
     * the last non-null one will be returned.
     * @apiNote Requires a proper implementation of {@link Object#equals(Object)}
     * @return A holder provided by {@link AbstractHolder#prev()}
     */
    default AbstractHolder<T> before() {
        var holder = prev();
        while (compare(holder) && !holder.equals(this))
            holder = holder.prev();
        return holder;
    }

    /**
     * Executes {@link AbstractHolder#before()} a given amount of times.
     * @param steps Modification count
     * @return A holder provided by {@link AbstractHolder#prev()}
     */
    default AbstractHolder<T> before(int steps) {
        var holder = prev();
        for (int i = 0; i < steps; i++)
            holder = holder.before();
        return holder;
    }

    /**
     * Performs a basic check to see if the previous holder is null;
     * returns false if null.
     * @return Whether the previous holder is not null.
     */
    default boolean hasPrevious() {
        return prev() != null && prev() != this;
    }

    /**
     * Checks if the holder has either a reference to a previous holder.
     * @return True, if empty.
     */
    default boolean isEmpty() {
        return !hasValue() && !hasPrevious();
    }

    /**
     * Directly tests the stored value against any other whilst ensuring
     * that neither of them are null.
     * @param value Testable value
     * @return If equal and not null
     */
    default boolean test(Object value) {
        var current = get();
        return current != null && current.equals(value);
    }

    /**
     * Uses {@link AbstractHolder#test(Object)} to compare the stored values
     * without having to explicitly {@link AbstractHolder#get()} them.
     * @param holder Another holder
     * @return If stored values are equal and not null
     */
    default boolean compare(AbstractHolder<T> holder) {
        return holder != null && test(holder.get());
    }

    @Override
    default Iterator<AbstractHolder<T>> iterator() {
        var main = this;
        return new Iterator<>() {
            private AbstractHolder<T> child = main;
            private boolean first = false;
            @Override
            public boolean hasNext() {
                var hasPrev = child.hasPrevious();
                return hasPrev || !first;
            }

            @Override
            public AbstractHolder<T> next() {
                if (first)
                    child = child.prev();
                else first = true;
                return child;
            }
        };
    }
}
