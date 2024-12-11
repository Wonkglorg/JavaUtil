package com.wonkglorg.util.valueholder.numbers;

import java.util.Objects;
import java.util.function.Consumer;

public class LongHolder extends NumberHolder<Long> {
    public LongHolder(Long value) {
        super(value);
    }

    @Override
    public LongHolder set(Long value) {
        return new LongHolder(value);
    }

    @Override
    public LongHolder peak(Consumer<Long> consumer) throws NullPointerException  {
        consumer.accept(value);
        return this;
    }

    @Override
    public LongHolder add(Long addValue) throws NullPointerException  {
        return new LongHolder(value + addValue);
    }

    @Override
    public LongHolder subtract(Long subtractValue) throws NullPointerException  {
        return new LongHolder(value - subtractValue);
    }

    @Override
    public LongHolder multiply(Long multiplyValue) throws NullPointerException  {
        return new LongHolder(value * multiplyValue);
    }

    @Override
    public LongHolder divide(Long divideValue) throws NullPointerException  {
        return new LongHolder(value / divideValue);
    }

    @Override
    public boolean isGreaterThan(Long other) throws NullPointerException  {
        if (other == null) {
            return true;
        }

        if (value == null) {
            return false;
        }

        return value > other;
    }

    @Override
    public boolean isLessThan(Long other) throws NullPointerException  {
        if (other == null) {
            return false;
        }
        if (value == null) {
            return true;
        }
        return value < other;
    }

    @Override
    public boolean isEqualTo(Long other) throws NullPointerException  {
        return Objects.equals(value, other);
    }

    @Override
    public boolean isBetween(Long lowerBound, Long upperBound) throws NullPointerException  {
        return value > lowerBound && value < upperBound;
    }

    @Override
    public LongHolder clamp(Long min, Long max) throws NullPointerException  {
        return new LongHolder(value < min ? min : value > max ? max : value);
    }

    @Override
    public LongHolder increment() throws NullPointerException  {
        return new LongHolder(value + 1);
    }

    @Override
    public LongHolder decrement() throws NullPointerException  {
        return new LongHolder(value - 1);
    }

    @Override
    public LongHolder max(Long other) throws NullPointerException  {
        return new LongHolder(value > other ? value : other);
    }

    @Override
    public LongHolder min(Long other) throws NullPointerException  {
        return new LongHolder(value < other ? value : other);
    }
}
