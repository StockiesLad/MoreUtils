package net.stockieslad.moreutils.event;

import net.stockieslad.moreutils.data.Buffer;

public final class EventListener<T> {
    public static final Buffer<EventListener<?>> BUFFER = new Buffer<>(EventListener::new);
    public int priority;
    public EventArgs<T> arg;

    public EventListener(int priority, EventArgs<T> arg) {
        this.priority = priority;
        this.arg = arg;
    }

    public EventListener() {
        this.priority = 0;
        this.arg = null;
    }

    EventListener<T> setAll(int priority, EventArgs<T> args) {
        this.priority = priority;
        this.arg = args;
        return this;
    }
}