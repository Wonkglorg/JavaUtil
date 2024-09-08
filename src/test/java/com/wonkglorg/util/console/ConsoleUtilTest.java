package com.wonkglorg.util.console;

import com.wonkglorg.util.ip.IPv4;
import com.wonkglorg.util.ip.IPv6;

import static com.wonkglorg.util.console.ConsoleUtil.*;

public class ConsoleUtilTest {

    public static void main(String[] args) {
        println("Enter a number: ");
        int number = readInput(Integer.class, 0);
        println("You entered: " + number);

        println("Enter a char: ");
        char character = readInput(Character.class, 'a');
        println("You entered: " + character);

        println("Enter an IPv4: ");
        IPv4 ip = readInput(IPv4.class, IPv4.Min);
        println("You entered: " + ip);

        println("Enter a IPv6: ");
        IPv6 ip6 = readInput(IPv6.class, IPv6.Min);
        println("You entered: " + ip6);
    }
}
