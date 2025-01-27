package com.wonkglorg.util.collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.Map;

class OrderedMapTest{
	@Test
	void canSortAutomatically() {
		OrderedMap<String, Integer> map = new OrderedMap<>(Map.Entry.<String, Integer>comparingByValue());
		
		map.put("C", 3);
		map.put("D", 4);
		map.put("A", 1);
		map.put("G", 7);
		map.put("I", 9);
		map.put("K", 11);
		map.put("B", 2);
		map.compute("G", (key, value) -> value + 1);
		map.merge("G",-1,Integer::sum);
		map.putIfAbsent("L", 12);

		assertEquals("B=2", map.get(1).toString());
		assertEquals("[A=1, B=2, C=3, D=4, G=7, I=9, K=11, L=12]", map.toString());
		assertEquals("[A=1, B=2, C=3, D=4, G=7, I=9, K=11, L=12]", map.entrySet().toString());
		assertEquals("[A, B, C, D, G, I, K, L]",map.keySet().toString());
		assertEquals("[1, 2, 3, 4, 7, 9, 11, 12]",map.values().toString());
	}
	
	@Test
	void canSortAutomaticallyByKeys() {
		OrderedMap<String, Integer> map = new OrderedMap<>(Map.Entry.<String, Integer>comparingByKey());
		
		map.put("C", 3);
		map.put("A", 1);
		map.put("B", 2);
		
		assertEquals("B=2", map.get(1).toString());
		assertEquals("[A=1, B=2, C=3]", map.toString());
	}
}
