package com.wonkglorg.util.database;


import com.wonkglorg.util.database.response.*;
import com.wonkglorg.util.database.values.DbName;
import com.wonkglorg.util.database.values.DbPassword;
import com.wonkglorg.util.database.values.DbUrl;
import com.wonkglorg.util.database.values.DbUser;
import com.wonkglorg.util.interfaces.functional.checked.CheckedConsumer;
import com.wonkglorg.util.interfaces.functional.checked.CheckedFunction;

import java.sql.*;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * IMPORTANT! Please add the mysql Jconnector to the project if you want to use MySql, I did not include this myself to not inflate the libraries
 * size. groupId : mysql artifactId : mysql-connector-java
 */
@SuppressWarnings("unused")
public class MySqlDatabase extends Database {

    protected final DbUser USERNAME;
    protected final DbUrl URL;
    protected DbPassword PASSWORD;
    protected DbName DATABASE_NAME;
    private final BlockingQueue<Connection> connectionPool;

    public MySqlDatabase(DbUrl url, DbUser username, DbPassword password, DbName databasename, int poolSize) {
        super(DatabaseType.MYSQL);
        USERNAME = username;
        URL = url;
        PASSWORD = password;
        DATABASE_NAME = databasename;

        connectionPool = new ArrayBlockingQueue<>(poolSize);
        initializeConnectionPool(poolSize);
    }


    public MySqlDatabase(DbUrl url, DbUser username, DbPassword password, int poolSize) {
        this(url, username, password, null, poolSize);
    }

    public MySqlDatabase(DbUrl url, DbUser username, DbName databasename) {
        this(url, username, null, databasename, 5);
    }

    /**
     * @return a connection from the connection pool should be released after use manually
     */
    @Override
    public Connection getConnection() {
        try {
            return connectionPool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void releaseConnection(Connection connection) {
        connectionPool.offer(connection);
    }


    public void resizePool(int newSize) {
        if (newSize < 1) {
            throw new IllegalArgumentException("Pool size must be at least 1");
        }
        synchronized (connectionPool) {
            int currentSize = connectionPool.size();
            if (newSize < currentSize) {
                for (int i = newSize; i < currentSize; i++) {
                    try {
                        connectionPool.take().close();
                    } catch (SQLException | InterruptedException e) {
                        System.err.println("Error closing connection: " + e.getMessage());
                    }
                }
            } else if (newSize > currentSize) {
                for (int i = currentSize; i < newSize; i++) {
                    connectionPool.add(createConnection());
                }
            }
        }
    }


    @Override
    public void disconnect() {
        for (Connection connection : connectionPool) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }


    private void initializeConnectionPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            connectionPool.add(createConnection());
        }
    }


