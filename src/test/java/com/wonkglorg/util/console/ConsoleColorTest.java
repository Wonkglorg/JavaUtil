package com.wonkglorg.util.console;

import org.junit.jupiter.api.Test;

public class ConsoleColorTest {

    @Test
    public void testColors() {
        for (ConsoleColor color : ConsoleColor.values()) {
            System.out.println(color + color.name() + ConsoleColor.RESET + " ");
        }
    }
}
