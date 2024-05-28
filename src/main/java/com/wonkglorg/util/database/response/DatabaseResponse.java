package com.wonkglorg.util.database.response;

public class DatabaseResponse {
    private final Exception exception;

    public DatabaseResponse(Exception exception) {
        this.exception = exception;
    }

    public boolean hasError() {
        return exception != null;
    }

    public Exception getException() {
        return exception;
    }
}
