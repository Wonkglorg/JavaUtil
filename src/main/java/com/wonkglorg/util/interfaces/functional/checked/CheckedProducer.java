package com.wonkglorg.util.interfaces.functional.checked;

import com.wonkglorg.util.interfaces.functional.Producer;

@SuppressWarnings("unused")
@FunctionalInterface
public interface CheckedProducer<T> extends Producer<T> {
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
