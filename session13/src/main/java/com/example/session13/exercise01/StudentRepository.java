package com.example.session13.exercise01;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StudentRepository {
    public void addStudents(List<Student> students) {
        // CÚ PHÁP SENIOR: Try-With-Resources (Java 7+)
        // Tự động đóng (close) Connection và CallableStatement sau khi dùng xong
        try (Connection conn = DBConnection.getConnection();
                // Cú pháp gọi Stored Procedure trong JDBC
                CallableStatement cs = conn.prepareCall("{call sp_add_student(?, ?)}")) {

            // BƯỚC 1: Tắt Auto-Commit để bắt đầu Transaction
            // Giống hệt 'BEGIN TRANSACTION' trong SQL
            conn.setAutoCommit(false);

            try {
                // BƯỚC 2: Truyền dữ liệu vào Stored Procedure
                for (var student : students) {
                    cs.setString(1, student.name()); // Index bắt đầu từ 1, không phải 0 như mảng
                    cs.setInt(2, student.age());

                    // Thêm vào hàng đợi (Batch) thay vì chạy ngay lập tức để tối ưu hiệu năng
                    cs.addBatch();
                }

                // BƯỚC 3: Thực thi toàn bộ hàng đợi
                cs.executeBatch();

                // BƯỚC 4: Xác nhận thay đổi (Commit Transaction)
                conn.commit();
                System.out.println("Đã thêm " + students.size() + " sinh viên thành công.");

            } catch (SQLException e) {
                // BƯỚC 5: Xảy ra lỗi -> Rollback (Hủy bỏ mọi thay đổi trong Transaction này)
                System.err.println("Có lỗi xảy ra khi insert. Đang thực hiện Rollback...");
                conn.rollback();
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }
}
