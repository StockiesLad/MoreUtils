package net.stockieslad.moreutils.holder.series;

import net.stockieslad.moreutils.holder.AbstractHolder;

import java.util.*;

public class SeriesPool<T> {
    private static final int BASE_CAPACITY = 256;
    private final List<Series<T>> pool;
    private int place = 0;

    public SeriesPool() {
        this.pool = new ArrayList<>(BASE_CAPACITY);
        fill();
    }

    public Series<T> acquire(T value, AbstractHolder<T> prev) {
        if (place == 0) fill();
        return pool.get(--place).setAll(value, prev);
    }

    public void fill() {
        fill(BASE_CAPACITY);
    }

    public void fill(int amount) {
        place = amount + pool.size();
        for (int i = 0; i < amount; i++) {
            pool.add(new Series<>());
        }
    }
}