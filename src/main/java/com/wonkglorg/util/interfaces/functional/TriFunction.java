package com.wonkglorg.util.interfaces.functional;

import java.util.Objects;
import java.util.function.BiFunction;

public interface TriFunction<T, U, V, E> {
    E apply(T t, U u, V v);

    default <W> TriFunction<T, U, V, W> andThen(BiFunction<? super E, ? super V, ? extends W> after) {
        Objects.requireNonNull(after);
        return (t, u, v) -> after.apply(apply(t, u, v), v);
    }
}
