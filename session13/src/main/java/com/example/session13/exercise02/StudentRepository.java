package main.java.com.example.session13.exercise02;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;

public class StudentRepository {
    public boolean updateStudents(List<Student> students) {
        try (Connection connection = DBConnection.getConnection();
                CallableStatement cs = connection.prepareCall("{call update_student(?, ?, ?)}")) {
            connection.setAutoCommit(false);
            try {
                for (var student : students) {
                    cs.setInt(1, student.id());
                    cs.setString(2, student.name());
                    cs.setInt(3, student.age());

                    cs.addBatch();
                }

                // Lấy mảng kết quả
                int[] updateCounts = cs.executeBatch();

                // Quét qua mảng kết quả (Giống mảng.some() trong JS)
                for (int count : updateCounts) {
                    if (count == 0) {
                        // Phát hiện có câu lệnh không update được dòng nào (Ví dụ: Sai ID)
                        System.err.println("Cảnh báo: Có sinh viên không tồn tại trong DB. Đang Rollback toàn bộ...");
                        conn.rollback();
                        return false; // Hủy bỏ giao dịch
                    }
                }

                // Nếu qua được vòng for nghĩa là mọi update đều tác động ít nhất 1 dòng
                conn.commit();
                return true;
            } catch (Exception e) {
                System.err.println("Lỗi dữ liệu, đang thực hiện Rollback: " + e.getMessage());
                connection.rollback();
                return false;
            }
        } catch (Exception e) {
            System.err.println("Lỗi kết nối CSDL: " + e.getMessage());
            return false;
        }
    }
}
