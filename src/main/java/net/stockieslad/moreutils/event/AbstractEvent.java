package net.stockieslad.moreutils.event;

import net.stockieslad.moreutils.holder.Pointer;

/**
 * A record-based event template which stores and organises the
 * {@link EventArgs} according to {@link Comparable}.
 * @param <T> The context for usage in {@link EventArgs#proceed(AbstractEvent, EventListener, T, Pointer)}
 */
public interface AbstractEvent<T> {
    void execute(T context);

    void add(EventListener<T> listener);

    default void add(int priority, EventArgs<T> args) {
        add(new EventListener<>(priority, args));
    }

    default void add(EventArgs<T> args) {
        add(0, args);
    }

    void replace(EventListener<T> listener, EventArgs<T> args);

    void remove(EventListener<T> listener);

    default void remove(int priority, EventArgs<T> args) {
        remove(new EventListener<>(priority, args));
    }

    boolean isEmpty();
}
