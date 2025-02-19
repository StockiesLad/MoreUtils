package net.stockieslad.moreutils.lambda;

import java.util.function.Consumer;

public interface Functional {
    static <T> void inlinedVar(T value, Consumer<T> with) {
        with.accept(value);
    }
}
