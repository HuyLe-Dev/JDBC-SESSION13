package com.example.session13.exercise03;

import java.sql.CallableStatement;
import java.sql.Connection;

public class StudentRepository {
    public int deleteStudentsByAge(int maxAge) {
        int deletedCount = 0;

        try (Connection connection = DBConnection.getConnection();
                CallableStatement cs = connection.prepareCall("{call delete_students_by_age(?)}")) {
            connection.setAutoCommit(false);
            try {
                // Truyền tham số vào Stored Procedure
                cs.setInt(1, maxAge);

                // executeUpdate() chạy câu lệnh DML (INSERT/UPDATE/DELETE)
                // và TỰ ĐỘNG trả về số dòng (rows) bị ảnh hưởng trong Database.
                deletedCount = cs.executeUpdate();

                // Xác nhận Transaction
                connection.commit();
            } catch (Exception e) {
                System.err.println("Lỗi SQL khi xóa, đang Rollback: " + e.getMessage());
                connection.rollback();
            }
        } catch (Exception e) {
            System.err.println("Lỗi kết nối CSDL: " + e.getMessage());
        }
        return deletedCount;
    }
}
