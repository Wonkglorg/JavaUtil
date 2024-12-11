package com.wonkglorg.util.valueholder.numbers;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ByteHolder extends NumberHolder<Byte> {
    public ByteHolder(Byte value) {
        super(value);
    }

    public ByteHolder(Supplier<Byte> consumer) {
        super(consumer);
    }

    @Override
    public ByteHolder set(Byte value){
        return new ByteHolder(value);
    }

    @Override
    public ByteHolder peak(Consumer<Byte> consumer) throws NullPointerException {
        consumer.accept(value);
        return this;
    }

    @Override
    public ByteHolder add(Byte addValue) throws NullPointerException {
        return new ByteHolder((byte) (value + addValue));
    }

    @Override
    public ByteHolder subtract(Byte subtractValue) throws NullPointerException {
        return new ByteHolder((byte) (value - subtractValue));
    }

    @Override
    public ByteHolder multiply(Byte multiplyValue) throws NullPointerException {
        return new ByteHolder((byte) (value * multiplyValue));
    }

    @Override
    public ByteHolder divide(Byte divideValue) throws NullPointerException {
        return new ByteHolder((byte) (value / divideValue));
    }

    @Override
    public boolean isGreaterThan(Byte other) throws NullPointerException {
        return value > other;
    }

    @Override
    public boolean isLessThan(Byte other) throws NullPointerException {
        return value < other;
    }

    @Override
    public boolean isEqualTo(Byte other) throws NullPointerException {
        return Objects.equals(value, other);
    }

    @Override
    public boolean isBetween(Byte lowerBound, Byte upperBound) throws NullPointerException {
        return value > lowerBound && value < upperBound;
    }

    @Override
    public NumberHolder<Byte> clamp(Byte min, Byte max) throws NullPointerException {
        return new ByteHolder(value < min ? min : value > max ? max : value);
    }

    @Override
    public NumberHolder<Byte> increment() throws NullPointerException {
        return new ByteHolder((byte) (value + 1));
    }

    @Override
    public NumberHolder<Byte> decrement() throws NullPointerException {
        return new ByteHolder((byte) (value - 1));
    }

    @Override
    public NumberHolder<Byte> max(Byte other) throws NullPointerException {
        return new ByteHolder(value > other ? value : other);
    }

    @Override
    public NumberHolder<Byte> min(Byte other) throws NullPointerException {
        return new ByteHolder(value < other ? value : other);
    }
}
