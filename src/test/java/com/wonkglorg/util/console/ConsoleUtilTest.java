package com.wonkglorg.util.console;

import com.wonkglorg.util.ip.IPv4;
import com.wonkglorg.util.ip.IPv6;

import java.util.regex.Pattern;

import static com.wonkglorg.util.console.ConsoleUtil.println;

public class ConsoleUtilTest {

    public static void main(String[] args) {
        println("Enter a number: ");
        int number = ConsoleInput.of(Integer.class).get(0);
        println("You entered: " + number);

        println("Enter a char: ");
        char character = ConsoleInput.of(Character.class).get('a');
        println("You entered: " + character);

        println("Enter an IPv4: ");
        IPv4 ip = ConsoleInput.of(IPv4.class).get(IPv4.Min);
        println("You entered: " + ip);

        println("Enter a IPv6: ");
        IPv6 ip6 = ConsoleInput.of(IPv6.class).get(IPv6.Min);
        println("You entered: " + ip6);


        String test = ConsoleInput.of(String.class, "Enter a string: ")
                .errorMessage("Invalid input try again: ")
                .matchesPattern(Pattern.compile("[a-zA-Z0-9]{6}"), "Invalid input, max 6 characters a-Z, 0-9")
                .prompt("Enter a string new: ")
                .get("default");

        println("You entered: " + test);

    }


}
