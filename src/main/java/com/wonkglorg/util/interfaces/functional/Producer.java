package com.wonkglorg.util.interfaces.functional;

@SuppressWarnings("unused")
@FunctionalInterface
public interface Producer<T> {
    T get();
}
