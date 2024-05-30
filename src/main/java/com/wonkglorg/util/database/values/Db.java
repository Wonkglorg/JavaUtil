package com.wonkglorg.util.database.values;

public class Db {
    /**
     * Represents a database url
     *
     * @param url
     * @return
     */

    public static DbUrl url(String url) {
        return new DbUrl(url);
    }

    /**
     * Represents a database user
     *
     * @param user
     * @return
     */
    public static DbUser user(String user) {
        return new DbUser(user);
    }

    /**
     * Represents a database password
     *
     * @param password
     * @return
     */
    public static DbPassword password(String password) {
        return new DbPassword(password);
    }

    /**
     * Represents a database name
     *
     * @param name
     * @return
     */
    public static DbName name(String name) {
        return new DbName(name);
    }

    /**
     * Represents a database port
     *
     * @param port
     * @return
     */
    public static DbPort port(String port) {
        return new DbPort(port);
    }
}
