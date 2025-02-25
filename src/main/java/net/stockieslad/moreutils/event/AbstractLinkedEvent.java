package net.stockieslad.moreutils.event;

import net.stockieslad.moreutils.holder.AbstractHolder;

public interface AbstractLinkedEvent<T> extends AbstractEvent<T>{

    AbstractHolder<EventListener<T>> insert(
            AbstractHolder<EventListener<T>> prev, AbstractHolder<EventListener<T>> next);
    AbstractHolder<EventListener<T>> displace(
            AbstractHolder<EventListener<T>> prev, AbstractHolder<EventListener<T>> next);

}
