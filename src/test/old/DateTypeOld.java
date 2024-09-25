package com.wonkglorg.util.converter.date;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Enum for common date types
 */
public enum DateType {
	MILLISECOND("ms", "millisecond", 1),
	SECOND("s", "second", 1000L),
	MINUTE("m", "minute", 1000L * 60),
	HOUR("h", "hour", 1000L * 60 * 60),
	DAY("d", "day", 1000L * 60 * 60 * 24),
	WEEK("W", "week", 1000L * 60 * 60 * 24 * 7),
	MONTH("M", "month", 1000L * 60 * 60 * 24 * 30),
	YEAR("Y", "year", 1000L * 60 * 60 * 24 * 365),
	DECADE("D", "decade", 1000L * 60 * 60 * 24 * 365 * 10),
	CENTURY("C", "century", 1000L * 60 * 60 * 24 * 365 * 100),
	MILLENNIA("ML", "millennia", 1000L * 60 * 60 * 24 * 365 * 1000),
	ERA("E", "era", 1000L * 60 * 60 * 24 * 365 * 1000000);
	private final String preset;
	private final String fullName;
	private final long toMilliseconds;

	DateType(String preset, String fullName, long toMilliseconds) {
		this.preset = preset;
		this.fullName = fullName;
		this.toMilliseconds = toMilliseconds;
	}

	public String getPreset() {
		return preset;
	}

	public long getMilliseconds() {
		return toMilliseconds;
	}

	public String getFullName() {
		return fullName;
	}

	public long getMultiplierToMillies() {
		return toMilliseconds;
	}

	public DateType convertFromChronoUnit(ChronoUnit chronoUnit) {
		chronoUnit.getDuration()
	}

	public Duration getDuration() {
		return Duration.ofMillis(toMilliseconds);
	}
}
