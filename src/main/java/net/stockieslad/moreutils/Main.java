package net.stockieslad.moreutils;

import net.stockieslad.moreutils.event.AbstractEvent;
import net.stockieslad.moreutils.event.Event;

public class Main {
    public static void main(String[] args) {
        testMillionRuns();
        testMillionArgs();
        testThreadSafety();
    }

    public static void testMillionRuns() {
        AbstractEvent<Boolean> event = createEvent();

        var time = System.currentTimeMillis();
        event.add((event1, listener, ctx, status) -> {});
        System.out.println(System.currentTimeMillis() - time + "ms to add an arg");

        time = System.currentTimeMillis();
        for (int i = 0; i <= 1_000_000; i++)
            event.execute(true);
        System.out.println(System.currentTimeMillis() - time + "ms to execute a million runs");
    }

    public static void testMillionArgs() {
        AbstractEvent<Boolean> event = createEvent();
        var time = System.currentTimeMillis();
        for (int i = 0; i <= 1_000_000; i++)
            event.add(i, (event1, listener, ctx, status) -> {});
        System.out.println(System.currentTimeMillis() - time + "ms to add a million args");

        time = System.currentTimeMillis();
        event.execute(true);
        System.out.println(System.currentTimeMillis() - time + "ms to execute a million args");
    }
    public static void testThreadSafety() {
        AbstractEvent<Integer> event = createEvent();
        for (int i = 0; i < 1; i++) {
            var x = i;
            event.add(x, (event1, listener, ctx, status) -> {
                System.out.println(x + " has been executed from thread " + ctx);
                status.set(true);
            });
        }
        for (int i = 0; i < 20; i++) {
            var x = i;
            new Thread(() -> {
                while (!event.isEmpty()) {
                    event.execute(x);
                }
            }).start();
        }
    }

    public static <T> AbstractEvent<T> createEvent() {
        return new Event<>();
    }
}