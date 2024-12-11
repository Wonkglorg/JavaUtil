package com.wonkglorg.util.valueholder;


import com.wonkglorg.util.valueholder.numbers.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ObjectHolder<T> extends BaseHolder<T> {


    public ObjectHolder(T value) {
        super(value);
    }

    public ObjectHolder(Supplier<T> supplier) {
        super(supplier);
    }

    @Override
    public ObjectHolder<T> set(T value) {
        return new ObjectHolder<>(value);
    }

    @Override
    public ObjectHolder<T> peak(Consumer<T> consumer) {
        consumer.accept(value);
        return this;
    }

    //--mappers--

    /**
     * Converts the value to a String.
     *
     * @param func the function to convert the value to a String.
     * @return a StringHolder with the converted value.
     * @throws NullPointerException if the func is null.
     */
    public StringHolder toStringValue(Function<T, String> func) throws NullPointerException {
        return new StringHolder(func.apply(value));
    }

    /**
     * Converts the value to an Integer.
     *
     * @param func the function to convert the value to an Integer.
     * @return an IntHolder with the converted value.
     * @throws NullPointerException if the func is null.
     */
    public IntHolder toIntValue(Function<T, Integer> func) throws NullPointerException {
        return new IntHolder(func.apply(value));
    }

    /**
     * Converts the value to a Byte.
     *
     * @param func the function to convert the value to a Byte.
     * @return a ByteHolder with the converted value.
     * @throws NullPointerException if the func is null.
     */
    public ByteHolder toByteValue(Function<T, Byte> func) throws NullPointerException {
        return new ByteHolder(func.apply(value));
    }

    /**
     * Converts the value to a Short.
     *
     * @param func the function to convert the value to a Short.
     * @return a ShortHolder with the converted value.
     * @throws NullPointerException if the func is null.
     */
    public ShortHolder toShortValue(Function<T, Short> func) throws NullPointerException {
        return new ShortHolder(func.apply(value));
    }

    /**
     * Converts the value to a Float.
     *
     * @param func the function to convert the value to a Float.
     * @return a FloatHolder with the converted value.
     * @throws NullPointerException if the func is null.
     */
    public FloatHolder toFloatValue(Function<T, Float> func) throws NullPointerException {
        return new FloatHolder(func.apply(value));
    }

    /**
     * Converts the value to a Double.
     *
     * @param func the function to convert the value to a Double.
     * @return a DoubleHolder with the converted value.
     * @throws NullPointerException if the func is null.
     */
    public DoubleHolder toDoubleValue(Function<T, Double> func) throws NullPointerException {
        return new DoubleHolder(func.apply(value));
    }

    /**
     * Converts the value to a Long.
     *
     * @param func the function to convert the value to a Long.
     * @return a LongHolder with the converted value.
     * @throws NullPointerException if the func is null.
     */
    public LongHolder toLongValue(Function<T, Long> func) throws NullPointerException {
        return new LongHolder(func.apply(value));
    }

    /**
     * Converts the value to a Character.
     *
     * @param func the function to convert the value to a Character.
     * @return a CharHolder with the converted value.
     * @throws NullPointerException if the func is null.
     */
    public CharHolder toCharValue(Function<T, Character> func) throws NullPointerException {
        return new CharHolder(func.apply(value));
    }

}
