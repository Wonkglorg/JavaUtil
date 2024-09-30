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

@SuppressWarnings({"unchecked", "unused"})
public class TestTimings {


    //todo:jmd add a way to test multiple files against eachother? with a report on the results how
    // they match up
    // perhaps with a check what type it is?

    public record RunFunction(String name, Object function, Object... objects) {
    }


    /**
     * Times a list of functions
     *
     * @param functions the functions to be timed
     * @param repeats   the number of times to repeat the functions
     * @return a list of timing reports
     */
    public List<TimingReport> time(List<RunFunction> functions, long repeats) {

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
                reports.add(time(runFunction.name, (Supplier<Object>) function, repeats));
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

    //todo:jmd add checks and better returns for errors, to showcase what elements would be invalid
    // etc
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

    /**
     * Times a function with a single argument
     *
     * @param name     the name of the function
     * @param function the function to be timed
     * @param t        the argument to be passed to the function
     * @param repeats  the number of times to repeat the function
     * @param <T>      the type of the argument
     * @param <R>      the return type of the function
     * @return a timing report
     */
    public static <T, R> TimingReport time(String name, Function<T, R> function,
                                           Supplier<T> t, long repeats) {
        return timeFunctionBase(name, args -> function.apply((T) args[0]), repeats,
                t.get());
    }

    /**
     * Times a function with two arguments
     *
     * @param name     the name of the function
     * @param function the function to be timed
     * @param t        the first argument to be passed to the function
     * @param u        the second argument to be passed to the function
     * @param repeats  the number of times to repeat the function
     * @param <T>      the type of the first argument
     * @param <U>      the type of the second argument
     * @param <R>      the return type of the function
     * @return a timing report
     */
    public static <T, U, R> TimingReport time(String name, BiFunction<T, U, R> function, Supplier<T> t, Supplier<U> u,
                                              long repeats) {
        return timeFunctionBase(name, args -> function.apply((T) args[0], (U) args[1]), repeats, t.get(), u.get());
    }

    /**
     * Times a function with three arguments
     *
     * @param name     the name of the function
     * @param function the function to be timed
     * @param t        the first argument to be passed to the function
     * @param u        the second argument to be passed to the function
     * @param v        the third argument to be passed to the function
     * @param repeats  the number of times to repeat the function
     * @param <T>      the type of the first argument
     * @param <U>      the type of the second argument
     * @param <V>      the type of the third argument
     * @param <R>      the return type of the function
     * @return a timing report
     */
    public static <T, U, V, R> TimingReport time(String name, TriFunction<T, U, V, R> function, Supplier<T> t,
                                                 Supplier<U> u, Supplier<V> v, long repeats) {
        return timeFunctionBase(name, args -> function.apply((T) args[0], (U) args[1], (V) args[2]),
                repeats, t.get(), u.get(), v.get());
    }

    /**
     * Times a consumer with a single argument
     *
     * @param name     the name of the function
     * @param consumer the consumer to be timed
     * @param t        the argument to be passed to the consumer
     * @param repeats   the number of times to repeat the consumer
     * @param <T>      the type of the argument
     * @return a timing report
     */
    public static <T> TimingReport time(String name, Consumer<T> consumer, Supplier<T> t, long repeats) {
        return timeConsumerBase(name, args -> consumer.accept((T) args[0]), repeats, t.get());
    }

    /**
     * Times a consumer with two arguments
     *
     * @param name     the name of the function
     * @param consumer the consumer to be timed
     * @param t        the first argument to be passed to the consumer
     * @param u        the second argument to be passed to the consumer
     * @param repeats  the number of times to repeat the consumer
     * @param <T>      the type of the first argument
     * @param <U>      the type of the second argument
     * @return a timing report
     */
    public static <T, U> TimingReport time(String name, BiConsumer<T, U> consumer, Supplier<T> t, Supplier<U> u, long repeats) {
        return timeConsumerBase(name, args -> consumer.accept((T) args[0], (U) args[1]), repeats, t.get(), u.get());
    }

    /**
     * Times a consumer with three arguments
     *
     * @param name     the name of the function
     * @param consumer the consumer to be timed
     * @param t        the first argument to be passed to the consumer
     * @param u        the second argument to be passed to the consumer
     * @param v        the third argument to be passed to the consumer
     * @param repeats  the number of times to repeat the consumer
     * @param <T>      the type of the first argument
     * @param <U>      the type of the second argument
     * @param <V>      the type of the third argument
     * @return a timing report
     */
    public static <T, U, V> TimingReport time(String name, TriConsumer<T, U, V> consumer, Supplier<T> t, Supplier<U> u, Supplier<V> v, long repeats) {
        return timeConsumerBase(name, args -> consumer.accept((T) args[0], (U) args[1], (V) args[2]),
                repeats, t.get(), u.get(), v.get());
    }

    /**
     * Times a supplier
     *
     * @param name     the name of the function
     * @param producer the supplier to be timed
     * @param repeats  the number of times to repeat the supplier
     * @param <T>      the return type of the supplier
     * @return a timing report
     */
    public static <T> TimingReport time(String name, Supplier<T> producer, long repeats) {
        return timeConsumerBase(name, args -> producer.get(), repeats);
    }

    /**
     * Same as its static counterpart but for the {@link #time(List, long)}
     */
    private TimingReport time(String name, Function<Object, Object> function, long repeats,
                              Object[] objects) {
        return timeFunctionBase(name, args -> function.apply(args[0]), repeats, objects);
    }

    /**
     * Same as its static counterpart but for the {@link #time(List, long)}
     */
    private TimingReport time(String name, BiFunction<Object, Object, Object> function, long repeats,
                              Object[] objects) {
        return timeFunctionBase(name, args -> function.apply(args[0], args[1]), repeats, objects);
    }

    /**
     * Same as its static counterpart but for the {@link #time(List, long)}
     */
    private TimingReport time(String name, TriFunction<Object, Object, Object, Object> function,
                              long repeats, Object[] objects) {
        return timeFunctionBase(name, args -> function.apply(args[0], args[1], args[2]), repeats,
                objects);
    }

    /**
     * Same as its static counterpart but for the {@link #time(List, long)}
     */
    private TimingReport time(String name, Consumer<Object> function, long repeats,
                              Object[] objects) {
        return timeConsumerBase(name, args -> function.accept(args[0]), repeats, objects);
    }

    /**
     * Same as its static counterpart but for the {@link #time(List, long)}
     */
    private TimingReport time(String name, BiConsumer<Object, Object> function, long repeats,
                              Object[] objects) {
        return timeConsumerBase(name, args -> function.accept(args[0], args[1]), repeats, objects);
    }

    /**
     * Same as its static counterpart but for the {@link #time(List, long)}
     */
    private TimingReport time(String name, TriConsumer<Object, Object, Object> function,
                              long repeats,
                              Object[] objects) {
        return timeConsumerBase(name, args -> function.accept(args[0], args[1], args[2]), repeats,
                objects);
    }

    private static <R> TimingReport.Timing timerBase(Supplier<R> function, long iteration) {
        long start = System.nanoTime();
        R ignored = function.get();
        long end = System.nanoTime();
        return new TimingReport.Timing(iteration, end - start);
    }


}
