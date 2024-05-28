package com.wonkglorg.util.database;


import com.wonkglorg.util.database.values.DbName;
import com.wonkglorg.util.database.values.DbPassword;
import com.wonkglorg.util.database.values.DbUrl;
import com.wonkglorg.util.database.values.DbUser;

/**
 * IMPORTANT! Please add the mysql Jconnector to the project if you want to use MySql, I did not include this myself to not inflate the libraries
 * size. groupId : mysql artifactId : mysql-connector-java
 */
@SuppressWarnings("unused")
public class MySqlDatabase extends GenericServerDatabase {


    public MySqlDatabase(DbUrl url, DbUser username, DbPassword password, DbName databasename, int poolSize) {
        super(DatabaseType.MYSQL, url, username, password, databasename, poolSize);
    }

    public MySqlDatabase(DbUrl url, DbUser username, DbPassword password, int poolSize) {
        super(DatabaseType.MYSQL, url, username, password, poolSize);
    }

    public MySqlDatabase(DbUrl url, DbUser username) {
        super(DatabaseType.MYSQL, url, username);
    }

}
