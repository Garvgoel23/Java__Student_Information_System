package com.sis.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL  = "jdbc:postgresql://localhost:5432/sis_db";
    private static final String USER = "postgres";
    private static final String PASS = "postgres"; // Password set during PostgreSQL installation

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found.", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
