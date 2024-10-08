package com.wonkglorg.util.string;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class StringUtils {
	private static ThreadLocalRandom random = ThreadLocalRandom.current();
	private StringUtils() {
	}

	/**
	 * Pads a string to the left with spaces if the String is less than the required length
	 *
	 * @param str The string to pad
	 * @param totalLength The total length of the padded string if the padding is less than the
	 * length
	 * of
	 * the string no additional padding will be added
	 * @return The padded string
	 */
	public static String padLeft(final Object str, final int totalLength) {
		return padLeft(str, totalLength, ' ');
	}

	/**
	 * Pads a string to the left with a specified character if the String is less than the required
	 * length
	 *
	 * @param str The string to pad
	 * @param totalLength The total length of the padded string
	 * @param character The character to pad with
	 * @return The padded string or just the padding if the specified object is null or empty
	 */
	public static String padLeft(final Object str, final int totalLength, final char character) {
		if (str == null) {
			return String.valueOf(character).repeat(Math.max(0, totalLength));
		}
		var stringValue = String.valueOf(str);
		return String.valueOf(character).repeat(Math.max(0, totalLength - stringValue.length()))
				+ stringValue;
	}

	/**
	 * Pads a string to the right with spaces if the String is less than the required length
	 *
	 * @param str The string to pad
	 * @param totalLength The total length of the padded string
	 * @return The padded string
	 */
	public static String padRight(final Object str, final int totalLength) {
		return padRight(str, totalLength, ' ');
	}

	/**
	 * Pads a string to the right with a specified character if the String is less than the required
	 * length
	 *
	 * @param str The string to pad
	 * @param totalLength The total length of the padded string
	 * @param character The character to pad with
	 * @return The padded string or just the padding if the specified object is null or empty
	 */
	public static String padRight(final Object str, final int totalLength, final char character) {
		if (str == null) {
			return String.valueOf(character).repeat(Math.max(0, totalLength));
		}
		var stringValue = String.valueOf(str);
		return stringValue + String.valueOf(character)
				.repeat(Math.max(0, totalLength - stringValue.length()));
	}

	/**
	 * Pads a string to the center with spaces if the String is less than the required length
	 *
	 * @param str The string to pad
	 * @param totalLength The total length of the padded string
	 * @return The padded string
	 */
	public static String padCenter(final Object str, final int totalLength) {
		return padCenter(str, totalLength, ' ');
	}

	/**
	 * Pads a string to the center with a specified character if the padding length is less or equal
	 * to the strings total size no modification will be made.
	 *
	 * @param str The string to pad
	 * @param totalLength The total length of the padded string
	 * @param character The character to pad with
	 * @return The padded string or just the padding if the specified object is null or empty
	 */
public static String padCenter(final Object str, int totalLength, final char character) {
			totalLength = Math.max(totalLength, 0);
    if (str == null) {
			return String.valueOf(character).repeat(totalLength);
    }

    final String stringValue = String.valueOf(str);
    final int padding = totalLength - stringValue.length();
    if (padding <= 0) {
        return stringValue;
    }

    final int leftPadding = padding / 2;
    final int rightPadding = padding - leftPadding;

	return String.valueOf(character).repeat(leftPadding) + stringValue + String.valueOf(character)
			.repeat(Math.max(0, rightPadding));
}
	/**
	 * Truncates a string to a specified length
	 *
	 * @param str The string to truncate
	 * @param length The length to truncate the string to
	 * @param offset The offset to start truncating from
	 * @return The truncated string
	 */
	public static String truncate(final String str, final int length, final int offset) {
		if (str.length() <= length + offset) {
			return str;
		}

		return str.substring(offset, offset + length);
	}

	public static int countOccurrences(String str, char character) {
		return (int) str.chars().filter(c -> c == character).count();
	}

	/**
	 * Counts all occurrences of a character in a String ignoring case
	 *
	 * @param str the String to count the chars in
	 * @param character the character to match
	 * @return the amount found
	 */
	@Contract(pure = true)
	public static int countOccurrences(final String str, final char character, boolean ignoreCase) {
		if (!ignoreCase) {
			return countOccurrences(str, character);
		}

		char finalCharacter = Character.toLowerCase(character);
		return (int) str.toLowerCase().chars().filter(c -> c == finalCharacter).count();
	}

	/**
	 * Counts all leading characters in a string
	 *
	 * @param str The string to count the leading characters from
	 * @param character The character to count
	 * @return The number of leading characters  or 0 if input is null
	 */
	public static int countAllLeading(@Nullable String str, char character) {
		int count = 0;
		if (str == null) {
			return 0;
		}

		int i = 0;
		while (i < str.length() && str.charAt(i) == character) {
			count++;
			i++;
		}
		return count;
	}


	/**
	 * Counts all trailing characters in a string
	 *
	 * @param str The string to count the trailing characters from
	 * @param character The character to count
	 * @return The number of trailing characters or 0 if input is null
	 */
	public static int countAllTrailing(@Nullable String str, char character) {
		int count = 0;
		if (str == null) {
			return 0;
		}
		int i = str.length() - 1;
		while (i >= 0 && str.charAt(i) == character) {
			count++;
			i--;
		}
		return count;
	}

	/**
	 * Removes all trailing characters from a string
	 *
	 * @param str The string to remove the trailing characters from
	 * @param character The character to remove
	 * @return The string with the trailing characters removed or null if input is null
	 */
	public static @Nullable String removeAllTrailing(@Nullable String str, char character) {
		if (str == null) {
			return null;
		}
		int i = str.length() - 1;
		while (i >= 0 && str.charAt(i) == character) {
			i--;
		}
		return str.substring(0, i + 1);
	}

	/**
	 * Wraps a string with a character
	 * <br>
	 * Example: wrap("Hello", '*') -> "*Hello*"
	 *
	 * @param str The string to wrap
	 * @param wrapWith The string to wrap str with
	 * @return The wrapped string
	 */
	public static String wrap(@Nullable String str, char wrapWith) {
		return wrapWith + str + wrapWith;
	}

	/**
	 * Removes all leading characters from a string
	 *
	 * @param str The string to remove the leading characters from
	 * @param character The character to remove
	 * @return The string with the leading characters removed or null if input is null
	 */
	public static @Nullable String removeAllLeading(@Nullable String str, char character) {
		int i = 0;
		if (str == null) {
			return null;
		}
		while (i < str.length() && str.charAt(i) == character) {
			i++;
		}
		return str.substring(i);
	}

	/**
	 * Wraps a string with another string
	 * <br>
	 * Example: wrap("Hello", "*") -> "*Hello*"
	 *
	 * @param str The string to wrap
	 * @param wrapWith The string to wrap str with
	 * @return The wrapped string
	 */
	public static String wrap(@NotNull String str, String wrapWith) {
		return wrapWith + str + wrapWith;
	}

	/**
	 * Unwraps a string if it is wrapped with a character
	 * <br>
	 * Example: unwrap("*Hello*", '*') -> "Hello"
	 *
	 * @param str The string to unwrap
	 * @param wrapWith The character to unwrap with
	 * @return The unwrapped string
	 */
	public static String unwrap(String str, char wrapWith) {
		if (str.charAt(0) == wrapWith && str.charAt(str.length() - 1) == wrapWith) {
			return str.substring(1, str.length() - 1);
		}
		return str;
	}

	/**
	 * Unwraps a string if it is wrapped with another string
	 * <br>
	 * Example: unwrap("*Hello*", '*') -> "Hello"
	 *
	 * @param str The string to unwrap
	 * @param wrapWith The string to unwrap with
	 * @return The unwrapped string
	 */
	public static String unwrap(String str, String wrapWith) {
		if (str.startsWith(wrapWith) && str.endsWith(wrapWith)) {
			return str.substring(wrapWith.length(), str.length() - wrapWith.length());
		}
		return str;
	}

	/**
	 * Wraps a string with a character if it is not already wrapped, if it is wrapped on 1 side it
	 * will wrap the other side to match
	 * <br>
	 * Examples:
	 * <br>
	 * wrapIfMissing("Hello", '*') -> "*Hello*"
	 * <br>
	 * wrapIfMissing("*Hello*", '*') -> "*Hello*"
	 * <br>
	 * wrapIfMissing("*Hello", '*') -> "*Hello*"
	 *
	 * @param str The string to wrap
	 * @param wrapWith The character to wrap str with
	 * @return The wrapped string
	 */
	public static String wrapIfMissing(String str, char wrapWith) {
		if (str.charAt(0) != wrapWith) {
			str = wrapWith + str;
		}
		if (str.charAt(str.length() - 1) != wrapWith) {
			str = str + wrapWith;
		}
		return str;
	}

	/**
	 * Wraps a string with a string if it is not already wrapped, if it is wrapped on 1 side it will
	 * wrap the other side to match
	 * <br>
	 * Examples:
	 * <br>
	 * wrapIfMissing("Hello", "*") -> "*Hello*"
	 * <br>
	 * wrapIfMissing("*Hello*", "*") -> "*Hello*"
	 * <br>
	 * wrapIfMissing("*Hello", "*") -> "*Hello*"
	 *
	 * @param str The string to wrap
	 * @param wrapWith The string to wrap str with
	 * @return The wrapped string
	 */
	public static String wrapIfMissing(String str, String wrapWith) {
		StringBuilder sb = new StringBuilder(str);

		if (!str.startsWith(wrapWith)) {
			sb.insert(0, wrapWith);
		}
		if (!str.endsWith(wrapWith)) {
			sb.append(wrapWith);
		}

		return sb.toString();
	}

	/**
	 * Generates a random string of a specified length
	 *
	 * @param length The length of the string
	 * @return The random string consisting of uppercase and lowercase letters and numbers
	 */
	public static String generateRandomString(int length) {
		return generateRandomString(length,
				"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
	}

	/**
	 * Generates a random string of a specified length with specified characters
	 *
	 * @param length The length of the string
	 * @param characters The characters to use in the string
	 * @return The random string
	 */
  public static String generateRandomString(int length, String characters) {
        length = Math.max(length, 0);
        int charLength = characters.length();
        char[] result = new char[length];

        byte[] randomBytes = new byte[length + 32];
        random.nextBytes(randomBytes);

        for (int i = 0; i < length; i++) {
            result[i] = characters.charAt((randomBytes[i] & 0xFF) % charLength);
        }

        return new String(result);
    }


	/**
	 * Capitalizes the first letter of every word in a String
	 *
	 * @param str The string to capitalize
	 * @return The capitalized string
	 */
	public static String capitalizeAllWords(String str) {
		String[] words = str.split(" ");
		if (str.isEmpty() || words.length == 0) {
			return str;
		}

		return Arrays.stream(words)
				.map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
				.collect(Collectors.joining(" "));
	}

	public static String[] split(String input, char delimiter) {
		if (input == null) {
			return new String[0];
		}

		ArrayList<String> resultList = new java.util.ArrayList<>();

		StringBuilder currentSubstring = new StringBuilder();

		for (char c : input.toCharArray()) {
			if (c == delimiter) {
				resultList.add(currentSubstring.toString());
				currentSubstring.setLength(0);
			} else {
				currentSubstring.append(c);
			}
		}

		resultList.add(currentSubstring.toString());

		return resultList.toArray(new String[0]);
	}

	/**
	 * Formats a String c# style which label them based on index and not position.
	 * Example:
	 * <p>
	 * format("Hello {0}, welcome to this {1}, Good bye {0}","World","Program")
	 *
	 * @param input the input string
	 * @param params the parameters to replace it with
	 * @return the replaced output
	 */
	public static String format(@NotNull String input, Object... params) {
		StringBuilder result = new StringBuilder(input);
		for (int i = 0; i < params.length; i++) {
			String placeholder = "\\{" + i + "}";
			Pattern pattern = Pattern.compile(placeholder);
			Matcher matcher = pattern.matcher(result);
			String replacement = params[i] == null ? "" : params[i].toString();

			int start = 0;
			while (matcher.find(start)) {
				result.replace(matcher.start(), matcher.end(), replacement);
				start = matcher.start() + replacement.length();
			}
		}

		return result.toString();
	}

	public static String formatDecimals(double decimalValue, int maxDecimalPlaces,
			boolean trimTrailingZeros) {
		return new BigDecimal(decimalValue).setScale(maxDecimalPlaces, RoundingMode.HALF_UP)
				.stripTrailingZeros().toString();
	}


	public static List<String> lineBreakSentences(String sentence, int maxWidth) {
		List<String> result = new ArrayList<>();
		StringBuilder currentLine = new StringBuilder();
		StringBuilder currentWord = new StringBuilder();

		for (char ch : sentence.toCharArray()) {
			if (ch == ' ') {
				if (currentLine.length() + currentWord.length() <= maxWidth) {
					if (!currentLine.isEmpty()) {
						currentLine.append(' ');
					}
				} else {

					result.add(currentLine.toString());
					currentLine.setLength(0);

				}
				currentLine.append(currentWord);
				currentWord.setLength(0);
			} else {
				currentWord.append(ch);
			}
		}


		if (!currentWord.isEmpty()) {
			if (currentLine.length() + currentWord.length() <= maxWidth) {
				if (!currentLine.isEmpty()) {
					currentLine.append(' ');
				}
				currentLine.append(currentWord);
			} else {
				result.add(currentLine.toString());
				currentLine.setLength(0);
				currentLine.append(currentWord);
			}
		}


		if (!currentLine.isEmpty()) {
			result.add(currentLine.toString());
		}

		return result;
	}

}
