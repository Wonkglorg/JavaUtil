package com.wonkglorg.util.convert;

import com.wonkglorg.util.converter.date.DurationFormatter;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.Test;

class DurationFormatterTest {
	
	@Test
	void testHoursMinutesSeconds() {
		Duration duration = Duration.ofHours(2)
				.plusMinutes(34)
				.plusSeconds(56);
		
		assertEquals(
				"02:34:56",
				DurationFormatter.ofPattern("HH:mm:ss").format(duration));
	}
	
	@Test
	void testDaysHoursMinutesSeconds() {
		Duration duration = Duration.ofDays(3)
				.plusHours(4)
				.plusMinutes(5)
				.plusSeconds(6);
		
		assertEquals(
				"03 04:05:06",
				DurationFormatter.ofPattern("dd HH:mm:ss").format(duration));
	}
	
	@Test
	void testMilliseconds() {
		Duration duration = Duration.ofSeconds(1)
				.plusMillis(250);
		
		assertEquals(
				"01.250",
				DurationFormatter.ofPattern("ss.SSS").format(duration));
	}
	
	@Test
	void testMicroseconds() {
		Duration duration = Duration.ofNanos(123_456);
		
		assertEquals(
				"123",
				DurationFormatter.ofPattern("uuu").format(duration));
	}
	
	@Test
	void testNanoseconds() {
		Duration duration = Duration.ofNanos(123_456_789);
		
		assertEquals(
				"123456789",
				DurationFormatter.ofPattern("nnnnnnnnn").format(duration));
	}
	
	@Test
	void testQuotedLiteral() {
		Duration duration = Duration.ofMinutes(15);
		
		assertEquals(
				"Elapsed: 00:15",
				DurationFormatter.ofPattern("'Elapsed: 'HH:mm").format(duration));
	}
	
	@Test
	void testUnitLiterals() {
		Duration duration = Duration.ofHours(3)
				.plusMinutes(4)
				.plusSeconds(5);
		
		assertEquals(
				"03h 04m 05s",
				DurationFormatter.ofPattern("HH'h' mm'm' ss's'").format(duration));
	}
	
	@Test
	void testEscapedQuote() {
		Duration duration = Duration.ofSeconds(10);
		
		assertEquals(
				"It's 10",
				DurationFormatter.ofPattern("'It''s 'ss").format(duration));
	}
	
	@Test
	void testLiteralCharacters() {
		Duration duration = Duration.ofHours(7)
				.plusMinutes(30);
		
		assertEquals(
				"[07:30]",
				DurationFormatter.ofPattern("[HH:mm]").format(duration));
	}
	
	@Test
	void testPadding() {
		Duration duration = Duration.ofSeconds(7);
		
		assertEquals(
				"0007",
				DurationFormatter.ofPattern("ssss").format(duration));
	}
	
	@Test
	void testFormatterCaching() {
		DurationFormatter first = DurationFormatter.ofPattern("HH:mm:ss");
		DurationFormatter second = DurationFormatter.ofPattern("HH:mm:ss");
		
		assertSame(first, second);
	}
	
	@Test
	void testEmptyPatternThrows() {
		assertThrows(
				IllegalArgumentException.class,
				() -> DurationFormatter.ofPattern(""));
	}
	
	@Test
	void testNullPatternThrows() {
		assertThrows(
				IllegalArgumentException.class,
				() -> DurationFormatter.ofPattern(null));
	}
	
	@Test
	void testUnterminatedQuoteThrows() {
		assertThrows(
				IllegalArgumentException.class,
				() -> DurationFormatter.ofPattern("'Hello"));
	}
	
	@Test
	void testZeroDuration() {
		assertEquals(
				"00:00:00",
				DurationFormatter.ofPattern("HH:mm:ss").format(Duration.ZERO));
	}
	
	@Test
	void testWidthOne() {
		assertEquals(
				"5",
				DurationFormatter.ofPattern("s").format(Duration.ofSeconds(5)));
	}
	
	@Test
	void testLargeHours() {
		assertEquals(
				"123",
				DurationFormatter.ofPattern("HH").format(Duration.ofHours(123)));
	}
}