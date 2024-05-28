package com.wonkglorg.util.database;

import com.wonkglorg.util.database.values.DbName;
import com.wonkglorg.util.database.values.DbPassword;
import com.wonkglorg.util.database.values.DbUrl;
import com.wonkglorg.util.database.values.DbUser;

/**
 * IMPORTANT! Please add the Microsoft SqlServer Connector to the project if you want to use SqlServer.
 */
public class MsSqlServerDatabase extends GenericServerDatabase {
    public MsSqlServerDatabase(DbUrl url, DbUser username, DbPassword password, DbName databasename, int poolSize) {
        super(DatabaseType.SQLSERVER, url, username, password, databasename, poolSize);
    }

    public MsSqlServerDatabase(DbUrl url, DbUser username, DbPassword password, int poolSize) {
        super(DatabaseType.SQLSERVER, url, username, password, poolSize);
    }

    public MsSqlServerDatabase(DbUrl url, DbUser username) {
        super(DatabaseType.SQLSERVER, url, username);
    }
}
