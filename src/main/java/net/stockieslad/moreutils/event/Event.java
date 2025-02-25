package net.stockieslad.moreutils.event;

import net.stockieslad.moreutils.holder.AbstractHolder;
import net.stockieslad.moreutils.holder.Pointer;
import net.stockieslad.moreutils.holder.Series;

/**
 * TODO: Optimise {@link Event#add(EventListener)}.
 * TODO: Add a body pointer and a size to optimise large additions
 * @param <T> The context for usage in {@link EventArgs#proceed(AbstractEvent, EventListener, T, Pointer)}
 */
public class Event<T> implements AbstractLinkedEvent<T> {
    private final AbstractHolder<EventListener<T>> tail = new Series<>();
    private AbstractHolder<EventListener<T>> head = tail;
    private final Pointer<Boolean> status = new Pointer<>();

    @Override
    public void execute(T context) {
        AbstractHolder<EventListener<T>> next = null;
        for (var series = head; series.hasPrevious(); series = series.prev()) {
            var listener = series.get();
            listener.arg.proceed(this, listener, context, status);
            if (status.get() != null)
                if (status.get()) displace(series, next);
                else break;
            next = series;
        }
    }

    @Override
    public void add(EventListener<T> listener) {
        var fail = true;
        for (var series = head; series.hasPrevious(); series = series.prev()) {
            if (series.get().priority - listener.priority > 0) continue;
            series.set(listener);
            fail = false;
            break;
        }
        if (fail) tail.set(listener);
    }

    @Override
    public AbstractHolder<EventListener<T>> insert(
            AbstractHolder<EventListener<T>> prev,
            AbstractHolder<EventListener<T>> next
    ) {
        prev.setPrev(next.prev());
        next.setPrev(prev);
        return prev;
    }

    @Override
    public void replace(EventListener<T> listener, EventArgs<T> args) {
        listener.arg = args;
    }

    @Override
    public void remove(EventListener<T> listener) {
        if (head.get() == listener)
            head = head.prev();
        else {
            var next = head;
            for (var series = head; series.hasPrevious(); series = series.prev()) {
                if (series.get() == listener) {
                    next.setPrev(series.prev());
                    break;
                } else next = series;
            }
        }
    }

    @Override
    public AbstractHolder<EventListener<T>> displace(
            AbstractHolder<EventListener<T>> prev,
            AbstractHolder<EventListener<T>> next
    ) {
        if (next == null)
            head = head.prev();
        else next.setPrev(prev.prev());
        return prev;
    }

    @Override
    public boolean isEmpty() {
        return head.isEmpty();
    }
}
