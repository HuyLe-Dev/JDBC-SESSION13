package com.example.session13.exercise03;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import com.example.session13.exercise02.DBConnection;

public class StudentRepository {
    public void updateStudent(int id, String fullName, LocalDate dateOfBirth, String email) {
        try (Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall("{call update_student(?, ?, ?, ?)}")) {

            cs.setInt(1, id);
            cs.setString(2, fullName);
            cs.setDate(3, Date.valueOf(dateOfBirth));
            cs.setString(4, email);

            // affectedRows == 0 nghĩa là ID không tồn tại — giống result.affectedRows trong
            // mysql2/Node
            int affected = cs.executeUpdate();
            if (affected == 0) {
                System.out.println("Không tìm thấy sinh viên với ID: " + id);
            } else {
                System.out.println("Cập nhật sinh viên thành công!");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật sinh viên: " + e.getMessage());
        }
    }

    public void deleteStudent(int id) {
        try (Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall("{call delete_student(?)}")) {

            cs.setInt(1, id);

            int affected = cs.executeUpdate();
            if (affected == 0) {
                System.out.println("Không tìm thấy sinh viên với ID: " + id);
            } else {
                System.out.println("Xóa sinh viên thành công!");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa sinh viên: " + e.getMessage());
        }
    }

}
