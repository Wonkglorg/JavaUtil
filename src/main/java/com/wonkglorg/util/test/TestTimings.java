package com.wonkglorg.util.test;

import com.wonkglorg.util.interfaces.functional.TriConsumer;
import com.wonkglorg.util.interfaces.functional.TriFunction;

import java.util.ArrayList;
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

	private List<TimingReport> time(List<RunFunction> functions, long repeats) {

		List<TimingReport> reports = new ArrayList<>();

		for (RunFunction runFunction : functions) {

			if (runFunction.function instanceof Function<?, ?> function) {
				reports.add(time(runFunction.name, (Function<Object, Object>) function, repeats,
						runFunction.objects));
			}

			if (runFunction.function instanceof BiFunction<?, ?, ?> function) {
				reports.add(time(runFunction.name, (BiFunction<Object, Object, Object>) function, repeats,
						runFunction.objects));
			}

			if (runFunction.function instanceof TriFunction<?, ?, ?, ?> function) {
				reports.add(
						time(runFunction.name, (TriFunction<Object, Object, Object, Object>) function, repeats,
								runFunction.objects));
			}

			if (runFunction.function instanceof Consumer<?> function) {
				reports.add(
						time(runFunction.name, (Consumer<Object>) function, repeats, runFunction.objects));
			}

			if (runFunction.function instanceof BiConsumer<?, ?> function) {
				reports.add(time(runFunction.name, (BiConsumer<Object, Object>) function, repeats,
						runFunction.objects));
			}

			if (runFunction.function instanceof TriConsumer<?, ?, ?> function) {
				reports.add(time(runFunction.name, (TriConsumer<Object, Object, Object>) function, repeats,
						runFunction.objects));
			}

			if (runFunction.function instanceof Supplier<?> function) {
				reports.add(timerWild(runFunction.name, (Supplier<Object>) function, repeats));
			}
		}

		return reports;
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

	private TimingReport time(String name, Function<Object, Object> function, long repeats,
			Object[] objects) {
		return timeFunctionBase(name, args -> function.apply(args[0]), repeats, objects);
	}

	public static <T, R> TimingReport time(String name, Function<T, R> function, T t, long repeats) {
		return timeFunctionBase(name, args -> function.apply((T) args[0]), repeats, t);
	}

	private TimingReport time(String name, BiFunction<Object, Object, Object> function, long repeats,
			Object[] objects) {
		return timeFunctionBase(name, args -> function.apply(args[0], args[1]), repeats, objects);
	}

	public static <T, U, R> TimingReport time(String name, BiFunction<T, U, R> function, T t, U u,
			long repeats) {
		return timeFunctionBase(name, args -> function.apply((T) args[0], (U) args[1]), repeats, t, u);
	}

	private TimingReport time(String name, TriFunction<Object, Object, Object, Object> function,
			long repeats, Object[] objects) {
		return timeFunctionBase(name, args -> function.apply(args[0], args[1], args[2]), repeats,
				objects);
	}

	public static <T, U, V, R> TimingReport time(String name, TriFunction<T, U, V, R> function, T t,
			U u, V v, long repeats) {
		return timeFunctionBase(name, args -> function.apply((T) args[0], (U) args[1], (V) args[2]),
				repeats, t, u, v);
	}

	private TimingReport time(String name, Consumer<Object> function, long repeats,
			Object[] objects) {
		return timeConsumerBase(name, args -> function.accept(args[0]), repeats, objects);
	}

	public static <T> TimingReport time(String name, Consumer<T> consumer, T t, long repeats) {
		return timeConsumerBase(name, args -> consumer.accept((T) args[0]), repeats, t);
	}

	private TimingReport time(String name, BiConsumer<Object, Object> function, long repeats,
			Object[] objects) {
		return timeConsumerBase(name, args -> function.accept(args[0], args[1]), repeats, objects);
	}

	public static <T, U> TimingReport time(String name, BiConsumer<T, U> consumer, T t, U u,
			long repeats) {
		return timeConsumerBase(name, args -> consumer.accept((T) args[0], (U) args[1]), repeats, t,
				u);
	}

	private TimingReport time(String name, TriConsumer<Object, Object, Object> function,
			long repeats,
			Object[] objects) {
		return timeConsumerBase(name, args -> function.accept(args[0], args[1], args[2]), repeats,
				objects);
	}

	public static <T, U, V> TimingReport time(String name, TriConsumer<T, U, V> consumer, T t, U u,
			V v, long repeats) {
		return timeConsumerBase(name, args -> consumer.accept((T) args[0], (U) args[1], (V) args[2]),
				repeats, t, u, v);
	}

	private TimingReport timerWild(String name, Supplier<Object> function, long repeats) {
		return timeConsumerBase(name, args -> function.get(), repeats);
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
