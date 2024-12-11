package com.wonkglorg.util.valueholder;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class BaseHolder<T> {
    protected final T value;

    public BaseHolder(T value) {
        this.value = value;
    }

    public BaseHolder(Supplier<T> supplier) {
        this.value = supplier.get();
    }


    /**
     * Set the value of the holder
     *
     * @param value the value to set
     * @return the holder with the new value
     */
    public abstract BaseHolder<T> set(T value);

    /**
     * Gets the current value of the holder
     *
     * @param consumer the consumer to pass the value to
     * @return the holder
     * @throws NullPointerException if the Consumer is null
     */
    public abstract BaseHolder<T> peak(Consumer<T> consumer) throws NullPointerException;

    /**
     * Map the current value to a new value
     *
     * @param func the function to apply to the value
     * @param <R>  the type of the new value
     * @return the new holder with the new value
     * @throws NullPointerException if the function is null
     */
    public <R> ObjectHolder<R> map(Function<T, R> func) throws NullPointerException {
        return new ObjectHolder<>(func.apply(value));
    }

    /**
     * @return true if the value is present
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Evaluate the predicate
     */
    public boolean is(Predicate<T> predicate) {
        return predicate.test(value);
    }

    /**
     * Evaluate the negative predicate
     */
    public boolean isNot(Predicate<T> predicate) {
        return !predicate.test(value);
    }

    /**
     * Check if the value is equal to the given value
     */
    public boolean isEqualTo(T other) {
        return Objects.equals(value, other);
    }

    /**
     * Check if the value is not equal to the given value
     */
    public boolean isNotEqualTo(T other) {
        return !Objects.equals(value, other);
    }

    /**
     * Check if the value is null
     */
    public boolean isNull() {
        return value == null;
    }

    /**
     * Check if the value is not null
     */
    public boolean isNotNull() {
        return value != null;
    }

    /**
     * Get the value
     */
    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }
}
