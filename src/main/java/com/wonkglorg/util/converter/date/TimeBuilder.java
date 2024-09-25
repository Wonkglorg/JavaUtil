package com.wonkglorg.util.converter.date;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TimeBuilder {
	private static final Pattern PATTERN = Pattern.compile("(\\d+)\\s*([A-Za-z]+)");
	private static final Comparator<DateType> COMPARATOR_BIGGEST_TIME_FIRST =
			Comparator.comparingLong(DateType::getMilliseconds).reversed();
	protected Function<DateType, Long> timeConversion = DateType::getMilliseconds;
	protected final Set<DateType> allTypes =
			Arrays.stream(DateType.values()).collect(Collectors.toSet());
	protected Set<DateType> formats = new HashSet<>();

	public static TimeToStringBuilder toTimeString() {
		return new TimeToStringBuilder();
	}

	public static TimeFromStringBuilder fromTimeString(String timeString) {
		return new TimeFromStringBuilder(timeString);
	}

	public static class TimeToStringBuilder extends TimeBuilder {
		private static final Comparator<DateType> comparatorBiggestTimeFirst =
				Comparator.comparingLong(DateType::getMilliseconds).reversed();
		protected long time = 0L;
		protected boolean forceAllValues = false;
		protected boolean restToDecimal = false;
		protected boolean trimTrailingDecimalZeros = true;
		protected int maxDecimalsToShow = 2;
		protected boolean useFullName = false;
		protected boolean capitalizeFirstLetter = true;
		protected Set<DateType> dateTypes = new HashSet<>();
		private final Set<DateType> allTypes =
				Arrays.stream(DateType.values()).collect(Collectors.toSet());


		public TimeToStringBuilder input(long time, Function<DateType, Long> timeConversion) {
			this.time = time;
			this.timeConversion = timeConversion;
			return this;
		}

		public TimeToStringBuilder inputNanos(long nanoseconds) {
			this.time = nanoseconds;
			return this;
		}


		public TimeToStringBuilder inputMillie(long millieSeconds) {
			this.time = millieSeconds;
			timeConversion = DateType::getMilliseconds;
			return this;
		}

		public TimeToStringBuilder inputSeconds(long seconds) {
			this.time = seconds;
			timeConversion = DateType::getSeconds;
			return this;
		}

		public TimeToStringBuilder showRestAsDecimal() {
			this.restToDecimal = true;
			return this;
		}

		public TimeToStringBuilder showRestAsDecimal(int maxDecimalsToShow) {
			this.restToDecimal = true;
			this.maxDecimalsToShow = maxDecimalsToShow;
			return this;
		}

		public TimeToStringBuilder showRestAsDecimal(boolean trimTrailingDecimalZeros) {
			this.restToDecimal = true;
			this.trimTrailingDecimalZeros = trimTrailingDecimalZeros;
			return this;
		}

		public TimeToStringBuilder showRestAsDecimal(int maxDecimalsToShow,
				boolean trimTrailingDecimalZeros) {
			this.restToDecimal = true;
			this.trimTrailingDecimalZeros = trimTrailingDecimalZeros;
			this.maxDecimalsToShow = maxDecimalsToShow;
			return this;
		}

		public TimeToStringBuilder useFullName(boolean capitalizeFirstLetter) {
			this.useFullName = true;
			this.capitalizeFirstLetter = capitalizeFirstLetter;
			return this;
		}

		public TimeToStringBuilder forceShowAllTypes() {
			this.forceAllValues = true;
			return this;
		}

		public TimeToStringBuilder typesToShow(DateType... types) {
			dateTypes.addAll(Arrays.asList(types));
			return this;
		}

		public String build() {
			return convertTimeToString(time, timeConversion, useFullName, capitalizeFirstLetter,
					forceAllValues, restToDecimal, dateTypes.isEmpty() ? allTypes : dateTypes);
		}


		@SuppressWarnings("MalformedFormatString")
		private String convertTimeToString(long time, Function<DateType, Long> timeConversion,
				boolean useFullNames, boolean capitalizeFirstLetter, boolean forceAllTypes,
				boolean restToDecimal, Set<DateType> formats) {
			List<DateType> dateList = formats.stream().sorted(comparatorBiggestTimeFirst).toList();

			StringBuilder sb = new StringBuilder();
			boolean isLastDateType;

			for (int i = 0; i < dateList.size(); i++) {
				DateType dateType = dateList.get(i);
				long dateTypeTime = timeConversion.apply(dateType);

				if (dateTypeTime <= 0) {
					if (forceAllTypes) {
						String name = timePostfix(dateType, 0, useFullNames, capitalizeFirstLetter);
						sb.append("%d%s ".formatted(0, name));
					}
					continue; // Skip if value is too small
				}

				long value = time / dateTypeTime;
				time %= dateTypeTime;

				// weather or not this is the last type in the list
				isLastDateType = (i == dateList.size() - 1);

				if (value > 0 || forceAllTypes) {
					String name = timePostfix(dateType, value, useFullNames, capitalizeFirstLetter);
					sb.append("%d%s ".formatted(value, name));

				}

				// Handle remainder to decimal for the last element
				if (restToDecimal && time > 0 && isLastDateType) {
					double decimalValue = (double) time / dateTypeTime;
					String name = timePostfix(dateType, 3, useFullNames, capitalizeFirstLetter);
					sb.append("%s%s".formatted(
							formatDecimal(decimalValue, maxDecimalsToShow, trimTrailingDecimalZeros), name));
				}
			}

			return sb.toString().

					trim();
		}

		public Map<DateType, Double> toTimeMap() {
			Map<DateType, Double> timeMap = new EnumMap<>(DateType.class);
			List<DateType> dateList;
			if (formats.isEmpty()) {
				dateList = allTypes.stream().sorted(comparatorBiggestTimeFirst).toList();
			} else {
				dateList = formats.stream().sorted(comparatorBiggestTimeFirst).toList();
			}
			boolean isLastDateType;
			for (int i = 0; i < dateList.size(); i++) {
				DateType dateType = dateList.get(i);
				long dateTypeTime = timeConversion.apply(dateType);
				double value = time / dateTypeTime;
				time %= dateTypeTime;

				if (value > 0 || forceAllValues) {
					timeMap.put(dateType, value);
				}
				isLastDateType = (i == dateList.size() - 1);

				if (restToDecimal && time > 0 && isLastDateType) {
					double decimalValue = (double) time / dateTypeTime;
					timeMap.put(dateType, decimalValue);
				}
			}
			return timeMap;
		}

		private String formatDecimal(double decimalValue, int maxDecimalPlaces,
				boolean trimTrailingZeros) {

			BigDecimal bigDecimal =
					BigDecimal.valueOf(decimalValue).setScale(maxDecimalPlaces, RoundingMode.HALF_UP);
			if (trimTrailingZeros) {
				return bigDecimal.stripTrailingZeros().toPlainString();
			}
			return bigDecimal.toPlainString();
		}

		private String timePostfix(DateType type, long value, boolean useFullName,
				boolean capitalizeFirstLetter) {
			if (!useFullName) {
				return type.getPostfix();
			}
			String name = " " + type.getFullName();

			if (!capitalizeFirstLetter) {
				name = name.toLowerCase();
			}
			if (value > 1) {
				name = name + "s";
			}

			return name;
		}
	}

	public static class TimeFromStringBuilder extends TimeBuilder {
		private String timeString;

		public TimeFromStringBuilder(String timeString) {
			this.timeString = timeString;
		}

		public TimeFromStringBuilder input(String timeString) {
			this.timeString = timeString;
			return this;
		}

		/**
		 * Converts the valid String to Millieseconds
		 *
		 * @throws ArithmeticException if numeric overflow occurs
		 */
		public long toMilliseconds() {
			return convertTo(DateType::getMilliseconds);
		}

		/**
		 * Converts the valid String to Seconds
		 *
		 * @throws ArithmeticException if numeric overflow occurs
		 */
		public long toSeconds() {
			return convertTo(DateType::getSeconds);
		}

		private long convertTo(Function<DateType, Long> conversion) {
			long time = 0;
			if (timeString == null || timeString.isBlank()) {
				return 0;
			}

			Matcher matcher = PATTERN.matcher(timeString);
			while (matcher.find()) {
				long value = Long.parseLong(matcher.group(1));
				String suffix = matcher.group(2);

				for (DateType dateType : DateType.values()) {
					if (suffix.equals(dateType.getPostfix()) || suffix.equalsIgnoreCase(
							dateType.getFullName()) || suffix.equalsIgnoreCase(dateType.getFullName() + "s")) {
						time += value * conversion.apply(dateType);
						break;
					}
				}

			}
			return time;
		}

		public Map<DateType, Double> toTimeMap(boolean forceAllValues) {
			long time = toMilliseconds();
			Map<DateType, Double> timeMap = new EnumMap<>(DateType.class);
			List<DateType> dateList;
			if (formats.isEmpty()) {
				dateList = allTypes.stream().sorted(COMPARATOR_BIGGEST_TIME_FIRST).toList();
			} else {
				dateList = formats.stream().sorted(COMPARATOR_BIGGEST_TIME_FIRST).toList();
			}

			for (DateType dateType : dateList) {
				long conversionTypeValue = timeConversion.apply(dateType);
				//this is intended
				double value = time / conversionTypeValue;
				time %= conversionTypeValue;

				if (value > 0 || forceAllValues) {
					timeMap.put(dateType, value);
				}
			}
			return timeMap;
		}


	}


}
