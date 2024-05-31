package com.wonkglorg.util.database.exceptions;

public class IncorrectTypeConversionException extends Exception {
    private final String fieldName;
    private final Class<?> type;
    private final String message;
    private final Throwable cause;

    public IncorrectTypeConversionException(String message, String fieldName, Class<?> type, Throwable cause) {
        super(message);
        this.fieldName = fieldName;
        this.type = type;
        this.message = message;
        this.cause = cause;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class<?> getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getCause() {
        return cause;
    }


}
