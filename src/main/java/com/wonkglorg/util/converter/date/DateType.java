package com.wonkglorg.util.converter.date;

import java.math.BigInteger;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Enum for common date types
 */
public enum DateType {
	NANO("ns", "Nanosecond", Duration.ofNanos(1)),
	MICRO("µ", "Microsecond", Duration.ofNanos(1000)),
	MILLI("ms", "Millisecond", Duration.ofMillis(1)),
	SECOND("s", "Second", Duration.ofSeconds(1)),
	MINUTE("m", "Minute", Duration.ofMinutes(1)),
	HOUR("h", "Hour", Duration.ofHours(1)),
	DAY("d", "Day", Duration.ofDays(1)),
	WEEK("W", "Week", Duration.ofDays(7)),
	/** An average month based on 30 days */
	MONTH("M", "Month", Duration.ofDays(30)),
	/** An average year based on 365 days */
	YEAR("Y", "Year", Duration.ofDays(365)),
	/** An average decade based on 365 day year */
	DECADE("D", "Decade", Duration.ofDays(365 * 10L)),
	/** An average century based on 365 day year */
	CENTURY("C", "Century", Duration.ofDays(365 * 100L)),
	/** An average millennia based on 365 day year */
	MILLENNIA("ML", "Millennia", Duration.ofDays(365 * 1000L)),
	/** An era set based on 365 day year and a million years */
	ERA("E", "Era", Duration.ofDays(365 * 1000000L));
	private final String postfix;
	private final String fullName;
	private final Duration duration;

	//cached for better efficiency
	/**
	 * Second representation of a DateType
	 */
	private final long seconds;
	/**
	 * MilliSecond representation of a DateType
	 */
	private final long milliseconds;
	/**
	 * NanoSecond representation of a DateType up to the "Second" format, this value never exceeds 999.999.999 and is always positive
	 */
	private final long nanoseconds;
	/**
	 * Total Nanosecond time, including values above seconds
	 */
	private final BigInteger nanoSecondsTotal;
	/**
	 * Total Milliseconds represented by this time stamp
	 */
	private final BigInteger millisecondsTotal;

	DateType(String postfix, String fullName, Duration duration) {
		this.postfix = postfix;
		this.fullName = fullName;
		this.duration = duration;
		this.milliseconds = duration.toMillis();
		this.seconds = duration.toSeconds();
		this.nanoseconds = duration.toNanos();
		this.nanoSecondsTotal = BigInteger.valueOf(seconds)//
				.multiply(BigInteger.valueOf(1_000_000_000L))//
				.add(BigInteger.valueOf(nanoseconds));

		this.millisecondsTotal = BigInteger.valueOf(seconds)//
				.multiply(BigInteger.valueOf(1_000L))//
				.add(BigInteger.valueOf(nanoseconds / 1_000_000));
	}

	/**
	 * @return This values representing postfix
	 */
	public String getPostfix() {
		return postfix;
	}

	/**
	 * @return the represented dateType in seconds
	 */
	public long getSeconds() {
		return seconds;
	}

	/**
	 * @return the represented dateType in milliseconds
	 * @throws ArithmeticException if numeric overflow occurs
	 */
	public long getMilliseconds() {
		return milliseconds;
	}

	/**
	 * @return is always positive, and never exceeds 999,999,999. This reaches up to the 1 Second
	 * mark, anything bigger can be retrieved from {@link #getSeconds()}
	 */
	public long getNanoseconds() {
		return nanoseconds;
	}

	/**
	 * @return the entire value as nanoseconds
	 */
	public BigInteger getTotalNanoSeconds() {
		return nanoSecondsTotal;
	}

	/**
	 * @return the time in nanoseconds without number exception potential for large values
	 */
	public BigInteger getTotalMilliSeconds(){
		return millisecondsTotal;
	}

	/**
	 * Converts a given value to NanoSeconds
	 * @param time the time value
	 * @param format the type
	 * @return a value representing the given time in NanoSeconds
	 */
	public static BigInteger toNanos(long time, DateType format) {
		return BigInteger.valueOf(time).multiply(format.getTotalNanoSeconds());
	}
	
	/**
	 * Converts a given value to MilliSeconds
	 * @param time the time value
	 * @param format the type
	 * @return a value representing the given time in MilliSeconds
	 */
	public static BigInteger toMillis(long time, DateType format) {
		return BigInteger.valueOf(time).multiply(format.getTotalMilliSeconds());
	}

	/**
	 * @return the representing dateTypes full name singular
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @return The {@link ChronoUnit} equivalent of this type
	 */
	public ChronoUnit toChronoUnit() {
		return switch (this) {
			case NANO -> ChronoUnit.NANOS;
			case MICRO -> ChronoUnit.MICROS;
			case MILLI -> ChronoUnit.MILLIS;
			case SECOND -> ChronoUnit.SECONDS;
			case MINUTE -> ChronoUnit.MINUTES;
			case HOUR -> ChronoUnit.HOURS;
			case DAY -> ChronoUnit.DAYS;
			case WEEK -> ChronoUnit.WEEKS;
			case MONTH -> ChronoUnit.MONTHS;
			case YEAR -> ChronoUnit.YEARS;
			case DECADE -> ChronoUnit.DECADES;
			case CENTURY -> ChronoUnit.CENTURIES;
			case MILLENNIA -> ChronoUnit.MILLENNIA;
			case ERA -> ChronoUnit.ERAS;
		};
	}

	/**
	 * Gets the duration associated with this time
	 */
	public Duration getDuration() {
		return duration;
	}
}
