package com.wonkglorg.util.database;

import com.wonkglorg.util.database.response.*;
import com.wonkglorg.util.interfaces.functional.checked.CheckedConsumer;
import com.wonkglorg.util.interfaces.functional.checked.CheckedFunction;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * @author Wonkglorg
 * <p>
 * Base class for databases
 */
@SuppressWarnings("unused")
public abstract class Database implements AutoCloseable {
    protected final String DRIVER;
    protected final String CLASSLOADER;
    protected final Logger logger = Logger.getLogger(Database.class.getName());

    protected Database(@NotNull DatabaseType databaseType) {
        this.DRIVER = databaseType.getDriver();
        this.CLASSLOADER = databaseType.getClassLoader();
    }

    protected Database(@NotNull final String driver, @NotNull final String classLoader) {
        this.DRIVER = driver;
        this.CLASSLOADER = classLoader;
    }

    /**
     * Small helper method to sanitize input for sql only does not other sanitizations like xss or html based
     *
     * @param input The input to sanitize
     * @return The sanitized output
     */
    public String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", "");
    }

    /**
     * @return A database connection
     */

    public abstract Connection getConnection();

    /**
     * Fully disconnects the database connection
     */
    public abstract void disconnect();

    /**
     * Close the result set and the statement
     *
     * @param resultSet the result set to close
     */
    protected void closeResources(ResultSet resultSet) {
        Statement statement = null;
        if (resultSet != null) {
            try {
                statement = resultSet.getStatement();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Central method to create a blob from a byte array
     *
     * @param bytes the byte array to convert
     * @return the blob
     */
    public Blob createBlob(byte[] bytes) {
        try {
            Blob blob = getConnection().createBlob();
            blob.setBytes(1, bytes);
            return blob;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] convertToByteArray(BufferedImage image, String formatType) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, formatType, baos);
        return baos.toByteArray();
    }


    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done
     *
     * @param query the query to execute
     */
    public abstract DatabaseResponse execute(CheckedConsumer<Connection> query);


    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done
     *
     * @param query the query to execute
     * @return amount of rows affected
     */
    public abstract DatabaseUpdateResponse executeUpdate(CheckedFunction<Connection, Integer> query);


    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done
     *
     * @param query  the query to execute
     * @param result the result of the query
     * @return DatabaseResponse
     */

    public abstract DatabaseUpdateResponse executeUpdate(CheckedFunction<Connection, PreparedStatement> query, CheckedFunction<PreparedStatement, Integer> result);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done
     *
     * @param query the query to execute
     * @return the result of the query
     */

    public abstract DatabaseResultSetResponse executeQuery(CheckedFunction<Connection, ResultSet> query);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done
     *
     * @param query  the query to execute
     * @param result the result of the query
     * @return DatabaseResponse
     */
    public abstract DatabaseResultSetResponse executeQuery(CheckedFunction<Connection, PreparedStatement> query, CheckedFunction<PreparedStatement, ResultSet> result);


    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done
     *
     * @param adapter the query to execute
     * @param <T>     the type of the object to return
     * @return the result of the query
     */
    public abstract <T> DatabaseObjResponse<T> executeObjQuery(CheckedFunction<Connection, List<T>> adapter);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done
     *
     * @param query  the query to execute
     * @param result the result of the query
     * @param <T>    the type of the object to return
     * @return DatabaseResponse
     */

    public abstract <T> DatabaseObjResponse<T> executeObjQuery(CheckedFunction<Connection, ResultSet> query, CheckedFunction<ResultSet, List<T>> result);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done
     *
     * @param adapter the adapter to convert the result to a single object
     * @param <T>     the type of the object to return
     * @return the result of the query
     */
    public abstract <T> DatabaseSingleObjResponse<T> executeSingleObjQuery(CheckedFunction<Connection, T> adapter);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done
     *
     * @param query   the query to execute
     * @param adapter the adapter to convert the result to a single object
     * @param <T>     the type of the object to return
     * @return the result of the query
     */
    public abstract <T> DatabaseSingleObjResponse<T> executeSingleObjQuery(CheckedFunction<Connection, ResultSet> query, CheckedFunction<ResultSet, T> adapter);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done
     *
     * @param query the query to execute
     * @return the result of the query
     */

    public abstract DatabaseResponse executeUnchecked(Consumer<Connection> query);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done not handle exceptions automatically
     *
     * @param query the query to execute
     * @return amount of rows affected
     */
    public abstract DatabaseUpdateResponse executeUpdateUnchecked(Function<Connection, Integer> query);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done not handle exceptions automatically
     *
     * @param query  the query to execute
     * @param result the result of the query
     * @return DatabaseResponse
     */
    public abstract DatabaseUpdateResponse executeUpdateUnchecked(Function<Connection, PreparedStatement> query, Function<PreparedStatement, Integer> result);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done not handle exceptions automatically
     *
     * @param query the query to execute
     * @return the result of the query
     */
    public abstract DatabaseResultSetResponse executeQueryUnchecked(Function<Connection, ResultSet> query);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done not handle exceptions automatically
     *
     * @param query  the query to execute
     * @param result the result of the query
     * @return DatabaseResponse
     */
    public abstract DatabaseResultSetResponse executeQueryUnchecked(Function<Connection, PreparedStatement> query, Function<PreparedStatement, ResultSet> result);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done does not handle exceptions automatically
     *
     * @param query the query to execute
     * @param <T>   the type of the object to return
     * @return the result of the query
     */
    public abstract <T> DatabaseObjResponse<T> executeObjQueryUnchecked(Function<Connection, List<T>> query);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done does not handle exceptions automatically
     *
     * @param query   the query to execute
     * @param adapter the adapter to convert the result to a list of objects
     * @param <T>     the type of the object to return
     * @return the result of the query
     */
    public abstract <T> DatabaseObjResponse<T> executeObjQueryUnchecked(Function<Connection, ResultSet> query, Function<ResultSet, List<T>> adapter);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done does not handle exceptions automatically
     *
     * @param adapter the adapter to convert the result to a single object
     * @param <T>     the type of the object to return
     * @return the result of the query
     */
    public abstract <T> DatabaseSingleObjResponse<T> executeSingleObjQueryUnchecked(Function<Connection, T> adapter);

    /**
     * Executes the given query with a connection and automatically releases the connection after the query is done does not handle exceptions automatically
     *
     * @param query   the query to execute
     * @param adapter the adapter to convert the result to a single object
     * @param <T>     the type of the object to return
     * @return the result of the query
     */
    public abstract <T> DatabaseSingleObjResponse<T> executeSingleObjQueryUnchecked(Function<Connection, ResultSet> query, Function<ResultSet, T> adapter);

    /**
     * @return the classloader path
     */
    public String getClassLoader() {
        return CLASSLOADER;
    }

    /**
     * @return The database driver
     */
    public String getDriver() {
        return DRIVER;
    }

    public enum DatabaseType {
        MYSQL("Mysql", "jdbc:mysql:", "com.mysql.cj.jdbc.Driver"), SQLITE("Sqlite", "jdbc:sqlite:", "org.sqlite.JDBC"), POSTGRESQL("Postgresql", "jdbc:postgresql:", "org.postgresql.Driver"), SQLSERVER("SqlServer", "jdbc:sqlserver:", "com.microsoft.sqlserver.jdbc.SQLServerDriver"), MARIA("MariaDB", "jdbc:mariadb:", "org.mariadb.jdbc.Driver");
        private final String driver;
        private final String classLoader;
        private final String name;

        DatabaseType(String name, String driver, String classLoader) {
            this.driver = driver;
            this.classLoader = classLoader;
            this.name = name;
        }

        public String getDriver() {
            return driver;
        }

        public String getClassLoader() {
            return classLoader;
        }

        public String getName() {
            return name;
        }
    }

}
