package com.wonkglorg.util.valueholder.numbers;


import com.wonkglorg.util.valueholder.BaseHolder;
import com.wonkglorg.util.valueholder.CharHolder;
import com.wonkglorg.util.valueholder.StringHolder;

import java.util.function.Supplier;

public abstract class NumberHolder<T extends Number> extends BaseHolder<T> {
    protected NumberHolder(T value) {
        super(value);
    }

    protected NumberHolder(Supplier<T> consumer) {
        super(consumer);
    }

    /**
     * Add the given value to the current value
     *
     * @param addValue the value to add
     * @return the new value
     * @throws NullPointerException if the given value is null or the current value is null
     */
    public abstract NumberHolder<T> add(T addValue) throws NullPointerException;

    /**
     * Subtract the given value from the current value
     *
     * @param subtractValue the value to subtract
     * @return the new value
     * @throws NullPointerException if the given value is null or the current value is null
     */

    public abstract NumberHolder<T> subtract(T subtractValue) throws NullPointerException;

    /**
     * Multiply the current value by the given value
     *
     * @param multiplyValue the value to multiply by
     * @return the new value
     * @throws NullPointerException if the given value is null or the current value is null
     */

    public abstract NumberHolder<T> multiply(T multiplyValue) throws NullPointerException;

    /**
     * Divide the current value by the given value
     *
     * @param divideValue the value to divide by
     * @return the new value
     * @throws NullPointerException if the given value is null or the current value is null
     */

    public abstract NumberHolder<T> divide(T divideValue) throws NullPointerException;

    /**
     * Check if the current value is greater than the given value
     *
     * @param other the value to compare to
     * @return true if the current value is greater than the given value
     * @throws NullPointerException if the given value is null or the current value is null
     */

    public abstract boolean isGreaterThan(T other) throws NullPointerException;

    /**
     * Check if the current value is less than the given value
     *
     * @param other the value to compare to
     * @return true if the current value is less than the given value
     * @throws NullPointerException if the given value is null or the current value is null
     */
    public abstract boolean isLessThan(T other) throws NullPointerException;

    /**
     * Check if the current value is between the given lower and upper bounds
     *
     * @param lowerBound the lower bound
     * @param upperBound the upper bound
     * @return true if the current value is between(exclusive)
     * @throws NullPointerException if the given value is null or the current value is null
     */
    public abstract boolean isBetween(T lowerBound, T upperBound) throws NullPointerException;

    /**
     * Clamp the current value between the given min and max values
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return the new value
     * @throws NullPointerException if the given value is null or the current value is null
     */
    public abstract NumberHolder<T> clamp(T min, T max) throws NullPointerException;

    /**
     * Increment the current value by 1
     *
     * @return the new value
     * @throws NullPointerException if the current value is null
     */
    public abstract NumberHolder<T> increment() throws NullPointerException;

    /**
     * Decrement the current value by 1
     *
     * @return the new value
     * @throws NullPointerException if the current value is null
     */
    public abstract NumberHolder<T> decrement() throws NullPointerException;

    /**
     * Return the maximum of the current value and the given value
     *
     * @param other the value to compare to
     * @return the maximum value
     * @throws NullPointerException if the given value is null or the current value is null
     */
    public abstract NumberHolder<T> max(T other) throws NullPointerException;

    /**
     * Return the minimum of the current value and the given value
     *
     * @param other the value to compare to
     * @return the minimum value
     * @throws NullPointerException if the given value is null or the current value is null
     */
    public abstract NumberHolder<T> min(T other) throws NullPointerException;


    /**
     * Convert the current value to a String
     *
     * @return {@link StringHolder}
     * @throws NullPointerException if the current value is null
     */
    public StringHolder toStringValue() throws NullPointerException {
        return new StringHolder(value.toString());
    }

    /**
     * Convert the current value to an Integer (losing precision)
     *
     * @return {@link IntHolder}
     * @throws NullPointerException if the current value is null
     */
    public IntHolder toIntValue() throws NullPointerException {
        return new IntHolder(value.intValue());
    }

    /**
     * Convert the current value to a Byte (losing precision)
     *
     * @return {@link ByteHolder}
     * @throws NullPointerException if the current value is null
     */
    public ByteHolder toByteValue() throws NullPointerException {
        return new ByteHolder(value.byteValue());
    }

    /**
     * Convert the current value to a Short (losing precision)
     *
     * @return {@link ShortHolder}
     * @throws NullPointerException if the current value is null
     */
    public ShortHolder toShortValue() throws NullPointerException {
        return new ShortHolder(value.shortValue());
    }

    /**
     * Convert the current value to a Float (losing precision)
     *
     * @return {@link FloatHolder}
     * @throws NullPointerException if the current value is null
     */
    public FloatHolder toFloatValue() throws NullPointerException {
        return new FloatHolder(value.floatValue());
    }

    /**
     * Convert the current value to a Double
     *
     * @return {@link DoubleHolder}
     * @throws NullPointerException if the current value is null
     */
    public DoubleHolder toDoubleValue() throws NullPointerException {
        return new DoubleHolder(value.doubleValue());
    }

    /**
     * Convert the current value to a Long (losing precision)
     *
     * @return {@link LongHolder}
     * @throws NullPointerException if the current value is null
     */
    public LongHolder toLongValue() throws NullPointerException {
        return new LongHolder(value.longValue());
    }

    /**
     * Convert the current value to a Character (losing precision)
     *
     * @return {@link CharHolder}
     * @throws NullPointerException if the current value is null
     */
    public CharHolder toCharValue() throws NullPointerException {
        return new CharHolder((char) value.intValue());
    }
}
