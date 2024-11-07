package com.wonkglorg.util.console;

public enum ConsoleTextStyle {
    RESET("\033[0m"),
    BOLD("\033[1m"),
    DIM("\033[2m"),
    ITALIC("\033[3m"),
    UNDERLINE("\033[4m"),
    BLINK("\033[5m"),
    INVERSE("\033[7m"),
    HIDDEN("\033[8m"),
    STRIKETHROUGH("\033[9m"),
    ;

    private final String code;

    ConsoleTextStyle(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}
