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
import java.util.logging.Level;

public class GenericServerDatabase extends Database {

    protected final DbUser USERNAME;
    protected final DbUrl URL;
    protected DbPassword PASSWORD;
    protected DbName DATABASE_NAME;
    private final BlockingQueue<Connection> connectionPool;


    public GenericServerDatabase(String driver, String classLoader, DbUrl url, DbUser username, DbPassword password, DbName databasename, int poolSize) {
        super(driver, classLoader);
        USERNAME = username;
        URL = url;
        PASSWORD = password;
        DATABASE_NAME = databasename;

        connectionPool = new ArrayBlockingQueue<>(poolSize);
        initializeConnectionPool(poolSize);
    }

    public GenericServerDatabase(DatabaseType type, DbUrl url, DbUser username, DbPassword password, DbName databasename, int poolSize) {
        this(type.getDriver(), type.getClassLoader(), url, username, password, databasename, poolSize);
    }

    public GenericServerDatabase(String driver, String classLoader, DbUrl url, DbUser username, DbPassword password, int poolSize) {
        this(driver, classLoader, url, username, password, null, poolSize);
    }

    public GenericServerDatabase(String driver, String classLoader, DbUrl url, DbUser username) {
        this(driver, classLoader, url, username, null, null, 5);
    }

    public GenericServerDatabase(DatabaseType type, DbUrl url, DbUser username, DbPassword password, int poolSize) {
        this(type.getDriver(), type.getClassLoader(), url, username, password, null, poolSize);
    }

    public GenericServerDatabase(DatabaseType type, DbUrl url, DbUser username) {
        this(type.getDriver(), type.getClassLoader(), url, username, null, null, 5);
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

    /**
     * Release a connection back to the connection pool
     *
     * @param connection the connection to release
     */
    public void releaseConnection(Connection connection) {
        connectionPool.offer(connection);
    }


    /**
     * Resize the connection pool
     *
     * @param newSize the new size of the connection pool
     */
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


    /**
     * Disconnect from the database and close all connections
     */
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


    /**
     * Initialize the connection pool
     *
     * @param poolSize the size of the connection pool
     */
    private void initializeConnectionPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            connectionPool.add(createConnection());
        }
    }


    /**
     * Use a specific database for a connection
     *
     * @param connection   the connection to use the database on
     * @param databaseName the name of the database to use
     */
    public void useDatabase(Connection connection, DbName databaseName) {
        String name = sanitize(databaseName.toString());
        try (Statement statement = connection.createStatement()) {
            statement.execute("USE " + name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Use a specific database for all connections
     *
     * @param databaseName the name of the database to use
     */
    public void useDatabaseForAllConnections(String databaseName) {
        DATABASE_NAME = new DbName(databaseName);
        for (Connection connection : connectionPool) {
            useDatabase(connection, DATABASE_NAME);
        }
    }

    /**
     * Helper Method to create a connection
     *
     * @return a new connection
     */
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

    /**
     * Close all resources
     */
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
            logger.log(Level.SEVERE, "Error executing query: " + e.getMessage(), e);
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
            logger.log(Level.SEVERE, "Error executing update: " + e.getMessage(), e);
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
            logger.log(Level.SEVERE, "Error executing update: " + e.getMessage(), e);
            return new DatabaseUpdateResponse(e, -1);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Error closing statement: " + e.getMessage(), e);
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
            logger.log(Level.SEVERE, "Error executing query: " + e.getMessage(), e);
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
            logger.log(Level.SEVERE, "Error executing query: " + e.getMessage(), e);
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
            logger.log(Level.SEVERE, "Error executing query: " + e.getMessage(), e);
            return new DatabaseObjResponse<>(e, null);
        } finally {
            releaseConnection(connection);
        }
    }

    public <T> DatabaseObjResponse<T> executeObjQueryUnchecked(Function<Connection, ResultSet> query, Function<ResultSet, List<T>> adapter) {
        Connection connection = getConnection();
        ResultSet resultSet = null;
        try {
            resultSet = query.apply(connection);

            List<T> results = adapter.apply(resultSet);
            return new DatabaseObjResponse<>(null, results);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error executing query: " + e.getMessage(), e);
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
            logger.log(Level.SEVERE, "Error executing query: " + e.getMessage(), e);
            return new DatabaseSingleObjResponse<>(e, null);
        } finally {
            releaseConnection(connection);
        }
    }

    @Override
    public <T> DatabaseSingleObjResponse<T> executeSingleObjQueryUnchecked(Function<Connection, ResultSet> query, Function<ResultSet, T> adapter) {
        Connection connection = getConnection();
        ResultSet resultSet = null;
        try {
            resultSet = query.apply(connection);
            T results = adapter.apply(resultSet);
            return new DatabaseSingleObjResponse<>(null, results);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error executing query: " + e.getMessage(), e);
            return new DatabaseSingleObjResponse<>(e, null);
        } finally {
            closeResources(resultSet);
            releaseConnection(connection);
        }
    }
}
