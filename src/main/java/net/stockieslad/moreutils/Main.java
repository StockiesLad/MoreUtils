package net.stockieslad.moreutils;

import net.stockieslad.moreutils.event.AbstractEvent;
import net.stockieslad.moreutils.event.Event;

public class Main {
    private static final boolean IS_TESTING = false;

    public static void main(String[] args) {
        if (!IS_TESTING) return;

        testMillionRuns();
        testMillionArgs();
        //testMillionHolders();
        //testThreadSafety();
        //testEventAdding();

        while (true) {
        }
    }

    public static <T> AbstractEvent<T> createEvent() {
        return new Event<>();
    }

    public static void testMillionRuns() {
        AbstractEvent<Integer> event = createEvent();

        var time = System.currentTimeMillis();
        event.add((event1, listener, ctx, status) -> {});
        System.out.println(System.currentTimeMillis() - time + "ms to add an arg");

        time = System.currentTimeMillis();
        for (int i = 0; i <= 1_000_000; i++)
            event.execute(i);
        System.out.println(System.currentTimeMillis() - time + "ms to execute a million runs");
    }

    public static void testMillionArgs() {
        AbstractEvent<Boolean> event = createEvent();

        var time = System.currentTimeMillis();

        for (int i = 0; i <= 1_000_000; i++)
            event.add((event1, listener, ctx, status) -> {});
        System.out.println(System.currentTimeMillis() - time + "ms to add a million args");

        time = System.currentTimeMillis();
        event.execute(true);
        System.out.println(System.currentTimeMillis() - time + "ms to execute a million args");
    }
    public static void testThreadSafety() {
        AbstractEvent<Integer> event = createEvent();
        for (short i = 0; i < 1; i++) {
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
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        event.execute(-1);
    }

    public static void testEventAdding() {
        for (int i = 0; i < 100; i++) {
            AbstractEvent<Boolean> event = createEvent();
            for (int i1 = 0; i1 < 1_000_000; i1++) {
                event.add((event1, listener, ctx, status) -> {});
            }
            event.execute(true);
        }
    }
}