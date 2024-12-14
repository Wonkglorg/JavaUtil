package com.wonkglorg.util.converter.date;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * A Class representing a point in time and a Duration away from this point where this Cooldown ends
 * (Millisecond Precision)
 */
public class Cooldown {

	/**
	 * Cooldown in milliseconds that have to pass from the given timestamp until the cooldown is
	 * marked as expired
	 */
	private final Duration duration;
	/**
	 * Time in milliseconds when the cooldown was started
	 */
	private final long startTimeMillis;

	/**
	 * @param duration The duration of the cooldown from the starting Time
	 * @param startTime The time when the cooldown started (default:
	 * {@link System#currentTimeMillis()})
	 */
	public Cooldown(final Duration duration, final long startTime) {
		this.duration = duration;
		this.startTimeMillis = startTime;
	}

	/**
	 * @param duration The duration of the cooldown from the starting Time
	 */
	public Cooldown(final Duration duration) {
		this(duration, System.currentTimeMillis());
	}

	/**
	 * @param cooldown the cooldown in milliseconds until it is marked as expired
	 * @param startTime the start time(default: {@link System#currentTimeMillis()})
	 */
	public Cooldown(long cooldown, long startTime) {
		this(Duration.ofMillis(cooldown), startTime);
	}

	/**
	 * @param cooldown the cooldown in milliseconds until it is marked as expired
	 */
	public Cooldown(long cooldown) {
		this(Duration.ofMillis(cooldown), System.currentTimeMillis());
	}

	/**
	 * @param cooldown the cooldown in a specified timeunit
	 * @param unit the time unit to use (If the resulting units size in milliseconds exceeds the
	 * {@link Long} limit throws an exception)
	 */
	public Cooldown(int cooldown, ChronoUnit unit) {
		this(Duration.of(cooldown, unit));
	}

	/**
	 * @param cooldown the cooldown in a specified timeunit
	 * @param unit the time unit to use  (If the resulting units size in milliseconds exceeds the
	 * {@link Long} limit throws an exception)
	 * @param startTime the start time in milliseconds, (default: {@link System#currentTimeMillis()})
	 */
	public Cooldown(int cooldown, ChronoUnit unit, long startTime) {
		this(Duration.of(cooldown, unit), startTime);
	}


	/**
	 * @return true if the cooldown has expired
	 */
	public boolean isExpired() {
		return System.currentTimeMillis() - startTimeMillis >= duration.toMillis();
	}

	/**
	 * @return the remaining time in milliseconds from the current timestamp to its expiration if the
	 * cooldown has already expired returns a negative number representing the milliseconds since its
	 * expiration
	 */
	public long getRemainingTime() {
		return duration.toMillis() - (System.currentTimeMillis() - startTimeMillis);
	}

	/**
	 * @return the {@link Duration} associated with this delay
	 */
	public Duration getDuration() {
		return duration;
	}

	/**
	 * @return the percentage of the cooldown that has elapsed, as a value between 0 and 100.
	 */
	public double getElapsedPercentage() {
		long elapsedMillis = System.currentTimeMillis() - startTimeMillis;
		long totalMillis = duration.toMillis();
		if (totalMillis <= 0) {
			return 100.0;
		}
		return Math.min(100.0, Math.max(0.0, (elapsedMillis / (double) totalMillis) * 100));
	}

	@Override
	public String toString() {
		return "Cooldown{" + "duration=" + duration.toMillis() + ", startTime=" + startTimeMillis + '}';
	}
}
