package com.wonkglorg.util.valueholder.numbers;

import java.util.Objects;
import java.util.function.Consumer;

public class FloatHolder extends NumberHolder<Float> {
    public FloatHolder(Float value) {
        super(value);
    }

    @Override
    public FloatHolder set(Float value) {
        return new FloatHolder(value);
    }

    @Override
    public FloatHolder peak(Consumer<Float> consumer) throws NullPointerException {
        consumer.accept(value);
        return this;
    }

    @Override
    public FloatHolder add(Float addValue) throws NullPointerException {
        return new FloatHolder(value + addValue);
    }

    @Override
    public FloatHolder subtract(Float subtractValue) throws NullPointerException {
        return new FloatHolder(value - subtractValue);
    }

    @Override
    public FloatHolder multiply(Float multiplyValue) throws NullPointerException {
        return new FloatHolder(value * multiplyValue);
    }

    @Override
    public FloatHolder divide(Float divideValue) throws NullPointerException {
        return new FloatHolder(value / divideValue);
    }

    @Override
    public boolean isGreaterThan(Float other) throws NullPointerException {
        return value > other;
    }

    @Override
    public boolean isLessThan(Float other) throws NullPointerException {
        return value < other;
    }

    @Override
    public boolean isEqualTo(Float other) throws NullPointerException {
        return Objects.equals(value, other);
    }

    @Override
    public boolean isBetween(Float lowerBound, Float upperBound) throws NullPointerException {
        return value > lowerBound && value < upperBound;
    }

    @Override
    public FloatHolder clamp(Float min, Float max) throws NullPointerException {
        return new FloatHolder(value < min ? min : value > max ? max : value);
    }

    @Override
    public FloatHolder increment() throws NullPointerException {
        return new FloatHolder(value + 1);
    }

    @Override
    public FloatHolder decrement() throws NullPointerException {
        return new FloatHolder(value - 1);
    }

    @Override
    public FloatHolder max(Float other) throws NullPointerException {
        return new FloatHolder(value > other ? value : other);
    }

    @Override
    public FloatHolder min(Float other) throws NullPointerException {
        return new FloatHolder(value < other ? value : other);
    }
}
