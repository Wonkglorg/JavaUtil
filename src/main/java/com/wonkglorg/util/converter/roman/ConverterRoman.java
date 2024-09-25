package com.wonkglorg.util.converter.roman;

import java.util.TreeMap;

/**
 * Converter for Roman numerals
 */
@SuppressWarnings("unused")
public class ConverterRoman {
	private static final TreeMap<Integer, String> romanMap = new TreeMap<>();

	static {
		romanMap.put(1000, "M");
		romanMap.put(900, "CM");
		romanMap.put(500, "D");
		romanMap.put(400, "CD");
		romanMap.put(100, "C");
		romanMap.put(90, "XC");
		romanMap.put(50, "L");
		romanMap.put(40, "XL");
		romanMap.put(10, "X");
		romanMap.put(9, "IX");
		romanMap.put(5, "V");
		romanMap.put(4, "IV");
		romanMap.put(1, "I");
	}

	private ConverterRoman() {
		// Utility class
	}

	/**
	 * Converts a number to a Roman numeral
	 *
	 * @param number number to be converted
	 * @return Roman numeral representation of the input number
	 */
	public static String toRoman(int number) {
		if (number <= 0) {
			throw new IllegalArgumentException("Number must be greater than 0");
		}

		int l = romanMap.floorKey(number);
		if (number == l) {
			return romanMap.get(number);
		}
		return romanMap.get(l) + toRoman(number - l);
	}

	/**
	 * Converts a Roman numeral to a number
	 *
	 * @param roman Roman numeral to be converted
	 * @return number representation of the input Roman numeral
	 */
	public static int fromRoman(String roman) {
		if (RomanNumeral.isValidNumeral(roman)) {
			throw new IllegalArgumentException("Not a valid numeral!");
		}
		return (int) fromRoman(roman, roman.length() - 1, 0);
	}

	private static double fromRoman(String roman, int pos, double rightNumeral) {
		if (pos < 0) {
			return 0;
		}
		char ch = roman.charAt(pos);
		double value = Math.floor(Math.pow(10, "IXCM".indexOf(ch))) + 5 * Math.floor(
				Math.pow(10, "VLD".indexOf(ch)));
		return value * Math.signum(value + 0.5 - rightNumeral) + fromRoman(roman, pos - 1, value);
	}
}
