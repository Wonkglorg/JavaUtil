package com.wonkglorg.util.database;

import com.wonkglorg.util.database.response.*;
import com.wonkglorg.util.interfaces.functional.checked.CheckedBiFunction;
import com.wonkglorg.util.interfaces.functional.checked.CheckedConsumer;
import com.wonkglorg.util.interfaces.functional.checked.CheckedFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.RecordComponent;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static final Map<Class<?>, CheckedBiFunction<ResultSet, String, Object>> valueMappers = new HashMap<>();
    private static final Map<Class<?>, CheckedBiFunction<ResultSet, Integer, Object>> indexMappers = new HashMap<>();

    static {
        valueMappers.put(int.class, ResultSet::getInt);
        valueMappers.put(long.class, ResultSet::getLong);
        valueMappers.put(double.class, ResultSet::getDouble);
        valueMappers.put(float.class, ResultSet::getFloat);
        valueMappers.put(boolean.class, ResultSet::getBoolean);
        valueMappers.put(byte.class, ResultSet::getByte);
        valueMappers.put(short.class, ResultSet::getShort);
        valueMappers.put(char.class, (resultSet, string) -> resultSet.getString(string).charAt(0));
        valueMappers.put(String.class, ResultSet::getString);
        valueMappers.put(Date.class, ResultSet::getDate);
        valueMappers.put(Time.class, ResultSet::getTime);
        valueMappers.put(Timestamp.class, ResultSet::getTimestamp);
        valueMappers.put(Blob.class, ResultSet::getBlob);
        valueMappers.put(byte[].class, ResultSet::getBytes);
        valueMappers.put(Image.class, (resultSet, string) -> ImageIO.read(resultSet.getBinaryStream(string)));


        indexMappers.put(int.class, ResultSet::getInt);
        indexMappers.put(long.class, ResultSet::getLong);
        indexMappers.put(double.class, ResultSet::getDouble);
        indexMappers.put(float.class, ResultSet::getFloat);
        indexMappers.put(boolean.class, ResultSet::getBoolean);
        indexMappers.put(byte.class, ResultSet::getByte);
        indexMappers.put(short.class, ResultSet::getShort);
        indexMappers.put(char.class, (resultSet, index) -> resultSet.getString(index).charAt(0));
        indexMappers.put(String.class, ResultSet::getString);
        indexMappers.put(Date.class, ResultSet::getDate);
        indexMappers.put(Time.class, ResultSet::getTime);
        indexMappers.put(Timestamp.class, ResultSet::getTimestamp);
        indexMappers.put(Blob.class, ResultSet::getBlob);
        indexMappers.put(byte[].class, ResultSet::getBytes);
        indexMappers.put(Image.class, (resultSet, index) -> ImageIO.read(resultSet.getBinaryStream(index)));

    }


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

    public CheckedFunction<ResultSet, Integer> singleIntAdapter() {
        return resultSet -> resultSet.getInt(1);
    }

    public CheckedFunction<ResultSet, String> singleStringAdapter() {
        return resultSet -> resultSet.getString(1);
    }

    public CheckedFunction<ResultSet, Boolean> singleBooleanAdapter() {
        return resultSet -> resultSet.getBoolean(1);
    }

    public CheckedFunction<ResultSet, Long> singleLongAdapter() {
        return resultSet -> resultSet.getLong(1);
    }

    public CheckedFunction<ResultSet, Double> singleDoubleAdapter() {
        return resultSet -> resultSet.getDouble(1);
    }

    public CheckedFunction<ResultSet, Float> singleFloatAdapter() {
        return resultSet -> resultSet.getFloat(1);
    }

    public CheckedFunction<ResultSet, Short> singleShortAdapter() {
        return resultSet -> resultSet.getShort(1);
    }

    public CheckedFunction<ResultSet, Byte> singleByteAdapter() {
        return resultSet -> resultSet.getByte(1);
    }

    /**
     * Maps a record constructor to its matching sql columns (names MUST match or it will not work)
     * <p/>
     * If any of the record columns do not have a adapter mapped a custom can be added / overwritten  with {@link #addValueMapper(Class, CheckedBiFunction)}
     *
     * @param recordClass the record class to map
     * @param <T>         the type of the record
     * @return the adapter to convert the result set to a record
     */
    public <T extends Record> CheckedFunction<ResultSet, T> recordAdapter(Class<T> recordClass) {
        return resultSet -> {
            try {
                RecordComponent[] components = recordClass.getRecordComponents();
                Object[] args = new Object[components.length];


                for (RecordComponent component : components) {
                    String columnName = component.getName();
                    Class<?> type = component.getType();
                    valueMappers.getOrDefault(type, (result, string) -> resultSet.getObject(columnName, type)).apply(resultSet, columnName);
                }

                return recordClass.getDeclaredConstructor(Arrays.stream(components).map(RecordComponent::getType).toArray(Class<?>[]::new)).newInstance(args);
            } catch (Exception e) {
                throw new SQLException("Failed to map record components", e);
            }
        };
    }

    /**
     * Maps a record constructor to its matching sql columns (in index order constructor must match the order)
     * <p/>
     * If any of the record columns do not have a adapter mapped a custom can be added / overwritten  with {@link #addIndexMapper(Class, CheckedBiFunction)}
     *
     * @param recordClass the record class to map
     * @param <T>         the type of the record
     * @return the adapter to convert the result set to a record
     */
    public <T extends Record> CheckedFunction<ResultSet, T> recordIndexAdapter(Class<T> recordClass) {
        return resultSet -> {
            try {
                RecordComponent[] components = recordClass.getRecordComponents();
                Object[] args = new Object[components.length];

                for (int i = 0; i < components.length; i++) {
                    Class<?> type = components[i].getType();
                    args[i] = indexMappers.getOrDefault(type, (result, index) -> resultSet.getObject(index, type)).apply(resultSet, i + 1);
                }

                return recordClass.getDeclaredConstructor(Arrays.stream(components).map(RecordComponent::getType).toArray(Class<?>[]::new)).newInstance(args);
            } catch (Exception e) {
                throw new SQLException("Failed to map record components", e);
            }
        };
    }


    public <T> T getSingleObject(ResultSet resultSet, CheckedFunction<ResultSet, T> adapter) {
        try {
            if (resultSet.next()) {
                return adapter.apply(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    /**
     * Checks the current database the connection is connected to
     *
     * @return
     */
    public String checkCurrentDatabase(Connection connection) {

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT DB_NAME() AS CurrentDB")) {
            if (rs.next()) {
                return rs.getString("CurrentDB");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error logging action: " + e.getMessage());
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


    /**
     * Adds a value mapper to auto map values in a record {@link #recordAdapter}
     *
     * @param type   the type to map
     * @param mapper the mapper to map the value
     * @return the previous mapper if there was one
     */
    public @Nullable CheckedBiFunction<ResultSet, String, Object> addValueMapper(Class<?> type, CheckedBiFunction<ResultSet, String, Object> mapper) {
        return valueMappers.put(type, mapper);
    }

    /**
     * Removes a value mapper from the auto mapper list used in {@link #recordAdapter}
     *
     * @param type the type to remove
     * @return the removed mapper if there was one
     */
    public @Nullable CheckedBiFunction<ResultSet, String, Object> removeValueMapper(Class<?> type) {
        return valueMappers.remove(type);
    }

    /**
     * Adds a index mapper to auto map values in a record {@link #recordIndexAdapter}
     *
     * @param type
     * @param mapper
     * @return
     */
    public @Nullable CheckedBiFunction<ResultSet, Integer, Object> addIndexMapper(Class<?> type, CheckedBiFunction<ResultSet, Integer, Object> mapper) {
        return indexMappers.put(type, mapper);
    }

    /**
     * Removes a index mapper from the auto mapper list used in {@link #recordIndexAdapter}
     *
     * @param type the type to remove
     * @return the removed mapper if there was one
     */
    public @Nullable CheckedBiFunction<ResultSet, Integer, Object> removeIndexMapper(Class<?> type) {
        return indexMappers.remove(type);
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
