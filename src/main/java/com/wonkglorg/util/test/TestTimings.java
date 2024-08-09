package com.wonkglorg.util.test;

import com.wonkglorg.util.interfaces.functional.TriConsumer;
import com.wonkglorg.util.interfaces.functional.TriFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class TestTimings {


	//todo:jmd add a way to test multiple files against eachother? with a report on the results how
	// they match up
	//perhaps with a check what type it is?

	private record RunFunction(String name, Object function, Object... objects) {

	}

	private List<TimingReport> time(List<RunFunction> functions) {

		Map<String, TimingReport> reports = new HashMap<>();

		for (RunFunction runFunction : functions) {
			if (runFunction.function instanceof Function<?, ?> function) {
				reports.put(runFunction.name, time(function, runFunction.objects));
			}
			//todo:jmd if its not a valid function return an empty Time report stating its not a runnable executeable
			// otherwise return a list of all reports in order then have a static method to format all time reports
		}

	}


	private static <R> TimingReport timeFunctionBase(String name, Function<Object[], R> function,
			long repeats, Object... args) {
		Map<Long, TimingReport.Timing> map = new HashMap<>();
		for (long i = 0; i < repeats; i++) {
			map.put(i, timerBase(() -> function.apply(args), i));
		}
		return new TimingReport(name, map);
	}

	private static TimingReport timeConsumerBase(String name, Consumer<Object[]> function,
			long repeats, Object... args) {
		Map<Long, TimingReport.Timing> map = new HashMap<>();
		for (long i = 0; i < repeats; i++) {
			map.put(i, timerBase(() -> {
				function.accept(args);
				return null;
			}, i));
		}
		return new TimingReport(name, map);
	}

	public static <T, R> TimingReport time(String name, Function<T, R> function, T t, long repeats) {
		return timeFunctionBase(name, args -> function.apply((T) args[0]), repeats, t);
	}

	public static <T, U, R> TimingReport time(String name, BiFunction<T, U, R> function, T t, U u,
			long repeats) {
		return timeFunctionBase(name, args -> function.apply((T) args[0], (U) args[1]), repeats, t, u);
	}

	public static <T, U, V, R> TimingReport time(String name, TriFunction<T, U, V, R> function, T t,
			U u, V v, long repeats) {
		return timeFunctionBase(name, args -> function.apply((T) args[0], (U) args[1], (V) args[2]),
				repeats, t, u, v);
	}

	public static <T> TimingReport time(String name, Consumer<T> consumer, T t, long repeats) {
		return timeConsumerBase(name, args -> consumer.accept((T) args[0]), repeats, t);
	}

	public static <T, U> TimingReport time(String name, BiConsumer<T, U> consumer, T t, U u,
			long repeats) {
		return timeConsumerBase(name, args -> consumer.accept((T) args[0], (U) args[1]), repeats, t,
				u);
	}

	public static <T, U, V> TimingReport time(String name, TriConsumer<T, U, V> consumer, T t, U u,
			V v, long repeats) {
		return timeConsumerBase(name, args -> consumer.accept((T) args[0], (U) args[1], (V) args[2]),
				repeats, t, u, v);
	}

	public static <T> TimingReport time(String name, Supplier<T> producer, long repeats) {
		return timeConsumerBase(name, args -> producer.get(), repeats);
	}

	private static <R> TimingReport.Timing timerBase(Supplier<R> function, long iteration) {
		long start = System.currentTimeMillis();
		R ignored = function.get();
		long end = System.currentTimeMillis();
		return new TimingReport.Timing(iteration, end - start);
	}


}
