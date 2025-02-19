package net.stockieslad.moreutils.event;

import net.stockieslad.moreutils.holder.AbstractHolder;
import net.stockieslad.moreutils.holder.Pointer;
import net.stockieslad.moreutils.holder.Series;

/**
 *
 * @param <T> The context for usage in {@link EventArgs#proceed(AbstractEvent, EventListener, T, Pointer)}
 */
public class Event<T> implements AbstractEvent<T> {
    private AbstractHolder<EventListener<T>> head = new Series<>();
    private final Pointer<Boolean> status = new Pointer<>();

    @Override
    public void execute(T context) {
        for (AbstractHolder<EventListener<T>> seriesRef = head; seriesRef.hasPrevious(); seriesRef = seriesRef.prev()) {
            var listener = seriesRef.get();
            listener.arg.proceed(this, listener, context, status);
            if (status.get() != null)
               if (status.get()) remove(listener);
            else break;
        }
    }

    @Override
    public void add(EventListener<T> listener) {
        var priority = listener.priority;
        for (AbstractHolder<EventListener<T>> seriesRef = head; true; seriesRef = seriesRef.prev()) {
            if (seriesRef.hasPrevious()) {
                if (seriesRef.get().priority - priority <= 0) {
                    seriesRef.set(listener);
                    break;
                }
            } else {
                seriesRef.set(listener);
                break;
            }
        }
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
            for (AbstractHolder<EventListener<T>> seriesRef = head; seriesRef.hasPrevious(); seriesRef = seriesRef.prev()) {
                if (seriesRef.get() == listener) {
                    next.setPrev(seriesRef.prev());
                    break;
                } else next = seriesRef;
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return head.isEmpty();
    }
}
