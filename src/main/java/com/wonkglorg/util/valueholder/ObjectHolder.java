package com.wonkglorg.util.valueholder;


import com.wonkglorg.util.valueholder.numbers.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ObjectHolder<T> extends BaseHolder<T> {


    public ObjectHolder(T value) {
        super(value);
    }

    @Override
    public ObjectHolder<T> set(T value) {
        return new ObjectHolder<>(value);
    }

    public static <T> ObjectHolder<T> of(T value) {
        return new ObjectHolder<>(value);
    }

    public static <T> ObjectHolder<T> of(Supplier<T> value) {
        return new ObjectHolder<>(value.get());
    }

    @Override
    public ObjectHolder<T> peak(Consumer<T> consumer) {
        return null;
    }

    //--mappers--
    public StringHolder toStringValue(Function<T, String> func) {
        return new StringHolder(func.apply(value));
    }

    public IntHolder toIntValue(Function<T, Integer> func) {
        return new IntHolder(func.apply(value));
    }

    public ByteHolder toByteValue(Function<T, Byte> func) {
        return new ByteHolder(func.apply(value));
    }

    public ShortHolder toShortValue(Function<T, Short> func) {
        return new ShortHolder(func.apply(value));
    }

    public FloatHolder toFloatValue(Function<T, Float> func) {
        return new FloatHolder(func.apply(value));
    }

    public DoubleHolder toDoubleValue(Function<T, Double> func) {
        return new DoubleHolder(func.apply(value));
    }

    public LongHolder toLongValue(Function<T, Long> func) {
        return new LongHolder(func.apply(value));
    }

    public CharHolder toCharValue(Function<T, Character> func) {
        return new CharHolder(func.apply(value));
    }

}
