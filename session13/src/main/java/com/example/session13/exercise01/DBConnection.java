package com.example.session13.exercise01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Trong thực tế, các thông số này sẽ đọc từ file .env hoặc
    // application.properties
    private static final String URL = "jdbc:mysql://localhost:3306/student_db?useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "123456"; // Thay pass

    // Private constructor để ngăn chặn khởi tạo object
    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
