package com.wonkglorg.util.valueholder.numbers;

import java.util.Objects;
import java.util.function.Consumer;

public class ShortHolder extends NumberHolder<Short> {
    public ShortHolder(Short value) {
        super(value);
    }

    @Override
    public ShortHolder set(Short value) {
        return new ShortHolder(value);
    }

    @Override
    public ShortHolder peak(Consumer<Short> consumer) throws NullPointerException {
        consumer.accept(value);
        return this;
    }

    @Override
    public ShortHolder add(Short addValue) throws NullPointerException {
        return new ShortHolder((short) (value + addValue));
    }

    @Override
    public ShortHolder subtract(Short subtractValue) throws NullPointerException {
        return new ShortHolder((short) (value - subtractValue));
    }

    @Override
    public ShortHolder multiply(Short multiplyValue) throws NullPointerException {
        return new ShortHolder((short) (value * multiplyValue));
    }

    @Override
    public ShortHolder divide(Short divideValue) throws NullPointerException {
        return new ShortHolder((short) (value / divideValue));
    }

    @Override
    public boolean isGreaterThan(Short other) throws NullPointerException {
        return value > other;
    }

    @Override
    public boolean isLessThan(Short other) throws NullPointerException {
        return value < other;
    }

    @Override
    public boolean isEqualTo(Short other) throws NullPointerException {
        return Objects.equals(value, other);
    }

    @Override
    public boolean isBetween(Short lowerBound, Short upperBound) throws NullPointerException {
        return value > lowerBound && value < upperBound;
    }

    @Override
    public NumberHolder<Short> clamp(Short min, Short max) throws NullPointerException {
        return new ShortHolder(value < min ? min : value > max ? max : value);
    }

    @Override
    public NumberHolder<Short> increment() throws NullPointerException {
        return new ShortHolder((short) (value + 1));
    }

    @Override
    public NumberHolder<Short> decrement() throws NullPointerException {
        return new ShortHolder((short) (value - 1));
    }

    @Override
    public NumberHolder<Short> max(Short other) throws NullPointerException {
        return new ShortHolder(value > other ? value : other);
    }

    @Override
    public NumberHolder<Short> min(Short other) throws NullPointerException {
        return new ShortHolder(value < other ? value : other);
    }
}
