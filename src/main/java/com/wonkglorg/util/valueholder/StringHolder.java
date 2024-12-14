package com.wonkglorg.util.valueholder;

import com.wonkglorg.util.valueholder.numbers.ByteHolder;
import com.wonkglorg.util.valueholder.numbers.DoubleHolder;
import com.wonkglorg.util.valueholder.numbers.FloatHolder;
import com.wonkglorg.util.valueholder.numbers.IntHolder;
import com.wonkglorg.util.valueholder.numbers.LongHolder;
import com.wonkglorg.util.valueholder.numbers.ShortHolder;

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
	 *
	 * @throws NullPointerException if the value is null.
	 */
	public StringHolder toUpperCase() throws NullPointerException {
		return new StringHolder(value.toUpperCase());
	}

	/**
	 * Converts the string to lower case.
	 *
	 * @throws NullPointerException if the value is null.
	 */
	public StringHolder toLowerCase() throws NullPointerException {
		return new StringHolder(value.toLowerCase());
	}

	/**
	 * Trims the string.
	 *
	 * @return the trimmed string
	 */
	public StringHolder trim() throws NullPointerException {
		return new StringHolder(value.trim());
	}

	/**
	 * Checks if the string contains the given value.
	 *
	 * @param regex the regex to check for
	 * @return true if the string contains the given value
	 */
	public boolean matchesExact(String regex) throws NullPointerException {
		return value.matches(regex);
	}

	/**
	 * Checks if the string contains the given value.
	 *
	 * @param pattern the pattern to check for
	 * @return a matcher for the given pattern
	 */
	public Matcher matcher(Pattern pattern) throws NullPointerException {
		return pattern.matcher(value);
	}

	/**
	 * Checks if the string contains the given value.
	 *
	 * @param regex the regex to check for
	 * @return a matcher for the given regex
	 */
	public Matcher matcher(String regex) throws NullPointerException {
		return Pattern.compile(regex).matcher(value);
	}


	/**
	 * Converts the string to a character stream.
	 *
	 * @return A stream of characters.
	 */
	public Stream<Character> toCharacterStream() throws NullPointerException {
		return value.chars().mapToObj(c -> (char) c);
	}

	/**
	 * Converts the string to a word stream.
	 *
	 * @return the given string as a stream of words
	 */
	public Stream<String> toWordStream() throws NullPointerException {
		return Stream.of(value.split("\\s+"));
	}

	/**
	 * Converts the string to a line stream.
	 *
	 * @return the given string as a stream of lines
	 */
	public Stream<String> toLineStream() throws NullPointerException {
		return Stream.of(value.split("\\n"));
	}

	/**
	 * Splits the string by the given regex.
	 *
	 * @param regex the regex to split by
	 * @return the given string as a stream of strings
	 */
	public Stream<String> splitBy(String regex) throws NullPointerException {
		return Stream.of(value.split(regex));
	}

	@Override
	public StringHolder peak(Consumer<String> consumer) {
		consumer.accept(value);
		return this;
	}


	/**
	 * Convert the current value to an Integer if possible
	 *
	 * @return {@link IntHolder}
	 * @throws NullPointerException if the current value is null
	 */
	public IntHolder toIntValue() throws NullPointerException {
		return new IntHolder(Integer.parseInt(value));
	}

	/**
	 * Convert the current value to a Byte if possible
	 *
	 * @return {@link ByteHolder}
	 * @throws NullPointerException if the current value is null
	 */
	public ByteHolder toByteValue() throws NullPointerException {
		return new ByteHolder(Byte.parseByte(value));
	}

	/**
	 * Convert the current value to a Short if possible
	 *
	 * @return {@link ShortHolder}
	 * @throws NullPointerException if the current value is null
	 */
	public ShortHolder toShortValue() throws NullPointerException {
		return new ShortHolder(Short.parseShort(value));
	}

	/**
	 * Convert the current value to a Float if possible
	 *
	 * @return {@link FloatHolder}
	 * @throws NullPointerException if the current value is null
	 */
	public FloatHolder toFloatValue() throws NullPointerException {
		return new FloatHolder(Float.parseFloat(value));
	}

	/**
	 * Convert the current value to a Double if possible
	 *
	 * @return {@link DoubleHolder}
	 * @throws NullPointerException if the current value is null
	 */
	public DoubleHolder toDoubleValue() throws NullPointerException {
		return new DoubleHolder(Double.parseDouble(value));
	}

	/**
	 * Convert the current value to a Long if possible
	 *
	 * @return {@link LongHolder}
	 * @throws NullPointerException if the current value is null
	 */
	public LongHolder toLongValue() throws NullPointerException {
		return new LongHolder(Long.parseLong(value));
	}

	/**
	 * Convert the current value to a Character if possible
	 *
	 * @return {@link CharHolder}
	 * @throws NullPointerException if the current value is null
	 */
	public CharHolder toCharValue() throws NullPointerException {
		if (value.length() != 1) {
			throw new IllegalArgumentException("Can't convert \"%s\" to a Character".formatted(value));
		}
		return new CharHolder(value.charAt(0));
	}
}
