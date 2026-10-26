package com.library.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {

    private static Properties props;

    private DBConnection() { }

    private static synchronized Properties config() throws SQLException {
        if (props == null) {
            Properties p = new Properties();
            try (InputStream in = DBConnection.class.getClassLoader()
                    .getResourceAsStream("db.properties")) {
                if (in == null) {
                    throw new SQLException("db.properties not found. Copy db.properties.example to db.properties.");
                }
                p.load(in);
            } catch (IOException e) {
                throw new SQLException("Could not read db.properties", e);
            }
            props = p;
        }
        return props;
    }

    public static Connection getConnection() throws SQLException {
        Properties p = config();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found in WEB-INF/lib", e);
        }
        return DriverManager.getConnection(
                p.getProperty("db.url"),
                p.getProperty("db.user"),
                p.getProperty("db.password"));
    }
}