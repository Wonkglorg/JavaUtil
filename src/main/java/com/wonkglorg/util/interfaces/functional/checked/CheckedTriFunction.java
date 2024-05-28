package com.wonkglorg.util.interfaces.functional.checked;

import com.wonkglorg.util.interfaces.functional.TriFunction;

public interface CheckedTriFunction<T, U, V, E> extends TriFunction<T, U, V, E> {
    E applyChecked(T t, U u, V v) throws Exception;

    @Override
    default E apply(T t, U u, V v) {
        try {
            return applyChecked(t, u, v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
