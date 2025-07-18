package com.wonkglorg.util.converter.date;

import java.math.BigInteger;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.time.Duration.*;

/**
 * Enum for common date types
 */
public enum DateType {
	NANO("ns", "Nanosecond", "Nanoseconds", (byte) 1, ofNanos(1)),
	MICRO("µ", "Microsecond", "Microseconds", (byte) 2, ofNanos(1000)),
	MILLI("ms", "Millisecond", "Milliseconds", (byte) 3, ofMillis(1)),
	SECOND("s", "Second", "Seconds", (byte) 4, ofSeconds(1)),
	MINUTE("m", "Minute", "Minutes", (byte) 5, ofMinutes(1)),
	HOUR("h", "Hour", "Hours", (byte) 6, ofHours(1)),
	DAY("d", "Day", "Days", (byte) 7, ofDays(1)),
	WEEK("W", "Week", "Weeks", (byte) 8, ofDays(7)),
	/** An average month based on 30 days */
	MONTH("M", "Month", "Months", (byte) 9, ofDays(30)),
	/** An average year based on 365 days */
	YEAR("Y", "Year", "Years", (byte) 10, ofDays(365)),
	/** An average decade based on 365 day year */
	DECADE("D", "Decade", "Decades", (byte) 11, ofDays(365 * 10L)),
	/** An average century based on 365 day year */
	CENTURY("C", "Century", "Centuries", (byte) 12, ofDays(365 * 100L)),
	/** An average millennia based on 365 day year */
	MILLENNIA("ML", "Millennium", "Millennia", (byte) 13, ofDays(365 * 1000L)),
	/** An era set based on 365 day year and a million years */
	ERA("E", "Era", "Eras", (byte) 14, ofDays(365 * 1000000L));

	private static final Map<String, DateType> LOOKUP_CACHE = new HashMap<>();
	private final String postfix;
	private final String fullNameSingular;
	private final String fullNamePlural;
	private final Duration duration;

	/**
	 * Behaves the same as Most vs Least significant bits, which determine what value is considered
	 * more impactful, the higher the value the more important it is (this value is in positive byte
	 * range 1 to 14)
	 */
	private final byte significance;

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
	 * NanoSecond representation of a DateType up to the "Second" format, this value never exceeds
	 * 999.999.999 and is always positive
	 */
	private final int nanoseconds;
	/**
	 * Total Nanosecond time, including values above seconds
	 */
	private final BigInteger nanoSecondsTotal;
	/**
	 * Total Milliseconds represented by this time stamp
	 */
	private final BigInteger millisecondsTotal;

	DateType(String postfix, String fullNameSingular, String fullNamePlural, byte significance,
			Duration duration) {
		this.postfix = postfix;
		this.fullNameSingular = fullNameSingular;
		this.fullNamePlural = fullNamePlural;
		this.significance = significance;
		this.duration = duration;
		this.milliseconds = duration.toMillis();
		this.seconds = duration.toSeconds();
		this.nanoseconds = duration.getNano();
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
	public int getNanoseconds() {
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
	public BigInteger getTotalMilliSeconds() {
		return millisecondsTotal;
	}

	/**
	 * Converts a given value to NanoSeconds
	 *
	 * @param time the time value
	 * @param format the type
	 * @return a value representing the given time in NanoSeconds
	 */
	public static BigInteger toNanos(long time, DateType format) {
		return BigInteger.valueOf(time).multiply(format.getTotalNanoSeconds());
	}

	/**
	 * Converts a given value to MilliSeconds
	 *
	 * @param time the time value
	 * @param format the type
	 * @return a value representing the given time in MilliSeconds
	 */
	public static BigInteger toMillis(long time, DateType format) {
		return BigInteger.valueOf(time).multiply(format.getTotalMilliSeconds());
	}

	/**
	 * Wether the scale is small enough to be stored in the nanos format (anything lower than seconds
	 * fits this descriptor)
	 *
	 * @return
	 */
	public boolean typeStoredInNanos() {
		return significance < 4;
	}

	/**
	 * @return the representing dateTypes full name singular
	 */
	public String getFullNameSingular() {
		return fullNameSingular;
	}

	public String getFullNamePlural() {
		return fullNamePlural;
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

	/**
	 * Finds a matching DateType by its {@link #fullNameSingular}(includes singular and plural forms)
	 * or
	 * {@link #postfix}
	 *
	 * @param identifier {@link #fullNameSingular}(includes singular and plural forms) or
	 * {@link #postfix} Capitalisation matters when given the shortend form! only full names singular and plural are case insensitive!
	 * @return a valid dateype identified by the given identifier or {@link Optional#empty()}
	 */
	public static DateType fromIdentifier(String identifier) {
		populateCache();
		identifier = identifier.length() > 2 ? identifier.toLowerCase() : identifier;
		return LOOKUP_CACHE.getOrDefault(identifier.strip(), null);
	}


	private static void populateCache() {
		if (!LOOKUP_CACHE.isEmpty()) {
			return;
		}
		for (var type : DateType.values()) {
			LOOKUP_CACHE.put(type.getPostfix(), type);
			LOOKUP_CACHE.put(type.getFullNameSingular().toLowerCase(), type);
			LOOKUP_CACHE.put(type.getFullNamePlural().toLowerCase(), type);
		}
	}

	public byte getSignificance() {
		return significance;
	}
}
