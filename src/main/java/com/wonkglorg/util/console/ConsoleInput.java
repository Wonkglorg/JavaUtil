package com.wonkglorg.util.console;

import com.wonkglorg.util.ip.IPv4;
import com.wonkglorg.util.ip.IPv6;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Class to manage and modify different inputs from places
 * @param <T>
 */
@SuppressWarnings("unchecked, unused")
public class ConsoleInput<T> {

    private static final String DEFAULT_ERROR_MESSAGE = "Invalid input try again: ";
    private static final Map<Class<?>, Function<String, Object>> converterMappings = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    private final Class<T> type;
    private String prompt;
    private String errorMessage;
    private Pattern pattern;
    private String patternErrorMessage;
    private Predicate<String> consideredForDefault = String::isEmpty;
    private Function<String, Object> customConverter;

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

    private ConsoleInput(Class<T> type, String prompt, String errorMessage) {
        this.type = type;
        this.prompt = prompt;
        this.errorMessage = errorMessage;
    }

    public static <T> ConsoleInput<T> of(Class<T> type) {
        checkIfConverterExists(type);
        return new ConsoleInput<>(type, null, DEFAULT_ERROR_MESSAGE);
    }

    public static <T> ConsoleInput<T> of(Class<T> type, String prompt) {
        checkIfConverterExists(type);
        return new ConsoleInput<>(type, prompt, DEFAULT_ERROR_MESSAGE);
    }

    public static <T> ConsoleInput<T> of(Class<T> type, String prompt, String errorMessage) {
        checkIfConverterExists(type);
        return new ConsoleInput<>(type, prompt, errorMessage);
    }

    public ConsoleInput<T> prompt(String prompt) {
        this.prompt = prompt;
        return this;
    }

    /**
     * Set the error message to display when the input is invalid
     *
     * @param errorMessage The error message to display
     * @return The ConsoleInput instance
     */
    public ConsoleInput<T> errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * Set a pattern to match the input against (happens before conversion)
     *
     * @param pattern The pattern to match the input against
     * @return The ConsoleInput instance
     */
    public ConsoleInput<T> matchesPattern(Pattern pattern, String patternErrorMessage) {
        this.patternErrorMessage = patternErrorMessage;
        this.pattern = pattern;
        return this;
    }

    /**
     * Set a predicate to determine if the input should be considered as return for default value
     *
     * @param consideredForDefault The predicate to determine if the input should be considered as the default value
     * @return The ConsoleInput instance
     */
    public ConsoleInput<T> whenDefault(Predicate<String> consideredForDefault) {
        this.consideredForDefault = consideredForDefault;
        return this;
    }

    /**
     * modifies the default input
     *
     * @param scanner the new scanner to use
     * @return The ConsoleInput instance
     */
    public ConsoleInput<T> setScanner(Scanner scanner) {
        ConsoleInput.scanner = scanner;
        return this;
    }

    public T get(T defaultValue) {
        if (prompt != null) {
            System.out.print(prompt);
        }

        boolean hasDefaultValue = defaultValue != null;
        boolean hasPattern = pattern != null;
        boolean hasCustomConverter = customConverter != null;

        while (true) {
            try {
                String input = scanner.nextLine();

                if (hasDefaultValue && consideredForDefault.test(input)) return defaultValue;

                if (hasPattern && !pattern.matcher(input).matches()) {
                    System.out.println(patternErrorMessage);
                    continue;
                }

                if (hasCustomConverter) {
                    return (T) customConverter.apply(input);
                }

                return (T) converterMappings.get(type).apply(input);
            } catch (Exception e) {
                System.out.println(errorMessage);
            }
        }
    }

    public T get() {
        return get(null);
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

    public static Scanner getScanner() {
        return scanner;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getPatternErrorMessage() {
        return patternErrorMessage;
    }

    public Predicate<String> getConsideredForDefault() {
        return consideredForDefault;
    }

    public Function<String, Object> getCustomConverter() {
        return customConverter;
    }
}
