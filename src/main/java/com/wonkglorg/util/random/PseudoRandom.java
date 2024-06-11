package com.wonkglorg.util.random;

import java.awt.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A pseudo-random number generator.
 * <p>
 * This class is a simple implementation of a Linear Congruential Generator (LCG).
 * <p>
 * Should not be used for cryptographic purposes.
 * <p>
 * Main usage is for generating a lot of random numbers quickly.
 */
@SuppressWarnings("unused")
public class PseudoRandom {

    /**
     * Seed for the random number generator.
     */
    private static AtomicLong seed = new AtomicLong(System.currentTimeMillis());

    /**
     * Generates a random color.
     *
     * @return A random color.
     */
    public static Color randomColor() {
        return new Color(nextRandomByte(), nextRandomByte(), nextRandomByte());
    }

    /**
     * Generates a pseudo-random double using a Linear Congruential Generator (LCG).
     *
     * @return A pseudo-random number in the range [0, 1).
     */
    public static double nextRandomDouble() {
        seed.set((seed.get() * 1664525 + 1013904223) & 0xFFFFFFFFL);
        return (double) (seed.get() & 0xFFFFFF) / (1 << 24);
    }

    /**
     * Generates a pseudo-random integer using a Linear Congruential Generator (LCG).
     *
     * @return A pseudo-random number in the range [0, 2^24).
     */
    public static int nextRandomInt() {
        return (int) (nextRandomDouble() * (1 << 24));
    }

    /**
     * Generates a pseudo-random byte using a Linear Congruential Generator (LCG).
     *
     * @return A pseudo-random number in the range [0, 256).
     */
    public static byte nextRandomByte() {
        return (byte) (nextRandomInt() % 256);
    }

    /**
     * Generates a pseudo-random long using a Linear Congruential Generator (LCG).
     *
     * @return A pseudo-random number in the range [0, 2^24).
     */
    public static long nextRandomLong() {
        return (long) (nextRandomDouble() * (1 << 24));
    }

    /**
     * Generates a pseudo-random short using a Linear Congruential Generator (LCG).
     *
     * @return A pseudo-random number in the range [0, 2^24).
     */
    public static short nextRandomShort() {
        return (short) (nextRandomInt() & 0xFFFF);
    }

    /**
     * Generates a pseudo-random float using a Linear Congruential Generator (LCG).
     *
     * @return A pseudo-random number in the range [0, 1).
     */
    public static float nextRandomFloat() {
        return (float) nextRandomDouble();
    }

    /**
     * Generates a pseudo-random boolean using a Linear Congruential Generator (LCG).
     *
     * @return A pseudo-random boolean.
     */
    public static boolean nextRandomBoolean() {
        return nextRandomInt() % 2 == 0;
    }

    /**
     * Generates a pseudo-random double in the range [min, max).
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return A pseudo-random number in the range [min, max).
     */
    public static double nextRandomDouble(double min, double max) {
        return min + (max - min) * nextRandomDouble();
    }

    /**
     * Generates a pseudo-random integer in the range [min, max).
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return A pseudo-random number in the range [min, max).
     */
    public static int nextRandomInt(int min, int max) {
        return min + (int) ((max - min) * nextRandomDouble());
    }

    /**
     * Generates a pseudo-random byte in the range [min, max).
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return A pseudo-random number in the range [min, max).
     */
    public static byte nextRandomByte(byte min, byte max) {
        return (byte) (min + (int) ((max - min) * nextRandomDouble()));
    }

    /**
     * Generates a pseudo-random long in the range [min, max).
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return A pseudo-random number in the range [min, max).
     */
    public static long nextRandomLong(long min, long max) {
        return min + (long) ((max - min) * nextRandomDouble());
    }

    /**
     * Generates a pseudo-random short in the range [min, max).
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return A pseudo-random number in the range [min, max).
     */
    public static short nextRandomShort(short min, short max) {
        return (short) (min + (int) ((max - min) * nextRandomDouble()));
    }

    /**
     * Generates a pseudo-random float in the range [min, max).
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return A pseudo-random number in the range [min, max).
     */
    public static float nextRandomFloat(float min, float max) {
        return min + (max - min) * nextRandomFloat();
    }

    /**
     * Sets the seed for the random number generator.
     *
     * @param seed the seed
     */
    public static void setSeed(long seed) {
        PseudoRandom.seed.set(seed);
    }
}
