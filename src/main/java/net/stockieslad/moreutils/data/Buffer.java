package net.stockieslad.moreutils.data;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Buffer<T> {
    private static final int SIZE = 65536;
    private final T[] pool;
    private final Supplier<T> supplier;
    private int place;

    public Buffer(Supplier<T> supplier) {
        this.supplier = supplier;
        this.pool = (T[]) new Object[SIZE];
        this.place = pool.length;
        for (int i = 0; i < pool.length; i++) {
            pool[i] = supplier.get();
        }
    }

    public T get() {
        if (place == 0) {
            place = pool.length;
            for (int i = 0; i < pool.length; i++) {
                pool[i] = supplier.get();
            }
        }
        return pool[--place];
    }
}
