package com.wonkglorg.util.converter.date;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TimeBuilderNew {
	/**
	 * Pattern to match any valid int + datatype pair checks for numbers or decimal numbers followed
	 * by any amount of spaces and uppercase / lowercase letters
	 */
	private static final Pattern PATTERN = Pattern.compile("(\\d+(?:[.,]\\d+)?)\\s*([a-zA-Zµ]+)");
	private static final Comparator<DateType> COMPARATOR_BIGGEST_TIME_FIRST =
			Comparator.comparingLong(DateType::getMilliseconds).reversed();
	private static final Set<DateType> allTypes =
			Arrays.stream(DateType.values()).collect(Collectors.toSet());
	private static final Map<Set<DateType>, List<DateType>> cachedTypes = new ConcurrentHashMap<>();

	private long nanos;
	private long seconds;


	private TimeBuilderNew(BigInteger nanoTime) {
		this.nanoTime = nanoTime;
	}

	public static TimeBuilderNew create(BigInteger time, DateType type) {
		return new TimeBuilderNew(time.mu);
	}
	
	
	public static TimeBuilderNew create(String timeString){
		return new TimeBuilderNew();
	}
	
	
	public long toMillies(){
		return  nanoTime.longValue();
	}
	
	
	

}
