package com.wonkglorg.util.converter.date;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Enum for common date types
 */
public enum DateType {
	NANO("n", "Nanosecond", Duration.ofNanos(1)),
	MICRO("µ", "Microsecond", Duration.ofNanos(1000)),
	MILLISECOND("ms", "Millisecond", Duration.ofMillis(1)),
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

	DateType(String postfix, String fullName, Duration duration) {
		this.postfix = postfix;
		this.fullName = fullName;
		this.duration = duration;
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
		return duration.getSeconds();
	}

	/**
	 * @return the represented dateType in milliseconds
	 * @throws ArithmeticException if numeric overflow occurs
	 */
	public long getMilliseconds() {
		return duration.toMillis();
	}

	/**
	 * @return the represented dateType in nanoSeconds
	 * @throws ArithmeticException if numeric overflow occurs
	 */
	public long getNanos() {
		return duration.toNanos();
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
			case MILLISECOND -> ChronoUnit.MILLIS;
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
