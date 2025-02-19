package net.stockieslad.moreutils.holder;

import java.util.function.Consumer;

/**
 * Maps a consumer to work with a holder. This is used for
 * inter-compatibility with existing systems.
 * <P>
 *     All implementations must not be immutable as the next
 *     instance will not be accessible solely using a consumer.
 * </P>
 * @param <T> Type of the stored value
 */
public interface ConsumingHolder<T> extends AbstractHolder<T>, Consumer<T> {
    /**
     * Plain call to {@link AbstractHolder#set(T)}, ignoring
     * its return value.
     * @param value the next value
     */
    @Override
    default void accept(T value) {
        set(value);
    }
}
