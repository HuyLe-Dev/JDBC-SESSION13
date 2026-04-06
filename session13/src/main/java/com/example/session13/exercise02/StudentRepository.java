package com.example.session13.exercise02;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    public List<Student> getAllStudents() {
        var results = new ArrayList<Student>();

        try (Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall("{call get_all_students()}");
                ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                results.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("full_name"),
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("email")));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách sinh viên: " + e.getMessage());
        }

        return results;
    }

    public void addStudent(String fullName, LocalDate dateOfBirth, String email) {
        try (Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall("{call add_student(?, ?, ?)}")) {

            cs.setString(1, fullName);
            // Date.valueOf() chuyển LocalDate → java.sql.Date — giống toISOString() trong
            // JS
            cs.setDate(2, Date.valueOf(dateOfBirth));
            cs.setString(3, email);
            cs.executeUpdate();

            System.out.println("Thêm sinh viên thành công!");

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm sinh viên: " + e.getMessage());
        }
    }

}
