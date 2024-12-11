package com.wonkglorg.util.valueholder;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class StringHolder extends BaseHolder<String> {
    public StringHolder(String value) {
        super(value);
    }

    public StringHolder(Supplier<String> supplier) {
        super(supplier);
    }

    @Override
    public StringHolder set(String value) {
        return new StringHolder(value);
    }

    /**
     * Converts the string to upper case.
     * @throws NullPointerException if the value is null.
     */
    public StringHolder toUpperCase() throws NullPointerException {
        return new StringHolder(value.toUpperCase());
    }

    /**
     * Converts the string to lower case.
     * @throws NullPointerException if the value is null.
     */
    public StringHolder toLowerCase() throws NullPointerException  {
        return new StringHolder(value.toLowerCase());
    }

    /**
     * Trims the string.
     * @return the trimmed string
     */
    public StringHolder trim() throws NullPointerException  {
        return new StringHolder(value.trim());
    }

    /**
     * Checks if the string contains the given value.
     *
     * @param regex the regex to check for
     * @return true if the string contains the given value
     */
    public boolean matchesExact(String regex) throws NullPointerException  {
        return value.matches(regex);
    }

    /**
     * Checks if the string contains the given value.
     *
     * @param pattern the pattern to check for
     * @return a matcher for the given pattern
     */
    public Matcher matcher(Pattern pattern) throws NullPointerException  {
        return pattern.matcher(value);
    }

    /**
     * Checks if the string contains the given value.
     *
     * @param regex the regex to check for
     * @return a matcher for the given regex
     */
    public Matcher matcher(String regex) throws NullPointerException  {
        return Pattern.compile(regex).matcher(value);
    }


    /**
     * Converts the string to a character stream.
     *
     * @return A stream of characters.
     */
    public Stream<Character> toCharacterStream() throws NullPointerException  {
        return value.chars().mapToObj(c -> (char) c);
    }

    /**
     * Converts the string to a word stream.
     *
     * @return the given string as a stream of words
     */
    public Stream<String> toWordStream() throws NullPointerException  {
        return Stream.of(value.split("\\s+"));
    }

    /**
     * Converts the string to a line stream.
     *
     * @return the given string as a stream of lines
     */
    public Stream<String> toLineStream() throws NullPointerException  {
        return Stream.of(value.split("\\n"));
    }

    /**
     * Splits the string by the given regex.
     *
     * @param regex the regex to split by
     * @return the given string as a stream of strings
     */
    public Stream<String> splitBy(String regex) throws NullPointerException  {
        return Stream.of(value.split(regex));
    }

    @Override
    public StringHolder peak(Consumer<String> consumer) {
        consumer.accept(value);
        return this;
    }
}
