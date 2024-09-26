package com.wonkglorg.util.console;

import org.junit.jupiter.api.Test;

class ConsoleColorTest {

	@Test
	void testColors() {
		for (ConsoleColor color : ConsoleColor.values()) {
			System.out.println(color + color.name() + ConsoleColor.RESET + " ");
		}
	}
}