    public void useDatabase(Connection connection, DbName databaseName) {
        String name = sanitize(databaseName.toString());
        try (Statement statement = connection.createStatement()) {
            statement.execute("USE " + name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection createConnection() {
        try {
            Class.forName(getClassLoader());
            if (DATABASE_NAME == null) {
                return DriverManager.getConnection(getDriver() + "//" + URL, USERNAME.toString(), PASSWORD.toString());
            }
            return DriverManager.getConnection(getDriver() + "//" + URL + "/" + DATABASE_NAME, USERNAME.toString(), PASSWORD.toString());
        } catch (Exception e) {
            disconnect();
            throw new RuntimeException(e);
        }
    }


    @Override
    public void close() {
        disconnect();
    }

    @Override
    public DatabaseResponse execute(CheckedConsumer<Connection> query) {
        return executeUnchecked(query);
    }

    @Override
    public DatabaseUpdateResponse executeUpdate(CheckedFunction<Connection, Integer> query) {
        return executeUpdateUnchecked(query);
    }

    public DatabaseUpdateResponse executeUpdate(CheckedFunction<Connection, PreparedStatement> query, CheckedFunction<PreparedStatement, Integer> result) {
        Connection connection = getConnection();
        try (PreparedStatement resultSet = query.apply(connection)) {
            return new DatabaseUpdateResponse(null, result.apply(resultSet));
        } catch (Exception e) {
            return new DatabaseUpdateResponse(e, -1);
        } finally {
            releaseConnection(connection);
        }
    }

    @Override
    public DatabaseResultSetResponse executeQuery(CheckedFunction<Connection, ResultSet> query) {
        return executeQueryUnchecked(query);
    }

    @Override
    public DatabaseResultSetResponse executeQuery(CheckedFunction<Connection, PreparedStatement> query, CheckedFunction<PreparedStatement, ResultSet> result) {
        return executeQueryUnchecked(query, result);
    }

    @Override
    public <T> DatabaseObjResponse<T> executeObjQuery(CheckedFunction<Connection, List<T>> adapter) {
        return executeObjQueryUnchecked(adapter);
    }


    @Override
    public <T> DatabaseObjResponse<T> executeObjQuery(CheckedFunction<Connection, ResultSet> query, CheckedFunction<ResultSet, List<T>> adapter) {
        return executeObjQueryUnchecked(query, adapter);
    }

    public <T> DatabaseSingleObjResponse<T> executeSingleObjQuery(CheckedFunction<Connection, T> adapter) {
        return executeSingleObjQueryUnchecked(adapter);
    }

    @Override
    public <T> DatabaseSingleObjResponse<T> executeSingleObjQuery(CheckedFunction<Connection, ResultSet> query, CheckedFunction<ResultSet, T> adapter) {
        return executeSingleObjQueryUnchecked(query, adapter);
    }

    @Override
    public DatabaseResponse executeUnchecked(Consumer<Connection> query) {
        Connection connection = getConnection();
        try {
            query.accept(connection);
            return new DatabaseResponse(null);
        } catch (Exception e) {
            return new DatabaseResponse(e);
        } finally {
            releaseConnection(connection);
        }
    }

    @Override
    public DatabaseUpdateResponse executeUpdateUnchecked(Function<Connection, Integer> query) {
        Connection connection = getConnection();
        try {
            return new DatabaseUpdateResponse(null, query.apply(connection));
        } catch (Exception e) {
            return new DatabaseUpdateResponse(e, -1);
        } finally {
            releaseConnection(connection);
        }
    }

    @Override
    public DatabaseUpdateResponse executeUpdateUnchecked(Function<Connection, PreparedStatement> query, Function<PreparedStatement, Integer> result) {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = query.apply(connection);
            return new DatabaseUpdateResponse(null, result.apply(statement));
        } catch (Exception e) {
            return new DatabaseUpdateResponse(e, -1);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            releaseConnection(connection);
        }
    }

    @Override
    public DatabaseResultSetResponse executeQueryUnchecked(Function<Connection, ResultSet> query) {
        Connection connection = getConnection();
        try {
            return new DatabaseResultSetResponse(null, query.apply(connection));
        } catch (Exception e) {
            return new DatabaseResultSetResponse(e, null);
        } finally {
            releaseConnection(connection);
        }
    }

    @Override
    public DatabaseResultSetResponse executeQueryUnchecked(Function<Connection, PreparedStatement> query, Function<PreparedStatement, ResultSet> result) {
        Connection connection = getConnection();
        try (var statement = query.apply(connection)) {
            return new DatabaseResultSetResponse(null, result.apply(statement));
        } catch (Exception e) {
            return new DatabaseResultSetResponse(e, null);
        } finally {
            releaseConnection(connection);
        }
    }

    @Override
    public <T> DatabaseObjResponse<T> executeObjQueryUnchecked(Function<Connection, List<T>> query) {
        Connection connection = getConnection();
        try {
            return new DatabaseObjResponse<>(null, query.apply(connection));
        } catch (Exception e) {
            return new DatabaseObjResponse<>(e, null);
        } finally {
            releaseConnection(connection);
        }
    }

    /**
     * Execute a query and return a list of objects automatically closes all resources after execution (should not be done by the user)
     *
     * @param query   the query to execute
     * @param adapter the adapter to convert the result to a list of objects
     * @param <T>     the type of the object
     * @return a response containing the list of objects or an exception if an error occurred
     */
    public <T> DatabaseObjResponse<T> executeObjQueryUnchecked(Function<Connection, ResultSet> query, Function<ResultSet, List<T>> adapter) {
        Connection connection = getConnection();
        ResultSet resultSet = null;
        try {
            resultSet = query.apply(connection);

            List<T> results = adapter.apply(resultSet);
            return new DatabaseObjResponse<>(null, results);
        } catch (Exception e) {
            return new DatabaseObjResponse<>(e, null);
        } finally {
            closeResources(resultSet);
            releaseConnection(connection);
        }
    }

    @Override
    public <T> DatabaseSingleObjResponse<T> executeSingleObjQueryUnchecked(Function<Connection, T> adapter) {
        Connection connection = getConnection();
        try {
            return new DatabaseSingleObjResponse<>(null, adapter.apply(connection));
        } catch (Exception e) {
            return new DatabaseSingleObjResponse<>(e, null);
        } finally {
            releaseConnection(connection);
        }
    }

    /**
     * Execute a query and return a single object automatically closes all resources after execution (should not be done by the user)
     *
     * @param query   the query to execute
     * @param adapter the adapter to convert the result to a single object
     * @param <T>     the type of the object
     * @return a response containing the single object or an exception if an error occurred
     */
    @Override
    public <T> DatabaseSingleObjResponse<T> executeSingleObjQueryUnchecked(Function<Connection, ResultSet> query, Function<ResultSet, T> adapter) {
        Connection connection = getConnection();
        ResultSet resultSet = null;
        try {
            resultSet = query.apply(connection);
            T results = adapter.apply(resultSet);
            return new DatabaseSingleObjResponse<>(null, results);
        } catch (Exception e) {
            return new DatabaseSingleObjResponse<>(e, null);
        } finally {
            closeResources(resultSet);
            releaseConnection(connection);
        }
    }


}
