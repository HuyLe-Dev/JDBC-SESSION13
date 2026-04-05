package com.example.session13.exercise10;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.session13.exercise10.entity.Student;
import com.example.session13.exercise10.entity.Course;
import com.example.session13.exercise10.entity.EnrollmentDetail;

public class StudentManager {
    public boolean addStudent(Student student) {
        if (checkExists("student", "email", student.getEmail())) {
            System.out.println("Error: Email already exists.");
            return false;
        }

        String sql = "INSERT INTO student (name, email) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addCourse(Course course) {
        if (checkExists("course", "title", course.getTitle())) {
            System.out.println("Error: Course title already exists.");
            return false;
        }

        String sql = "INSERT INTO course (title, credits) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course.getTitle());
            stmt.setInt(2, course.getCredits());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean enrollStudent(int studentId, int courseId) {
        if (!checkExists("student", "id", studentId) || !checkExists("course", "id", courseId)) {
            System.out.println("Error: Student ID or Course ID does not exist.");
            return false;
        }

        // Grade mặc định sẽ là NULL trong SQL
        String sql = "INSERT INTO enrollment (student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // MySQL Duplicate entry code
                System.out.println("Error: Student is already enrolled in this course.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    public boolean updateStudentGrade(int studentId, int courseId, double grade) {
        String sql = "UPDATE enrollment SET grade = ? WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, grade);
            stmt.setInt(2, studentId);
            stmt.setInt(3, courseId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Error: Enrollment record not found.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void listStudentsAndGrades() {
        String sql = """
                SELECT s.name AS student_name, c.title AS course_title, e.grade
                FROM enrollment e
                JOIN student s ON e.student_id = s.id
                JOIN course c ON e.course_id = c.id
                ORDER BY s.name
                """;

        List<EnrollmentDetail> flatList = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Đọc Object thay vì getDouble để bảo toàn giá trị NULL từ Database
                Object gradeObj = rs.getObject("grade");
                Double grade = gradeObj != null ? ((Number) gradeObj).doubleValue() : null;

                flatList.add(new EnrollmentDetail(
                        rs.getString("student_name"),
                        rs.getString("course_title"),
                        grade));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (flatList.isEmpty()) {
            System.out.println("No enrollments found.");
            return;
        }

        // Sử dụng Stream API gom nhóm (Tương tự Object.groupBy trong JS)
        Map<String, List<EnrollmentDetail>> groupedData = flatList.stream()
                .collect(Collectors.groupingBy(EnrollmentDetail::studentName));

        // Hiển thị phân cấp
        groupedData.forEach((studentName, details) -> {
            System.out.printf("Student: %s%n", studentName);
            details.forEach(d -> {
                // Xử lý ternary operator cho việc in điểm null
                String gradeDisplay = (d.grade() != null) ? String.format("%.2f", d.grade()) : "Not Graded";
                System.out.printf("  -> Course: %-20s | Grade: %s%n", d.courseTitle(), gradeDisplay);
            });
            System.out.println("-".repeat(45));
        });
    }

    // --- HELPER FUNCTION ---
    private boolean checkExists(String tableName, String columnName, Object value) {
        String sql = "SELECT 1 FROM " + tableName + " WHERE " + columnName + " = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (value instanceof String str)
                stmt.setString(1, str);
            else if (value instanceof Integer num)
                stmt.setInt(1, num);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
