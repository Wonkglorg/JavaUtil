package com.wonkglorg.util.console;

import com.wonkglorg.util.ip.IPv4;
import com.wonkglorg.util.ip.IPv6;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ConsoleUtil {

    private static final Logger logger = Logger.getLogger(ConsoleUtil.class.getName());
    private static Scanner scanner = new Scanner(System.in);
    private static final String defaultError = "Invalid input, please try again!";

    private static final Map<Class<?>, Function<String, Object>> converterMappings = new HashMap<>();

    private ConsoleUtil() {
    }

    //Default implemented Converter mappings
    static {
        converterMappings.put(Integer.class, Integer::parseInt);
        converterMappings.put(Double.class, Double::parseDouble);
        converterMappings.put(String.class, String::valueOf);
        converterMappings.put(Long.class, Long::parseLong);
        converterMappings.put(Float.class, Float::parseFloat);
        converterMappings.put(Boolean.class, booleanParser());
        converterMappings.put(IPv4.class, IPv4::of);
        converterMappings.put(IPv6.class, IPv6::of);
        converterMappings.put(Character.class, charParser());
        converterMappings.put(Byte.class, Byte::parseByte);
        converterMappings.put(Path.class, Path::of);
    }

    /**
     * Reads a value from the console and returns matching type
     *
     * @param type         desired type
     * @param defaultValue default value to return if the input is invalid
     * @param <T>          type of the value to be returned
     * @return value of the desired type
     */
    public static synchronized <T> T readInput(Class<T> type, T defaultValue) {
        return readInput(type, defaultValue, "", defaultError);
    }

    /**
     * Reads a value from the console and returns matching type
     *
     * @param error error message to be displayed if the input is invalid
     * @param type  desired type
     * @param <T>   type of the value to be returned
     * @return value of the desired type
     */
    public static synchronized <T> T readInput(Class<T> type, String message, String error) {
        checkIfConverterExists(type);
        if (message != null && !message.isEmpty()) {
            println(message);
        }

        T value;
        while (true) {
            try {
                String input = scanner.nextLine();
                value = type.cast(convert(input, type));
                break;
            } catch (Exception e) {
                if (error != null && !error.isEmpty()) println(error);
            }
        }

        return value;
    }

    public static synchronized <T> T readInput(Class<T> type, T defaultValue, String message, String error) {
        checkIfConverterExists(type);
        if (message != null && !message.isEmpty()) {
            println(message);
        }
        try {
            String input = scanner.nextLine();
            return type.cast(convert(input, type));
        } catch (Exception e) {
            if (error != null && !error.isEmpty()) println(error);
        }


        return defaultValue;
    }


    /**
     * Reads a value from the console and returns matching type, with a default error message if the
     * input is invalid
     *
     * @param type desired type
     * @param <T>  type of the value to be returned
     * @return value of the desired type
     */
    public static synchronized <T> T readInput(Class<T> type) {
        return readInput(type, "", "Invalid input, please try again!");
    }

    /**
     * Reads a value from the console and returns matching type
     *
     * @param converter    function to convert the input to the desired type (must throw an error on mismatch)
     * @param defaultValue default value to return if the input is invalid
     * @param <T>          type of the value to be returned
     * @return value of the desired type
     */
    public static synchronized <T> T readInput(Function<String, T> converter, T defaultValue, String message, String error) {
        T value;
        if (message != null && !message.isEmpty()) println(message);
        try {
            String input = scanner.nextLine();
            return converter.apply(input);
        } catch (Exception e) {
            if (error != null && !error.isEmpty()) println(error);
            return defaultValue;
        }
    }

    /**
     * Reads a value from the console and returns matching type, with a specified error message if
     * the
     * input is invalid
     *
     * @param converter function to convert the input to the desired type (must throw an error on
     *                  mismatch)
     * @param <T>       type of the value to be returned
     * @return value of the desired type
     */
    public static synchronized <T> T readInput(Function<String, T> converter, String message, String error) {
        T value;
        if (message != null && !message.isEmpty()) println(message);
        while (true) {
            try {
                String input = scanner.nextLine();
                value = converter.apply(input);
                break;
            } catch (Exception e) {
                if (error != null && !error.isEmpty()) println(error);
            }
        }

        return value;
    }

    /**
     * Reads a value from the console and returns matching type, with a default error message if the
     * input is invalid
     *
     * @param converter function to convert the input to the desired type (must throw an error on
     *                  mismatch)
     * @param message   message to send
     * @param <T>       type of the value to be returned
     * @return value of the desired type
     */
    public static synchronized <T> T readInput(Function<String, T> converter, String message) {
        return readInput(converter, message, "Invalid input, please try again!");
    }

    /**
     * Reads a value from the console and returns matching type, with a default error message if the
     * input is invalid
     *
     * @param converter function to convert the input to the desired type (must throw an error on
     *                  mismatch)
     * @param <T>       type of the value to be returned
     * @return value of the desired type
     */
    public static synchronized <T> T readInput(Function<String, T> converter) {
        return readInput(converter, "", "Invalid input, please try again!");
    }


    /**
     * Reads a value matching a pattern from the console if the input is invalid, it will display the
     * error message
     *
     * @param error   error message to be displayed if the input is invalid
     * @param pattern pattern to match the input
     * @return value matching the pattern
     */
    public static synchronized String readInputWithPattern(Pattern pattern, String defaultValue, String message, String error) {
        String value;
        try {
            String input = scanner.nextLine();
            if (pattern.matcher(input).matches()) {
                return input;
            } else {
                println(error);
            }
        } catch (Exception ignored) {
            println(error);
        }


        return defaultValue;
    }

    /**
     * Reads a value matching a pattern from the console if the input is invalid, it will display the
     *
     * @param pattern pattern to match the input
     * @param message message to be displayed before prompting for input
     * @param error   error message to be displayed if the input is invalid
     * @return value matching the pattern
     */
    public static synchronized String readInputWithPattern(Pattern pattern, String message, String error) {
        String value;
        while (true) {
            try {
                String input = scanner.nextLine();
                if (pattern.matcher(input).matches()) {
                    value = input;
                    break;
                } else {
                    println(error);
                }
            } catch (Exception ignored) {
                println(error);
            }
        }

        return value;
    }

    /**
     * Reads a value matching a pattern from the console if the input is invalid, it will display the
     *
     * @param pattern pattern to match the input
     * @param message message to be displayed before prompting for input
     * @return value matching the pattern
     */
    public static synchronized String readInputWithPattern(Pattern pattern, String message) {
        return readInputWithPattern(pattern, message, defaultError);
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
    private static Object convert(String input, Class<?> type) {
        if (converterMappings.containsKey(type)) {
            return converterMappings.get(type).apply(input);
        }
        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }

    /**
     * Adds a new mapping to convert a datatype to another
     *
     * @param type      the type to convert
     * @param converter the converter function
     */
    public static void addObjectMapping(Class<?> type, Function<String, Object> converter) {
        converterMappings.put(type, converter);
    }

    /**
     * Removes a mapping to convert a datatype to another
     *
     * @param type the datatype to remove
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
     * @return the scanner object associated with the console
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
            println();
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
     * @param inputStream the input stream
     */
    public static void openScanner(InputStream inputStream) {
        setScannerInput(inputStream);
    }


    /**
     * Prints text to the console
     *
     * @param text text to be printed
     */
    public static void print(Object... text) {
        for (Object t : text) {
            System.out.print(t);
        }
    }

    /**
     * Prints the text to the console with a new line
     *
     * @param text text to be printed
     */
    public static void println(Object... text) {
        if (text.length == 0) {
            System.out.println();
            return;
        }
        for (Object t : text) {
            System.out.println(t);
        }
    }

    /**
     * Sends an updatable message to the console, the message can be updated by sending a new message using {@link #printr(Object)}
     * <p>
     * To resume normal printing without removing this line use a new line character or call {@link #println(Object...)}
     * <p>
     * Only works if this method was used to print the text to update, and does not work if a new line has been
     * sent in between which is not updatable, works by sending a carriage return character to the console which
     * puts the cursor at the start of the line and overwrites the text
     *
     * @param text text to be printed
     */
    public static void printr(Object text) {
        System.out.print("\r" + text);
    }

    //------------------------------Parsers------------------------------------------
    private static Function<String, Object> booleanParser() {
        return str -> switch (str.toLowerCase()) {
            case "true", "1", "yes", "y", "t" -> true;
            case "false", "0", "no", "n", "f" -> false;
            default -> throw new IllegalArgumentException("Invalid boolean");
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

    //------------------------------Specialised------------------------------------------

    /**
     * Reads a {@link String} value from the console repeats until a valid boolean is entered
     *
     * @return entered value
     */
    public static synchronized String readString() {
        return readInput(String.class);
    }

    /**
     * Reads a {@link String} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @return entered value
     */
    public static synchronized String readString(String message) {
        return readInput(String.class, message);
    }

    /**
     * Reads a {@link String} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @param error   error message to be displayed if the input is invalid
     * @return entered value
     */
    public static synchronized String readString(String message, String error) {
        return readInput(String.class, message, error);
    }

    public static synchronized String readString(String defaultInput, String message, String error) {
        return readInput(String.class, defaultInput, message, error);
    }

    /**
     * Reads a {@link Boolean} value from the console repeats until a valid boolean is entered
     *
     * @return entered value
     */
    public static synchronized boolean readBoolean() {
        return readInput(Boolean.class);
    }

    /**
     * Reads a {@link Boolean} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @return entered value
     */
    public static synchronized boolean readBoolean(String message) {
        return readInput(Boolean.class, message, "Not a valid boolean try again!");
    }

    /**
     * Reads a {@link Boolean} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @param error   error message to be displayed if the input is invalid
     * @return entered value
     */
    public static synchronized boolean readBoolean(String message, String error) {
        return readInput(Boolean.class, message, error);
    }

    /**
     * Reads an {@link Integer} value from the console repeats until a valid boolean is entered
     *
     * @return entered value
     */
    public static synchronized int readInt() {
        return readInput(Integer.class);
    }

    /**
     * Reads a {@link Integer} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @return entered value
     */
    public static synchronized int readInt(String message) {
        return readInput(Integer.class, message, defaultError);
    }

    /**
     * Reads a {@link Integer} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @param error   error message to be displayed if the input is invalid
     * @return entered value
     */
    public static synchronized int readInt(String message, String error) {
        return readInput(Integer.class, message, error);
    }

    /**
     * Reads a {@link Double} value from the console repeats until a valid boolean is entered
     *
     * @return entered value
     */
    public static synchronized double readDouble() {
        return readInput(Double.class);
    }

    /**
     * Reads a {@link Double} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @return entered value
     */
    public static synchronized double readDouble(String message) {
        return readInput(Double.class, message, defaultError);
    }

    /**
     * Reads a {@link Double} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @param error   error message to be displayed if the input is invalid
     * @return entered value
     */
    public static synchronized double readDouble(String message, String error) {
        return readInput(Double.class, message, error);
    }

    /**
     * Reads a {@link Long} value from the console repeats until a valid boolean is entered
     *
     * @return entered value
     */
    public static synchronized long readLong() {
        return readInput(Long.class);
    }

    /**
     * Reads a {@link Long} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @return entered value
     */
    public static synchronized long readLong(String message) {
        return readInput(Long.class, message, defaultError);
    }

    /**
     * Reads a {@link Long} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @param error   error message to be displayed if the input is invalid
     * @return entered value
     */
    public static synchronized long readLong(String message, String error) {
        return readInput(Long.class, message, error);
    }

    /**
     * Reads a {@link Float} value from the console repeats until a valid boolean is entered
     *
     * @return entered value
     */
    public static synchronized float readFloat() {
        return readInput(Float.class);
    }

    /**
     * Reads a {@link Float} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @return entered value
     */
    public static synchronized float readFloat(String message) {
        return readInput(Float.class, message, defaultError);
    }

    /**
     * Reads a {@link Float} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @param error   error message to be displayed if the input is invalid
     * @return entered value
     */
    public static synchronized float readFloat(String message, String error) {
        return readInput(Float.class, message, error);
    }

    /**
     * Reads a {@link Byte} value from the console repeats until a valid boolean is entered
     *
     * @return entered value
     */
    public static synchronized byte readByte() {
        return readInput(Byte.class);
    }

    /**
     * Reads a {@link Byte} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @return entered value
     */
    public static synchronized byte readByte(String message) {
        return readInput(Byte.class, message, defaultError);
    }

    /**
     * Reads a {@link Byte} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @param error   error message to be displayed if the input is invalid
     * @return entered value
     */
    public static synchronized byte readByte(String message, String error) {
        return readInput(Byte.class, message, error);
    }

    /**
     * Reads a {@link Character} value from the console repeats until a valid boolean is entered
     *
     * @return entered value
     */
    public static synchronized char readChar() {
        return readInput(Character.class);
    }

    /**
     * Reads a {@link Character} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @return entered value
     */
    public static synchronized char readChar(String message) {
        return readInput(Character.class, message, defaultError);
    }

    /**
     * Reads a {@link Character} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @param error   error message to be displayed if the input is invalid
     * @return entered value
     */
    public static synchronized char readChar(String message, String error) {
        return readInput(Character.class, message, error);
    }

    /**
     * Reads a {@link Path} value from the console repeats until a valid boolean is entered
     *
     * @return entered value
     */
    public static synchronized Path readPath() {
        return readInput(Path.class);
    }

    /**
     * Reads a {@link Path} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @return entered value
     */
    public static synchronized Path readPath(String message) {
        return readInput(Path.class, message, defaultError);
    }

    /**
     * Reads a {@link Path} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @param error   error message to be displayed if the input is invalid
     * @return entered value
     */
    public static synchronized Path readPath(String message, String error) {
        return readInput(Path.class, message, error);
    }

    /**
     * Reads a boolean value from the console repeats until a valid boolean is entered
     *
     * @return entered value
     */
    public static synchronized IPv4 readIPv4() {
        return readInput(IPv4.class);
    }

    /**
     * Reads a {@link IPv4} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @return entered value
     */
    public static synchronized IPv4 readIPv4(String message) {
        return readInput(IPv4.class, message, defaultError);
    }

    /**
     * Reads a {@link IPv4} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @param error   error message to be displayed if the input is invalid
     * @return entered value
     */
    public static synchronized IPv4 readIPv4(String message, String error) {
        return readInput(IPv4.class, message, error);
    }

    /**
     * Reads a {@link IPv6} value from the console repeats until a valid boolean is entered
     *
     * @return entered value
     */
    public static synchronized IPv6 readIPv6() {
        return readInput(IPv6.class);
    }

    /**
     * Reads a {@link IPv6} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @return entered value
     */
    public static synchronized IPv6 readIPv6(String message) {
        return readInput(IPv6.class, message, defaultError);
    }

    /**
     * Reads a {@link IPv6} value from the console repeats until a valid boolean is entered
     *
     * @param message message to be displayed before prompting for input
     * @param error   error message to be displayed if the input is invalid
     * @return entered value
     */
    public static synchronized IPv6 readIPv6(String message, String error) {
        return readInput(IPv6.class, message, error);
    }


}
