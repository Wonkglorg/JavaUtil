package com.wonkglorg.util.convert;

import com.wonkglorg.util.converter.date.ConverterDate;
import org.junit.jupiter.api.Test;

import static com.wonkglorg.util.converter.date.TimeBuilder.fromTimeString;
import static com.wonkglorg.util.converter.date.TimeBuilder.toTimeString;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DateTypeTest {

	@Test
	void convertBackAndFourth() {
		String expectedTime = "124E 335ML 7D 7Y 4M 2W 5d 14h 21m 43s 333ms";
		long expectedMillies = 3921031000333303333L;

		long result = fromTimeString(expectedTime).toMilliseconds();
		String revertedString = toTimeString().inputMillie(result).build();
		System.out.println("Input time: " + expectedTime);
		System.out.println("---------------------");
		System.out.println("Millies result: " + result);
		System.out.println("---------------------");
		System.out.println("Reverse Conversion: " + revertedString);
		assertEquals(expectedMillies, result);
		assertEquals(expectedTime, revertedString);


	}

	@Test
	void convertTimeToFromString() {
		assertEquals("124E 335ML 7D 7Y 4M 2W 5d 14h 21m 43s 333ms",
				toTimeString().inputMillie(3921031000333303333L).build());

		assertEquals(3921031000333303333L,
				fromTimeString("124E 335ML 7D 7Y 4M 2W 5d 14h 21m 43s 333ms").toMilliseconds());

	}

	@Test
	void canConvertTimeToString() {
		String timePrefixed = toTimeString().inputSeconds(10).build();
		assertEquals("10s", timePrefixed);
		String timeFullName = toTimeString().inputSeconds(10).useFullName(true).build();
		assertEquals("10 Seconds", timeFullName);
		String timeFullNameNonCapital = toTimeString().inputSeconds(10).useFullName(false).build();
		assertEquals("10 seconds", timeFullNameNonCapital);

	}


	@Test
	void canConvertStringToTime() {
		//todo:jmd allow decimals
		assertEquals(10, fromTimeString("10s").toSeconds());
		assertEquals(10, fromTimeString("10 s").toSeconds());
		assertEquals(10, fromTimeString("10second").toSeconds());
		assertEquals(10, fromTimeString("10 second").toSeconds());
		assertEquals(10, fromTimeString("10seconds").toSeconds());
		assertEquals(10, fromTimeString("10 seconds").toSeconds());

		assertEquals(10000, fromTimeString("10 seconds").toMilliseconds());
		assertEquals(10000000000L, fromTimeString("10 seconds").toNano());
	}

	@Test
	void showNoValueOnNegativeTime() {
		assertEquals("", ConverterDate.toTimeString(-4));
	}
}
