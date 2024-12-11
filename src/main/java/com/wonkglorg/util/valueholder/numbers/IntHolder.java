package com.wonkglorg.util.valueholder.numbers;

import com.wonkglorg.util.valueholder.BaseHolder;

import java.util.Objects;
import java.util.function.Consumer;

public class IntHolder extends NumberHolder<Integer> {

    public IntHolder(Integer value) {
        super(value);
    }

    @Override
    public IntHolder set(Integer value) {
        return new IntHolder(value);
    }

    @Override
    public IntHolder peak(Consumer<Integer> consumer) throws NullPointerException  {
        consumer.accept(value);
        return this;
    }

    @Override
    public IntHolder add(Integer addValue) throws NullPointerException  {
        return new IntHolder(value + addValue);
    }

    @Override
    public IntHolder subtract(Integer subtractValue) throws NullPointerException  {
        return new IntHolder(value - subtractValue);
    }

    @Override
    public IntHolder multiply(Integer multiplyValue) throws NullPointerException  {
        return new IntHolder(value * multiplyValue);
    }

    @Override
    public IntHolder divide(Integer divideValue) throws NullPointerException  {
        return new IntHolder(value / divideValue);
    }

    @Override
    public boolean isGreaterThan(Integer other) throws NullPointerException  {
        if (other == null) {
            return true;
        }
        if (value == null) {
            return false;
        }
        return value > other;
    }

    @Override
    public boolean isLessThan(Integer other) throws NullPointerException  {
        if (other == null) {
            return false;
        }

        if (value == null) {
            return true;
        }

        return value < other;
    }

    @Override
    public boolean isEqualTo(Integer other) throws NullPointerException  {
        return Objects.equals(value, other);
    }

    @Override
    public boolean isBetween(Integer lowerBound, Integer upperBound) throws NullPointerException  {
        return value > lowerBound && value < upperBound;
    }

    @Override
    public IntHolder clamp(Integer min, Integer max) throws NullPointerException  {
        return new IntHolder(value < min ? min : value > max ? max : value);
    }

    @Override
    public IntHolder increment() throws NullPointerException  {
        return new IntHolder(value + 1);
    }

    @Override
    public IntHolder decrement() throws NullPointerException  {
        return new IntHolder(value - 1);
    }

    @Override
    public IntHolder max(Integer other) throws NullPointerException  {
        if (other == null) {
            return this;
        }
        return new IntHolder(value > other ? value : other);
    }

    @Override
    public IntHolder min(Integer other) throws NullPointerException  {
        if (other == null) {
            return this;
        }
        return new IntHolder(value < other ? value : other);
    }
}
