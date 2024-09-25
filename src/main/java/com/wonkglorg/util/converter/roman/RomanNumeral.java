package com.wonkglorg.util.converter.roman;

import java.util.regex.Pattern;

/**
 * Represents roman numerals
 */
public enum RomanNumeral {
	I(1),
	V(5),
	X(10),
	L(50),
	C(100),
	D(500),
	M(1000);

	private final int value;

	RomanNumeral(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	private static final Pattern numeralPattern = Pattern.compile("^[IVXLCDM]+$");

	/**
	 * Only verifies if the used symbols would make up a valid set of numerals
	 *
	 * @return true if valid symbols have been used, false otherwise
	 */
	public static boolean isValidNumeral(String romanNumeral) {
		return romanNumeral != null && numeralPattern.matcher(romanNumeral).matches();
	}


}
