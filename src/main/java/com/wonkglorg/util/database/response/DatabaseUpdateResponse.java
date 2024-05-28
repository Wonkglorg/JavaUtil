package com.wonkglorg.util.database.response;

public class DatabaseUpdateResponse extends DatabaseResponse {
    private final int response;

    public DatabaseUpdateResponse(Exception exception, int response) {
        super(exception);
        this.response = response;
    }

    public int getResponse() {
        return response;
    }
}
