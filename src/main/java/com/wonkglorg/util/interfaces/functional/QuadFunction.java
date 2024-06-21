package com.wonkglorg.util.interfaces.functional;

@FunctionalInterface
public interface QuadFunction<T, U, V, W, E> {
    E apply(T var1, U var2, V var3, W var4);

    //todo:jmd implement
    /*
    default <E> QuadFunction<T, U, V, W, E> andThen(TriFunction<? super T, ? super U, ? super V, ? extends W> after) {
        Objects.requireNonNull(after);
        return (t, u, v, w) -> after.apply(apply(t, u, v, w), v);
    }

     */
}
