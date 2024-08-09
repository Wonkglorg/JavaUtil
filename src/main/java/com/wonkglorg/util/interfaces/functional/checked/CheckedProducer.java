package com.wonkglorg.util.interfaces.functional.checked;

import com.wonkglorg.util.interfaces.functional.BiSupplier;

@SuppressWarnings("unused")
@FunctionalInterface
public interface CheckedProducer<T> extends BiSupplier<T> {
    T getChecked() throws Exception;

    @Override
    default T get() {
        try {
            return getChecked();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
