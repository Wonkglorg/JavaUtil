package com.wonkglorg.util.convert;

import com.wonkglorg.util.converter.date.DateType;
import com.wonkglorg.util.converter.date.TimeBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

class DateTypeTest {
	@Test
	void canResolveLargeStringInputs() {
		String expectedTime = "124E 335ML 7D 7Y 4M 2W 5d 14h 21m 43s 333ms";
		long expectedMillis = 3921031000333303333L;
		TimeBuilder builder = TimeBuilder.create(expectedTime);
		Assertions.assertEquals(expectedMillis, builder.toMillis());
		for(int i = 0; i <999999;i++) {
			TimeBuilder.create(Duration.of(i, ChronoUnit.SECONDS)).forceShowAllTypes().toTimeString();
		}
		System.out.println(builder.toTimeString());
	}


	@Test
	void canResolveIdentifiers() {
		for (DateType type : DateType.values()) {
			String postfix = type.getPostfix();
			String singular = type.getFullNameSingular();
			String plural = type.getFullNamePlural();

			Assertions.assertEquals(type, DateType.fromIdentifier(postfix),
					() -> "Should resolve postfix: " + postfix);
			Assertions.assertEquals(type, DateType.fromIdentifier(singular),
					() -> "Should resolve singular: " + singular);
			Assertions.assertEquals(type, DateType.fromIdentifier(plural),
					() -> "Should resolve plural: " + plural);

			String badCasedPostfix = flipCase(postfix);
			if (!badCasedPostfix.equals(postfix)) {
				Assertions.assertNotEquals(type, DateType.fromIdentifier(badCasedPostfix),
						() -> "Should NOT resolve incorrectly cased postfix: " + badCasedPostfix);
			}
		}
	}

	private String flipCase(String str) {
		StringBuilder flipped = new StringBuilder();
		for (char c : str.toCharArray()) {
			flipped.append(
					Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c));
		}
		return flipped.toString();
	}


	
	/*
	@Test
	void convertBackAndFourth() {
		String expectedTime = "124E 335ML 7D 7Y 4M 2W 5d 14h 21m 43s 333ms";
		long expectedMillis = 3921031000333303333L;
		long result = fromTimeString(expectedTime).toMilliseconds();
		String revertedString = toTimeString().inputMillie(result).build();
		System.out.println("Input time: " + expectedTime);
		System.out.println("---------------------");
		System.out.println("Millies result: " + result);
		System.out.println("---------------------");
		System.out.println("Reverse Conversion: " + revertedString);
		assertEquals(expectedMillis, result);
		assertEquals(expectedTime, revertedString);
	}

	/*
	@Test
	void testMappings() {
		var result = fromTimeString("10s").toTimeMap(false);
		assertEquals("{SECOND=10.0}", result.toString());

		var resultTime = toTimeString().inputMillie(1039).typesToShow(DateType.SECOND).toTimeMap();
		assertEquals("{SECOND=1.039}", resultTime.toString());
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
		assertEquals(10, fromTimeString("10s").toSeconds());
		assertEquals(10, fromTimeString("10 s").toSeconds());
		assertEquals(10, fromTimeString("10second").toSeconds());
		assertEquals(10, fromTimeString("10 second").toSeconds());
		assertEquals(10, fromTimeString("10seconds").toSeconds());
		assertEquals(10, fromTimeString("10 seconds").toSeconds());

		assertEquals(10000, fromTimeString("10 seconds").toMilliseconds());
		assertEquals(10100, fromTimeString("10,1 seconds").toMilliseconds());
		assertEquals(10100, fromTimeString("10.1 seconds").toMilliseconds());
		assertEquals(10000, fromTimeString("10 seconds").toMilliseconds());
	}

	@Test
	void decimalFormatting(){
		System.out.println(toTimeString().inputMillie(1030)
				.typesToShow(DateType.SECOND).build());

	}

	@Test()
	void showNoValueOnNegativeTime() {
		assertThrows(IllegalArgumentException.class, () -> toTimeString().inputMillie(-4).build(),
				"Time cannot be less than 0!");
	}
	
	 */
}
