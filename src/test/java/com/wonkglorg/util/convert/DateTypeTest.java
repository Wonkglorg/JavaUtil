package com.wonkglorg.util.convert;

import com.wonkglorg.util.converter.date.DateType;
import com.wonkglorg.util.converter.date.DurationBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

class DateTypeTest{
	@Test
	void canResolveLargeStringInputs() {
		String expectedTime = "124E 335ML 7D 7Y 4M 2W 5d 14h 21m 43s 333ms";
		long expectedMillis = 3921031000333303333L;
		DurationBuilder builder = DurationBuilder.create(expectedTime);
		assertEquals(expectedMillis, builder.toMillis());
		for(int i = 0; i < 999999; i++){
			DurationBuilder.create(Duration.of(i, ChronoUnit.SECONDS)).forceShowAllTypes().toTimeString();
		}
		System.out.println(builder.toTimeString());
	}
	
	@Test
	void canResolveIdentifiers() {
		for(DateType type : DateType.values()){
			String postfix = type.getPostfix();
			String singular = type.getFullNameSingular();
			String plural = type.getFullNamePlural();
			
			assertEquals(type, DateType.fromIdentifier(postfix), () -> "Should resolve postfix: " + postfix);
			assertEquals(type, DateType.fromIdentifier(singular), () -> "Should resolve singular: " + singular);
			assertEquals(type, DateType.fromIdentifier(plural), () -> "Should resolve plural: " + plural);
		}
	}
	
	@Test
	void convertBackAndFourth() {
		String expectedTime = "124E 335ML 7D 7Y 4M 2W 5d 14h 21m 43s 333ms";
		long expectedMillis = 3921031000333303333L;
		long result = DurationBuilder.create(expectedTime).to(DateType.MILLI);
		String revertedString = DurationBuilder.create(Duration.ofMillis(result)).toTimeString();
		System.out.println("Input time: " + expectedTime);
		System.out.println("---------------------");
		System.out.println("Millies result: " + result);
		System.out.println("---------------------");
		System.out.println("Reverse Conversion: " + revertedString);
		assertEquals(expectedMillis, result);
		assertEquals(expectedTime, revertedString);
	}
	
	@Test
	void canConvertTimeToString() {
		String timePrefixed = DurationBuilder.create(Duration.ofSeconds(10)).toTimeString();
		assertEquals("10s", timePrefixed);
		String timeFullName = DurationBuilder.create(Duration.ofSeconds(10)).useFullName(true).toTimeString();
		assertEquals("10 Seconds", timeFullName);
	}
	
	@Test
	void canConvertStringToTime() {
		assertEquals(10, DurationBuilder.create("10s").to(DateType.SECOND));
		assertEquals(10, DurationBuilder.create("10 s").to(DateType.SECOND));
		assertEquals(10, DurationBuilder.create("10second").to(DateType.SECOND));
		assertEquals(10, DurationBuilder.create("10 second").to(DateType.SECOND));
		assertEquals(10, DurationBuilder.create("10seconds").to(DateType.SECOND));
		assertEquals(10, DurationBuilder.create("10 seconds").to(DateType.SECOND));
		
		assertEquals(10000, DurationBuilder.create("10 seconds").to(DateType.MILLI));
		assertEquals(10100, DurationBuilder.create("10.1 seconds").to(DateType.MILLI));
		assertEquals(10100, DurationBuilder.create("10,1 seconds").to(DateType.MILLI));
		assertEquals(10000, DurationBuilder.create("10 seconds").to(DateType.MILLI));
	}
	
	@Test
	void decimalFormatting() {
		String timeString = DurationBuilder.create(Duration.ofMillis(1030)).typesToShow(DateType.SECOND).toTimeString();
		assertEquals("1.03s", timeString);
		
	}
	
	@Test()
	void showNoValueOnNegativeTime() {
		assertThrows(IllegalArgumentException.class, () -> DurationBuilder.create(Duration.ofMillis(-4)).toTimeString(), "Time cannot be less than 0!");
	}
	
}
