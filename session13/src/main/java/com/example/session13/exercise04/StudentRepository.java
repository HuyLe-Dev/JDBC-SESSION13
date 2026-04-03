package com.example.session13.exercise04;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    public List<Student> searchByName(String name) {
        var results = new ArrayList<Student>();

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{call search_student(?)}")) {

            cs.setString(1, name);

            // executeQuery() dùng cho SELECT — trả về ResultSet (giống Promise<Row[]> trong Node)
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    results.add(new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age")
                    ));
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi kết nối CSDL: " + e.getMessage());
        }

        return results;
    }
}
