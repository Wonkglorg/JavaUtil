package com.wonkglorg.util.converter.date;

import java.math.BigInteger;
import java.time.Duration;
import static java.time.Duration.ofDays;
import static java.time.Duration.ofHours;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofNanos;
import static java.time.Duration.ofSeconds;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Enum for common date types
 */
public enum DateType{
	NANO("ns", "Nanosecond", "Nanoseconds", (byte) 1, false, ofNanos(1)),
	MICRO("µ", "Microsecond", "Microseconds", (byte) 2, false, ofNanos(1000)),
	MILLI("ms", "Millisecond", "Milliseconds", (byte) 3, false, ofMillis(1)),
	SECOND("s", "Second", "Seconds", (byte) 4, false, ofSeconds(1)),
	MINUTE("m", "Minute", "Minutes", (byte) 5, true, ofMinutes(1)),
	HOUR("h", "Hour", "Hours", (byte) 6, false, ofHours(1)),
	DAY("d", "Day", "Days", (byte) 7, true, ofDays(1)),
	WEEK("W", "Week", "Weeks", (byte) 8, false, ofDays(7)),
	/**
	 * An average month based on 30 days
	 */
	MONTH("M", "Month", "Months", (byte) 9, true, ofDays(30)),
	/**
	 * An average year based on 365 days
	 */
	YEAR("Y", "Year", "Years", (byte) 10, true, ofDays(365)),
	/**
	 * An average decade based on 365 day year
	 */
	DECADE("D", "Decade", "Decades", (byte) 11, true, ofDays(365 * 10L)),
	/**
	 * An average century based on 365 day year
	 */
	CENTURY("C", "Century", "Centuries", (byte) 12, false, ofDays(365 * 100L)),
	/**
	 * An average millennia based on 365 day year
	 */
	MILLENNIA("ML", "Millennium", "Millennia", (byte) 13, false, ofDays(365 * 1000L)),
	/**
	 * An era set based on 365 day year and a million years
	 */
	ERA("E", "Era", "Eras", (byte) 14, false, ofDays(365 * 1000000L));
	
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
	/**
	 * If the value is case sensitive and needs to match exactly (because more than 1 value shares the same shortened symbol {@link #MINUTE} {@link #MONTH}
	 */
	private final boolean caseSensitive;
	
	DateType(String postfix, String fullNameSingular, String fullNamePlural, byte significance, boolean caseSensitive, Duration duration) {
		this.postfix = postfix;
		this.fullNameSingular = fullNameSingular;
		this.fullNamePlural = fullNamePlural;
		this.significance = significance;
		this.caseSensitive = caseSensitive;
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
	
	public boolean isCaseSensitive() {
		return caseSensitive;
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
		return switch(this) {
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
	 * @param identifier provide either case-insensitive {@link #fullNameSingular} {@link #fullNamePlural} or a {@link #postfix} that needs to be case sensitive in case more than 1 postfix share the same symbol such as: {@link #MINUTE} and {@link #MONTH}
	 * @return a valid date type identified by the given identifier or null
	 */
	public static DateType fromIdentifier(String identifier) {
		populateCache();
		identifier = identifier.length() > 2 ? identifier.toLowerCase() : identifier;
		return LOOKUP_CACHE.getOrDefault(identifier.strip(), null);
	}
	
	private static void populateCache() {
		if(!LOOKUP_CACHE.isEmpty()){
			return;
		}
		for(var type : DateType.values()){
			LOOKUP_CACHE.put(type.getPostfix(), type);
			LOOKUP_CACHE.put(type.getFullNameSingular().toLowerCase(), type);
			LOOKUP_CACHE.put(type.getFullNamePlural().toLowerCase(), type);
			
			if(!type.caseSensitive){
				LOOKUP_CACHE.put(type.getPostfix().toLowerCase(Locale.ROOT), type);
				LOOKUP_CACHE.put(type.getPostfix().toUpperCase(Locale.ROOT), type);
			}
		}
	}
	
	public byte getSignificance() {
		return significance;
	}
}
