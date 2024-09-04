package com.wonkglorg.util.console;

import com.wonkglorg.util.ip.IPv4;
import com.wonkglorg.util.ip.IPv6;

import static com.wonkglorg.util.console.ConsoleUtil.readInput;

public class ConsoleUtilTest {

    public static void main(String[] args) {
        System.out.println("Enter a number: ");
        int number = readInput(Integer.class, 0);
        System.out.println("You entered: " + number);

        System.out.println("Enter a char: ");
        char character = readInput(Character.class, 'a');
        System.out.println("You entered: " + character);

        System.out.println("Enter an IPv4: ");
        IPv4 ip = readInput(IPv4.class, IPv4.Min);
        System.out.println("You entered: " + ip);

        System.out.println("Enter a IPv6: ");
        IPv6 ip6 = readInput(IPv6.class, IPv6.Min);
        System.out.println("You entered: " + ip6);
    }
}
