package com.wonkglorg.util.random;


import java.util.ArrayList;
import java.util.Collections;
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
	 * @param elements element
	 * @return element
	 */
	public static <T> T randomElement(List<T> elements) {
		if (elements.isEmpty()) {
			return null;
		}
		if (elements.size() == 1) {
			return elements.get(0);
		}
		return elements.get(ThreadLocalRandom.current().nextInt(elements.size()));
	}


	/**
	 * Get random elements from a list.
	 *
	 * @param elements
	 * @param count
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> randomElement(List<T> elements, int count) {
		return randomElement(elements, count, false);
	}

	/**
	 * Get random elements from a list, if unique is set to true returns the entire list if the
	 * specified count is bigger than the list containing elements, when an empty list or null is
	 * inserted return an empty list.
	 *
	 * @param elements the elements to pick from
	 * @param count the amount
	 * @param unique if the elements should be unique
	 * @param <T>
	 * @return list of elements picked from the provided Collection
	 */
	public static <T> List<T> randomElement(final List<T> elements, int count, boolean unique) {
		if (elements == null || elements.isEmpty() || count <= 0) {
			return Collections.emptyList();
		}

		List<T> elementEntries = new ArrayList<>(elements);

		if (unique) {
			if (count >= elementEntries.size()) {
				Collections.shuffle(elementEntries);
				return elementEntries;
			}
			Collections.shuffle(elementEntries);
			return new ArrayList<>(elementEntries.subList(0, count));
		}

		List<T> outputElements = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			outputElements.add(
					elementEntries.get(ThreadLocalRandom.current().nextInt(elementEntries.size())));
		}

		return outputElements;
	}
}