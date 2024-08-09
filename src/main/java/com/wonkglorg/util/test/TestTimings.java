package com.wonkglorg.util.test;

import com.wonkglorg.util.interfaces.functional.BiSupplier;
import com.wonkglorg.util.interfaces.functional.TriConsumer;
import com.wonkglorg.util.interfaces.functional.TriFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class TestTimings {


	private static <R> TimingReport timeFunctionBase(Function<Object[], R> function, long repeats,
			Object... args) {
		Map<Long, TimingReport.Timing> map = new HashMap<>();
		for (long i = 0; i < repeats; i++) {
			map.put(i, timerBase(() -> function.apply(args), i));
		}
		return new TimingReport("", map);
	}

	private static TimingReport timeConsumerBase(Consumer<Object[]> function, long repeats,
			Object... args) {
		Map<Long, TimingReport.Timing> map = new HashMap<>();
		for (long i = 0; i < repeats; i++) {
			map.put(i, timerBase(() -> {
				function.accept(args);
				return null;
			}, i));
		}
		return new TimingReport("", map);
	}

	public static <T, R> TimingReport time(Function<T, R> function, T t, long repeats) {
		return timeFunctionBase(args -> function.apply((T) args[0]), repeats, t);
	}

	public static <T, U, R> TimingReport time(BiFunction<T, U, R> function, T t, U u, long repeats) {
		return timeFunctionBase(args -> function.apply((T) args[0], (U) args[1]), repeats, t, u);
	}

	public static <T, U, V, R> TimingReport time(TriFunction<T, U, V, R> function, T t, U u, V v,
			long repeats) {
		return timeFunctionBase(args -> function.apply((T) args[0], (U) args[1], (V) args[2]), repeats,
				t, u, v);
	}

	public static <T> TimingReport time(Consumer<T> consumer, T t, long repeats) {
		return timeConsumerBase(args -> consumer.accept((T) args[0]), repeats, t);
	}

	public static <T, U> TimingReport time(BiConsumer<T, U> consumer, T t, U u, long repeats) {
		return timeConsumerBase(args -> consumer.accept((T) args[0], (U) args[1]), repeats, t, u);
	}

	public static <T, U, V> TimingReport time(TriConsumer<T, U, V> consumer, T t, U u, V v,
			long repeats) {
		return timeConsumerBase(args -> consumer.accept((T) args[0], (U) args[1], (V) args[2]),
				repeats,
				t, u, v);
	}

	public static <T> TimingReport time(Supplier<T> producer, long repeats) {
		return timeConsumerBase(args -> producer.get(), repeats);
	}

	private static <R> TimingReport.Timing timerBase(BiSupplier<R> function, long iteration) {
		long start = System.currentTimeMillis();
		R ignored = function.get();
		long end = System.currentTimeMillis();
		return new TimingReport.Timing(iteration, end - start);
	}


}
