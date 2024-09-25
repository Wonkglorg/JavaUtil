package com.wonkglorg.util.converter.date;

import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ConverterDate {
	private static final Pattern pattern = Pattern.compile("(\\d+)([A-Za-z]+)");
	private static final Comparator<DateType> comparatorBiggestTimeFirst =
			Comparator.comparingLong(DateType::getMilliseconds).reversed();

	private ConverterDate() {
		//Utility class
	}

	/**
	 * Converts nanoseconds into a human-readable time format, all dateTypes added will be
	 * displayed.
	 *
	 * @param durationNano The time in nanoseconds
	 * @param formats all dateTypes to be displayed
	 * @return Formatted time string
	 * @throws ArithmeticException if numeric overflow occurs
	 */
	public static String toTimeString(Duration durationNano, DateType... formats) {
		List<DateType> dateList = Arrays.stream(formats).sorted(comparatorBiggestTimeFirst).toList();

		StringBuilder sb = new StringBuilder();
		long nanoTime = durationNano.toNanos();
		for (DateType dateType : dateList) {
			long nanoDateType = dateType.getDuration().toNanos();
			long value = nanoTime / nanoDateType;
			nanoTime %= nanoDateType;

			if (value > 0) {
				sb.append(value).append(dateType.getPostfix()).append(" ");
			}
		}
		return sb.toString().trim();
	}

	/**
	 * Converts milliseconds into a human-readable time format
	 *
	 * @param durationNano The time in nanoseconds
	 * @return Formatted time string
	 * @throws ArithmeticException if numeric overflow occurs
	 */
	public static String toTimeString(Duration durationNano) {
		return toTimeString(durationNano, DateType.values());
	}

	/**
	 * Converts milliseconds into a human-readable time format
	 *
	 * @param timeInMs The time in milliseconds
	 * @return Formatted time string
	 */
	public static String toTimeString(long timeInMs) {
		return toTimeString(timeInMs, false, false, DateType.values());
	}

	//todo:jmd repurpose this one to handle all conversions, more general than the rest as it gives
	// me all info back

	//todo:jmd change to a builder pattern
	/**
	 * @param timeInMs The time in milliseconds
	 * @param formats all dateTypes to be displayed
	 * @return Time Map containing all specified dateTypes and their amount
	 */
	public static Map<DateType, Long> toTimeMap(long time, Function<DateType, Long> timeConversion,
			boolean forceAllValues, boolean restToDecimal, DateType... formats) {
		Map<DateType, Long> timeMap = new EnumMap<>(DateType.class);
		List<DateType> dateList = Arrays.stream(formats).sorted(comparatorBiggestTimeFirst).toList();

		for (DateType dateType : dateList) {
			long conversionTypeValue = timeConversion.apply(dateType);
			long value = time / conversionTypeValue;
			time %= conversionTypeValue;

			if (value > 0 || forceAllValues) {
				timeMap.put(dateType, value);
			}
		}
		return timeMap;
	}

	//todo:jmd add a way to also go for decimal places if leftovers are present?

	/**
	 * Converts a time string into millisecond time. Example : 10d 5m 8s 10ms
	 *
	 * @param timeString time string to be converted
	 * @return millisecond representation of the input string
	 */
	public static long toMillis(String timeString) {
		long timeInMs = 0;
		if (timeString == null || timeString.isEmpty()) {
			return 0;
		}

		Matcher matcher = pattern.matcher(timeString);
		while (matcher.find()) {
			long value = Long.parseLong(matcher.group(1));
			String suffix = matcher.group(2);

			for (DateType dateType : DateType.values()) {
				if (suffix.endsWith(dateType.getPostfix())) {
					timeInMs += value * dateType.getMilliseconds();
					break;
				}
			}

		}
		return timeInMs;
	}

	/**
	 * Converts a time string into nanoseconds time. Example : 10d 5m 8s 10ms
	 *
	 * @param timeString time string to be converted
	 * @return nano representation of the input string
	 * @throws ArithmeticException if numeric overflow occurs
	 */
	public static long toNanos(String timeString) {
		long timeInNano = 0;
		if (timeString == null || timeString.isEmpty()) {
			return 0;
		}

		Matcher matcher = pattern.matcher(timeString);
		while (matcher.find()) {
			long value = Long.parseLong(matcher.group(1));
			String suffix = matcher.group(2);

			for (DateType dateType : DateType.values()) {
				if (suffix.endsWith(dateType.getPostfix())) {
					timeInNano += value * dateType.getDuration().toNanos();
					break;
				}
			}

		}
		return timeInNano;
	}


	/**
	 * Converts milliseconds into a human-readable time format, all dateTypes added will be
	 * displayed.
	 *
	 * @param timeInMs The time in milliseconds
	 * @param formats all dateTypes to be displayed
	 * @return Formatted time string
	 */
	public static String toTimeString(long timeInMs, boolean useFullName, boolean forceAllTypes,
			DateType... formats) {
		return convertTimeToString(timeInMs, DateType::getMilliseconds, useFullName, forceAllTypes,
				formats);
	}

	/**
	 * Utility Method to converting a variety of time durations to its appropriate time format
	 *
	 * @param time the time to convert
	 * @param timeConversion the datatype value to compare, should be the same format as the input
	 * time
	 * @param useFullNames weather or not to use its prefix or full name representation
	 * @param forceAllTypes forces all types specified to show up even if the time isn't large enough
	 * @param formats the formats to show
	 * @return a converted string (example: 1000 -> 1s)
	 */
	private static String convertTimeToString(long time, Function<DateType, Long> timeConversion,
			boolean useFullNames, boolean forceAllTypes, DateType... formats) {
		List<DateType> dateList = Arrays.stream(formats).sorted(comparatorBiggestTimeFirst).toList();

		StringBuilder sb = new StringBuilder();
		for (DateType dateType : dateList) {
			long dateTypeTime = timeConversion.apply(dateType);
			if (dateTypeTime <= 0) {
				//value too small can't work with
				continue;
			}
			long value = time / dateTypeTime;
			time %= dateTypeTime;

			if (value > 0 || forceAllTypes) {
				sb.append("%d%s ".formatted(value,
						useFullNames ? dateType.getFullName() : dateType.getPostfix()));
			}
		}
		return sb.toString().trim();
	}


	/*
	private static long convertStringToTime(String timeString, boolean useFullName,
			Function<DateType, String> matcherValue) {
		if (timeString == null || timeString.isBlank()) {
			return 0;
		}

		long convertedTime = 0;

		Matcher matcher = pattern.matcher(timeString);
		while (matcher.find()) {
			long value = Long.parseLong(matcher.group(1));
			String suffix = matcher.group(2);

			for (DateType dateType : DateType.values()) {
				if (suffix.equalsIgnoreCase(dateType.getPostfix())) {
					timeInNano += value * dateType.getDuration().toNanos();
					break;
				}
			}

		}
		return timeInNano;

	}

	 */


}
