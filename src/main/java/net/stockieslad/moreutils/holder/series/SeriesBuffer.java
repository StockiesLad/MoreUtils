package net.stockieslad.moreutils.holder.series;

import net.stockieslad.moreutils.holder.AbstractHolder;

@SuppressWarnings("unchecked")
class SeriesBuffer {
    //TODO: DONT FORGET TO REDUCE THE BUFFER SIZE AFTER TESTING
    private static final int SIZE = (int) (1024 * Math.pow(2, 12));
    private static final Series<?>[] POOL = new Series[SIZE];
    private static int PLACE;

    static {
        PLACE = POOL.length;
        for (int i = 0; i < POOL.length; i++) {
            POOL[i] = new Series<>();
        }
    }

    static <T> Series<T> get(T value, AbstractHolder<T> prev) {
        if (PLACE == 0) {
            PLACE = POOL.length;
            for (int i = 0; i < POOL.length; i++) {
                POOL[i] = new Series<>();
            }
        }
        return ((Series<T>) POOL[--PLACE]).setAll(value, prev);
    }

    static void init() {}
}