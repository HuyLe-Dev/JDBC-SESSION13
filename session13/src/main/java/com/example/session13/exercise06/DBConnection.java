package com.example.session13.exercise06;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/todo_db";
    private static final String USER = "root";
    private static final String PASSWORD = "123456"; // Thay pass

    public DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}