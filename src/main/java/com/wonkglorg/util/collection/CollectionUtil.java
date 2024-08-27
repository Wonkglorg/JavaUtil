package com.wonkglorg.util.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class CollectionUtil {
	private CollectionUtil() {
		//
	}

	/**
	 * Removes duplicates from a Collection of values based on provided fields
	 *
	 * @param rows the rows to check
	 * @param merge behaviour when encountering duplicates
	 * @param fields the fields that should be unique
	 * @return a list representing the unique values
	 */
	@SafeVarargs
	private <T> List<T> reduceDuplicates(Collection<T> rows, BinaryOperator<T> merge,
			Function<T, Object>... fields) {
		if (rows == null) {
			return List.of();
		}

		Map<String, T> uniqueEntries =
				rows.stream().collect(Collectors.toMap(row -> generateKey(row, fields), row -> row,
						merge));

		return new ArrayList<>(uniqueEntries.values());
	}

	/**
	 * Removes duplicates from a Collection of values based on provided fields keeps the first
	 * instance found and discards all other duplicates
	 *
	 * @param rows the rows to check
	 * @param fields the fields that should be unique
	 * @return a list representing the unique values
	 */
	@SafeVarargs
	private <T> List<T> reduceDuplicates(Collection<T> rows, Function<T, Object>... fields) {
		return reduceDuplicates(rows, (existing, replacement) -> existing, fields);
	}

	/**
	 * Generates a unique key for the {@link #reduceDuplicates(Collection, Function[])}method}
	 *
	 * @param row the object to generate a key for
	 * @param fields the fields to validate uniqueness for
	 * @return a unique key represented by the required values
	 */
	private static <T> String generateKey(T row, Function<T, Object>[] fields) {
		return Arrays.stream(fields).map(f -> {
			Object value = f.apply(row);
			return value != null ? value.toString() : "null";
		}).collect(Collectors.joining("-"));
	}
}
