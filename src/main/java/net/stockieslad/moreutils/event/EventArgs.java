package net.stockieslad.moreutils.event;

import net.stockieslad.moreutils.holder.Pointer;

import java.util.function.Consumer;

/**
 * A function specifically for {@link AbstractEvent}. It is stored
 * in {@link EventListener} with a priority.
 * @param <T> Context
 */
@FunctionalInterface
public interface EventArgs<T> {
    /**
     * Called from the event during execution.
     * @param event The event the arg was called from.
     * @param listener The listener the arg was stored in.
     * @param ctx The context from the point of execution.
     * @param status True -> Remove, False -> Cancel; Null -> Continue;
     */
    void proceed(
            AbstractEvent<T> event,
            EventListener<T> listener,
            T ctx,
            Pointer<Boolean> status
    );

    /**
     * The main args that provides all the necessary context.
     * @param eventArgs Args to execute
     * @return Upcasted Args
     * @param <T> Context
     */
    static <T> EventArgs<T> basic(EventArgs<T> eventArgs) {
        return eventArgs;
    }

    /**
     * A basic function that just takes in the context.
     * @param eventArgs Args to execute
     * @return Upcasted Args
     * @param <T> Context
     */
    static <T> EventArgs<T> simple(Consumer<T> eventArgs) {
        return (event, listener, ctx, status) -> eventArgs.accept(ctx);
    }

    /**
     * A basic function that takes in no parameters.
     * @param eventArgs Args to execute
     * @return Upcasted Args
     * @param <T> Context
     */
    static <T> EventArgs<T> empty(Runnable eventArgs) {
        return (event, listener, ctx, status) -> eventArgs.run();
    }
}
