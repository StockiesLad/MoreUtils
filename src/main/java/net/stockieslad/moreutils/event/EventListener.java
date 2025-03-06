package net.stockieslad.moreutils.event;

import net.stockieslad.moreutils.data.Buffer;

public final class EventListener<T> {
    public static final Buffer<EventListener<?>> BUFFER = new Buffer<>(EventListener::new);
    public EventArgs<T> arg;
    public int priority;

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

    @Override
    public int hashCode() {
        return this.priority;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EventListener<?> listener &&
                this.priority == listener.priority &&
                this.arg == listener.arg;
    }

    @Override
    public String toString() {
        return "EventListener[priority=" + priority +
                ", arg=" + arg + "]";
    }
}