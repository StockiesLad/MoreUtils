package net.stockieslad.moreutils.event;

public final class EventListener<T> {
    public final int priority;
    public EventArgs<T> arg;

    public EventListener(int priority, EventArgs<T> arg) {
        this.priority = priority;
        this.arg = arg;
    }
}