package com.wonkglorg.util.random;


import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Random.
 */
@SuppressWarnings("unused")
public final class Random {

    /**
     * Returns an Integer between a and b
     *
     * @param a first number
     * @param b second number
     * @return number between a and b
     */
    public static int getNumberBetween(int a, int b) {
        if (a == b) {
            return a;
        }
        int min = Math.min(a, b);
        int max = Math.max(a, b);
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    /**
     * Returns a Double between a and b
     *
     * @param a first number
     * @param b second number
     * @return number between a and b
     */
    public static double getNumberBetween(double a, double b) {
        if (a == b) {
            return a;
        }
        double min = Math.min(a, b);
        double max = Math.max(a, b);
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    /**
     * Returns a Long between a and b
     *
     * @param a first number
     * @param b second number
     * @return number between a and b
     */
    public static long getNumberBetween(long a, long b) {
        if (a == b) {
            return a;
        }
        long min = Math.min(a, b);
        long max = Math.max(a, b);
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    /**
     * Get random element from list
     *
     * @param element element
     * @return element
     */
    public static <T> T randomElement(List<T> element) {
        if (element.isEmpty()) {
            return null;
        }
        if (element.size() == 1) {
            return element.get(0);
        }
        return element.get(ThreadLocalRandom.current().nextInt(element.size()));
    }

}