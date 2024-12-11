package com.wonkglorg.util.valueholder;

import java.util.Objects;
import java.util.function.Consumer;

public class CharHolder extends BaseHolder<Character> {
    public CharHolder(Character value) {
        super(value);
    }

    @Override
    public CharHolder set(Character value) {
        return new CharHolder(value);
    }

    /**
     *
     * Converts the character to upper case.
     * @throws NullPointerException if the character is null.
     */
    public CharHolder toUpperCase() throws NullPointerException {
        return new CharHolder(Character.toUpperCase(value));
    }

    /**
     *
     * Converts the character to lower case.
     * @throws NullPointerException if the character is null.
     */
    public CharHolder toLowerCase() throws NullPointerException {
        return new CharHolder(Character.toLowerCase(value));
    }

    @Override
    public CharHolder peak(Consumer<Character> consumer) throws NullPointerException {
        consumer.accept(value);
        return this;
    }
}
