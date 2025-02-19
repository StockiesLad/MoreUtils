package net.stockieslad.moreutils.lambda;

public interface FuncIf {
    boolean getCondition();

    default FuncIf ifTrue(Runnable runnable) {
        if (getCondition())
            runnable.run();
        return this;
    }

    default void ifFalse(Runnable runnable) {
        if (!getCondition())
            runnable.run();
    }

    default FuncIf elseIf(boolean condition) {
        return () -> !getCondition() && condition;
    }
}
