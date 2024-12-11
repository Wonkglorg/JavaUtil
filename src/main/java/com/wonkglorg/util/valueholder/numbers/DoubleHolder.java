package com.wonkglorg.util.valueholder.numbers;

import java.util.function.Consumer;

public class DoubleHolder extends NumberHolder<Double> {
    public DoubleHolder(Double value) {
        super(value);
    }

    @Override
    public DoubleHolder set(Double value) {
        return new DoubleHolder(value);
    }

    @Override
    public DoubleHolder peak(Consumer<Double> consumer) throws NullPointerException  {
        consumer.accept(value);
        return this;
    }

    @Override
    public DoubleHolder add(Double addValue) throws NullPointerException  {
        return new DoubleHolder(value + addValue);
    }

    @Override
    public DoubleHolder subtract(Double subtractValue) throws NullPointerException  {
        return new DoubleHolder(value - subtractValue);
    }

    @Override
    public DoubleHolder multiply(Double multiplyValue) throws NullPointerException  {
        return new DoubleHolder(value * multiplyValue);
    }

    @Override
    public DoubleHolder divide(Double divideValue) throws NullPointerException  {
        return new DoubleHolder(value / divideValue);
    }

    @Override
    public boolean isGreaterThan(Double other) throws NullPointerException  {
        return value > other;
    }

    @Override
    public boolean isLessThan(Double other) throws NullPointerException  {
        return value < other;
    }

    @Override
    public boolean isBetween(Double lowerBound, Double upperBound) throws NullPointerException  {
        return value > lowerBound && value < upperBound;
    }

    @Override
    public DoubleHolder clamp(Double min, Double max) throws NullPointerException  {
        return new DoubleHolder(value < min ? min : value > max ? max : value);
    }

    @Override
    public DoubleHolder increment() throws NullPointerException  {
        return new DoubleHolder(value + 1);
    }

    @Override
    public DoubleHolder decrement() throws NullPointerException  {
        return new DoubleHolder(value - 1);
    }

    @Override
    public DoubleHolder max(Double other) throws NullPointerException  {
        return new DoubleHolder(value > other ? value : other);
    }

    @Override
    public DoubleHolder min(Double other) throws NullPointerException  {
        return new DoubleHolder(value < other ? value : other);
    }


}
