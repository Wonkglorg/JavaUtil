package com.wonkglorg.util.test;

import com.wonkglorg.util.string.StringUtils;

import java.util.List;
import java.util.Map;

public class TimingReport {
	private final String name;
	private final Map<Long, Timing> timings;

	public TimingReport(final String name, Map<Long, Timing> timings) {
		this.name = name;
		this.timings = timings;
	}


	public String getName() {
		return name;
	}

	public Map<Long, Timing> getTimings() {
		return timings;
	}

	public double getAverageExecutionTime() {
		return (double) timings.values().stream().mapToLong(Timing::duration).reduce(0L, Long::sum)
				/ timings.size();
	}

	public long getShortestExecutionTime() {
		return timings.values().stream().mapToLong(Timing::duration).min().getAsLong();
	}


	public long getLongestExecutionTime() {
		return timings.values().stream().mapToLong(Timing::duration).max().getAsLong();
	}

	public long getTotalDuration() {
		return timings.values().stream().mapToLong(Timing::duration).sum();
	}

	public static void printTimeReport(List<TimingReport> reports) {
		//todo:jmd implement
	}

	public record Timing(long iteration, long duration) {
		@Override
		public String toString() {
			return StringUtils.format("Timing{iteration={0}ns, duration={1}ns", iteration, duration);
		}
	}


	public void printReport() {
		//todo:jmd create class to format record values easier.
	}


	@Override
	public String toString() {
		return StringUtils.format(
				"TimingReport{name={0}, min={1}ns, average={2}ns, max={3}ns, duration={4}ns, timings={5}}",
				name, getShortestExecutionTime(), getAverageExecutionTime(), getLongestExecutionTime(),
				getTotalDuration(), timings);
	}
}
