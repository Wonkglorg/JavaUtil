package com.wonkglorg.util.parser;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class GenericNumberParser {

	public static Number parseNumber(String input) {
		// Attempt parsing in English locale
		Number number = tryParse(input, Locale.ENGLISH);
		if (number == null) {
			// If English fails, try German locale
			number = tryParse(input, Locale.GERMAN);
		}
		return number;
	}

	private static Number tryParse(String input, Locale locale) {
		NumberFormat format = NumberFormat.getInstance(locale);
		try {
			Number number = format.parse(input);

			// Convert to the most suitable type
			if (number instanceof Long || number.doubleValue() == Math.floor(number.doubleValue())) {
				long longValue = number.longValue();
				if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
					return (int) longValue;
				} else {
					return longValue;
				}
			} else {
				double doubleValue = number.doubleValue();
				if (doubleValue >= Float.MIN_VALUE && doubleValue <= Float.MAX_VALUE) {
					return (float) doubleValue;
				} else {
					return doubleValue;
				}
			}
		} catch (ParseException e) {
			// Parsing failed for this locale
			return null;
		}
	}
}


