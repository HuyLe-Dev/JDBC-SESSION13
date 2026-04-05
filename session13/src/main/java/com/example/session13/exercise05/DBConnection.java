package com.example.session13.exercise05;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL      = "jdbc:mysql://localhost:3306/movie_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "123456"; // Thay pass của bạn

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}



