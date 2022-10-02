package com.ravisreyas.redis.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectConfig {

    private static final DBConnectConfig INSTANCE = new DBConnectConfig();
    private Connection conn;
    private DBConnectConfig() {
        String url = "jdbc:sqlite:D:/Akshayaa/ravi_repo/pocRedisDB.db";
        try {
            this.conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBConnectConfig getInstance() {
        return INSTANCE;
    }

    public Connection getDBConnection() {
        return conn;
    }
}
