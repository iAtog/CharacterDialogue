package me.iatog.characterdialogue.util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class SingleUseConsumer<T> implements Consumer<T> {
    private final Consumer<T> consumer;
    private final AtomicBoolean executed;

    public SingleUseConsumer(Consumer<T> consumer) {
        this.consumer = consumer;
        this.executed = new AtomicBoolean(false);
    }

    @Override
    public void accept(T t) {
        if (!executed.get()) {
            consumer.accept(t);
            executed.set(true);
        } else {
            throw new IllegalStateException("This consumer was already executed.");
        }
    }

    public static <T> SingleUseConsumer<T> create(Consumer<T> consumer) {
        return new SingleUseConsumer<>(consumer);
    }
}
