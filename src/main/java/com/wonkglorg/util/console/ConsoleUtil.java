package com.wonkglorg.util.console;

import com.wonkglorg.util.ip.IPv4;
import com.wonkglorg.util.ip.IPv6;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ConsoleUtil {
    private ConsoleUtil() {
    }

    private static Scanner scanner = new Scanner(System.in);

    private static final Map<Class<?>, Function<String, Object>> converterMappings = new HashMap<>();

    //Default implemented Converter mappings
    static {
        converterMappings.put(Integer.class, Integer::parseInt);
        converterMappings.put(Double.class, Double::parseDouble);
        converterMappings.put(String.class, String::valueOf);
        converterMappings.put(Long.class, Long::parseLong);
        converterMappings.put(Float.class, Float::parseFloat);
        converterMappings.put(Boolean.class, booleanParser());
        converterMappings.put(IPv4.class, IPv4::new);
        converterMappings.put(IPv6.class, IPv6::new);
        converterMappings.put(Character.class, charParser());
    }


    /**
     * Reads a value from the console and returns matching type
     *
     * @param error error message to be displayed if the input is invalid
     * @param type  desired type
     * @param <T>   type of the value to be returned
     * @return value of the desired type
     */
    public static synchronized <T> T readInput(Class<T> type, String error) {
        checkIfConverterExists(type);
        T value;
        while (true) {
            try {
                String input = scanner.nextLine();
                value = type.cast(convert(input, type));
                break;
            } catch (Exception ignored) {
                System.out.println(error);
            }
        }

        return value;
    }

    /**
     * Reads a value from the console and returns matching type, with a default error message if the input is invalid
     *
     * @param type desired type
     * @param <T>  type of the value to be returned
     * @return value of the desired type
     */
    public static synchronized <T> T readInput(Class<T> type) {
        return readInput(type, "Invalid input, please try again!");
    }

    /**
     * Reads a value from the console and returns matching type, with a specified error message if the input is invalid
     *
     * @param converter function to convert the input to the desired type (must throw an error on mismatch)
     * @param <T>       type of the value to be returned
     * @return value of the desired type
     */
    public static synchronized <T> T readInput(Function<String, T> converter, String errorMessage) {
        T value;
        while (true) {
            try {
                String input = scanner.nextLine();
                value = converter.apply(input);
                break;
            } catch (Exception ignored) {
                System.out.println(errorMessage);
            }
        }

        return value;
    }

    /**
     * Reads a value from the console and returns matching type, with a default error message if the input is invalid
     *
     * @param converter function to convert the input to the desired type (must throw an error on mismatch)
     * @param <T>       type of the value to be returned
     * @return value of the desired type
     */
    public static synchronized <T> T readInput(Function<String, T> converter) {
        return readInput(converter, "Invalid input, please try again!");
    }


    /**
     * Reads a value matching a pattern from the console if the input is invalid, it will display the error message
     *
     * @param error   error message to be displayed if the input is invalid
     * @param pattern pattern to match the input
     * @return value matching the pattern
     */
    public static synchronized String readInputWithPattern(String error, Pattern pattern) {
        String value;
        while (true) {
            try {
                String input = scanner.nextLine();
                if (pattern.matcher(input).matches()) {
                    value = input;
                    break;
                } else {
                    System.out.println(error);
                }
            } catch (Exception ignored) {
                System.out.println(error);
            }
        }

        return value;

    }

    /**
     * Checks if a converter exists for the given type
     *
     * @param type type to check
     * @throws IllegalArgumentException if the type is not supported
     */
    private static void checkIfConverterExists(Class<?> type) {
        if (!converterMappings.containsKey(type)) {
            throw new IllegalArgumentException("No converter found for type: " + type.getName());
        }
    }

    /**
     * Converts the input string to the desired type if present
     *
     * @param input input string to be converted
     * @param type  desired type
     * @return converted object
     * @throws IllegalArgumentException if the type is not supported
     */
    private static @NotNull Object convert(@NotNull String input, Class<?> type) {
        if (converterMappings.containsKey(type)) {
            return converterMappings.get(type).apply(input);
        }
        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }

    /**
     * Adds a new mapping to convert a datatype to another
     *
     * @param type
     * @param converter
     */
    public static void addObjectMapping(Class<?> type, Function<String, Object> converter) {
        converterMappings.put(type, converter);
    }

    /**
     * Removes a mapping to convert a datatype to another
     *
     * @param type
     */
    public static void removeObjectMapping(Class<?> type) {
        converterMappings.remove(type);
    }

    /**
     * @return unmodifiable map of the mappings
     */
    public static Map<Class<?>, Function<String, Object>> getConverterMappings() {
        return Collections.unmodifiableMap(converterMappings);
    }

    /**
     * Closes the scanner to prevent resource leaks
     */
    public static void closeScanner() {
        scanner.close();
    }

    /**
     * Gets the scanner object associated with the console
     *
     * @return
     */
    public static Scanner getScanner() {
        return scanner;
    }

    /**
     * Sets the input stream for the scanner
     *
     * @param inputStream input stream to be set
     */
    public static void setScannerInput(InputStream inputStream) {
        Locale locale = scanner.locale();
        Pattern pattern = scanner.delimiter();
        scanner.close();
        scanner = new Scanner(inputStream);
        scanner.useLocale(locale);
        scanner.useDelimiter(pattern);
    }


    public static void addNewLines(int count) {
        for (int i = 0; i < count; i++) {
            System.out.println();
        }
    }

    /**
     * Opens the scanner to read input if the previous scanner has been closed
     */
    public static void openScanner() {
        scanner = new Scanner(System.in);
    }

    /**
     * Opens the scanner to read input from the input stream
     *
     * @param inputStream
     */
    public static void openScanner(InputStream inputStream) {
        setScannerInput(inputStream);
    }

    //-----------------------------------------------Parsers----------------------------------------------------------
    private static Function<String, Object> booleanParser() {
        return str -> {
            switch (str.toLowerCase()) {
                case "true", "1", "yes", "y", "t":
                    return true;
                case "false", "0", "no", "n", "f":
                    return false;
            }
            throw new IllegalArgumentException("Invalid boolean");
        };
    }

    private static Function<String, Object> charParser() {
        return s -> {
            if (s.length() != 1) {
                throw new IllegalArgumentException("Invalid character");
            }
            return s.charAt(0);
        };
    }
}
