package com.wonkglorg.util.database.response;

import java.sql.ResultSet;

public class DatabaseResultSetResponse extends DatabaseResponse {
    private final ResultSet resultSet;

    public DatabaseResultSetResponse(Exception exception, ResultSet resultSet) {
        super(exception);
        this.resultSet = resultSet;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }


}
