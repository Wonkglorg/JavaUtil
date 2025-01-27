package com.wonkglorg.util.collection;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderedMapTest {
	@Test
	void canSortAutomatically() {
		OrderedMap<String, Long> map =
				new OrderedMap<>(Map.Entry.<String, Long>comparingByValue());

		map.put("K", 11L);
		map.put("C", 3L);
		map.put("D", 4L);
		map.put("A", 1L);
		map.put("G", 7L);
		map.put("I", 9L);
		map.put("B", 2L);
		map.compute("G", (key, value) -> value + 1);
		map.merge("G", -1L, Long::sum);
		map.putIfAbsent("L", 12L);

		assertEquals("B=2", map.get(1).toString());
		assertEquals("[A=1, B=2, C=3, D=4, G=7, I=9, K=11, L=12]", map.toString());
		assertEquals("[A=1, B=2, C=3, D=4, G=7, I=9, K=11, L=12]", map.entrySet().toString());
		assertEquals("[A, B, C, D, G, I, K, L]", map.keySet().toString());
		assertEquals("[1, 2, 3, 4, 7, 9, 11, 12]", map.values().toString());
	}

	@Test
	void canSortAutomaticallyByKeys() {
		OrderedMap<String, Integer> map =
				new OrderedMap<>(Map.Entry.<String, Integer>comparingByKey());

		map.put("C", 3);
		map.put("A", 1);
		map.put("B", 2);

		assertEquals("B=2", map.get(1).toString());
		assertEquals("[A=1, B=2, C=3]", map.toString());
	}

	@Test
	void canSortOtherTypes() {
		OrderedMap<UUID, Long> allXp = new OrderedMap<UUID, Long>(Map.Entry.comparingByValue());
		allXp.put(UUID.randomUUID(), 939L);
		allXp.put(UUID.randomUUID(), 0L);
		allXp.put(UUID.randomUUID(), 1L);

		for(Map.Entry<UUID, Long> entry : allXp.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}


	}
}
