package com.wonkglorg.util.converter.date;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class DateConverter {
	private static final Pattern pattern = Pattern.compile("(\\d+)([A-Za-z]+)");
	private static final Comparator<DateType> comparatorBiggestTimeFirst =
			Comparator.comparingLong(DateType::getMilliseconds).reversed();

	private DateConverter() {

	}


	/**
	 * Converts milliseconds into a human-readable time format, all dateTypes added will be
	 * displayed.
	 *
	 * @param timeInMs The time in milliseconds
	 * @param formats all dateTypes to be displayed
	 * @return Formatted time string
	 */
	public static String toTimeString(long timeInMs, DateType... formats) {
		List<DateType> dateList = Arrays.stream(formats).sorted(comparatorBiggestTimeFirst).toList();

		StringBuilder sb = new StringBuilder();
		for (DateType dateType : dateList) {
			long value = timeInMs / dateType.getMilliseconds();
			timeInMs %= dateType.getMilliseconds();

			if (value > 0) {
				sb.append(value).append(dateType.getPreset()).append(" ");
			}
		}
		return sb.toString().trim();
	}

	/**
	 * @param timeInMs The time in milliseconds
	 * @param formats all dateTypes to be displayed
	 * @return Time Map containing all specified dateTypes and their amount
	 */
	public static Map<DateType, Long> toTimeMap(long timeInMs, DateType... formats) {
		Map<DateType, Long> timeMap = new EnumMap<>(DateType.class);
		List<DateType> dateList = Arrays.stream(formats).sorted(comparatorBiggestTimeFirst).toList();

		for (DateType dateType : dateList) {
			long value = timeInMs / dateType.getMilliseconds();
			timeInMs %= dateType.getMilliseconds();

			if (value > 0) {
				timeMap.put(dateType, value);
			}
		}
		return timeMap;
	}

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
				if (suffix.endsWith(dateType.getPreset())) {
					timeInMs += value * dateType.getMilliseconds();
					break;
				}
			}

		}
		return timeInMs;
	}


	/**
	 * Converts milliseconds into a human-readable time format
	 *
	 * @param timeInMs The time in milliseconds
	 * @return Formatted time string
	 */
	public static String toTimeString(long timeInMs) {
		return toTimeString(timeInMs, DateType.values());
	}

}
