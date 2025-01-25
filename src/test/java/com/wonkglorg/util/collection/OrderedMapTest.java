package com.wonkglorg.util.collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.Map;

class OrderedMapTest{
	@Test
	void canSortAutomatically() {
		OrderedMap<String, Integer> map = new OrderedMap<>(Map.Entry.<String, Integer>comparingByValue());
		
		map.put("C", 3);
		map.put("A", 1);
		map.put("B", 2);
		
		assertEquals("B=2", map.get(1).toString());
		assertEquals("[A=1, B=2, C=3]", map.toString());
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
