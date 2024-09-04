package com.wonkglorg.util.console;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static com.wonkglorg.util.console.ConsoleUtil.readInput;

public class ConsoleUtilTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a number: ");
        int num = readInput(Integer.class);


    }
}
