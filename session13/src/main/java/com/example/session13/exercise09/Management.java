package com.example.session13.exercise09;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Management {
    public boolean addEmployee(Employee emp) {
        if (checkExists("employee", "name", emp.getName())) {
            System.out.println("Error: Employee name already exists.");
            return false;
        }

        String sql = "INSERT INTO employee (name, department, salary) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getDepartment());
            stmt.setBigDecimal(3, emp.getSalary());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addProject(Project proj) {
        if (checkExists("project", "name", proj.getName())) {
            System.out.println("Error: Project name already exists.");
            return false;
        }

        String sql = "INSERT INTO project (name, budget) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proj.getName());
            stmt.setBigDecimal(2, proj.getBudget());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean assignEmployeeToProject(int employeeId, int projectId, String role) {
        if (!checkExists("employee", "id", employeeId) || !checkExists("project", "id", projectId)) {
            System.out.println("Error: Employee ID or Project ID does not exist.");
            return false;
        }

        String sql = "INSERT INTO assignment (employee_id, project_id, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setInt(2, projectId);
            stmt.setString(3, role);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Xử lý lỗi trùng lặp do PK kép (Duplicate Key)
            if (e.getErrorCode() == 1062) {
                System.out.println("Error: This employee is already assigned to this project.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    public boolean updateEmployeeSalary(int employeeId, double newSalary) {
        String sql = "UPDATE employee SET salary = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Ép kiểu an toàn từ double sang BigDecimal
            stmt.setBigDecimal(1, BigDecimal.valueOf(newSalary));
            stmt.setInt(2, employeeId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Error: Employee ID not found.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Dùng Java Text Blocks (""") cho câu lệnh SQL dài.
     * Trả về danh sách phẳng các dòng (Flat Rows).
     */
    public List<AssignmentDetails> getFlatAssignments() {
        String sql = """
                SELECT e.name AS emp_name, e.department, p.name AS proj_name, a.role
                FROM assignment a
                JOIN employee e ON a.employee_id = e.id
                JOIN project p ON a.project_id = p.id
                ORDER BY e.name
                """;

        List<AssignmentDetails> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new AssignmentDetails(
                        rs.getString("emp_name"),
                        rs.getString("department"),
                        rs.getString("proj_name"),
                        rs.getString("role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Hàm dùng Stream API để gom nhóm dữ liệu.
     * Tương tự logic group by ID/Name trong Javascript.
     */
    public void listEmployeesAndProjects() {
        var flatList = getFlatAssignments();

        if (flatList.isEmpty()) {
            System.out.println("No assignment records found.");
            return;
        }

        // Group by employee name (Key: Tên NV, Value: Danh sách các dự án họ tham gia)
        Map<String, List<AssignmentDetails>> groupedData = flatList.stream()
                .collect(Collectors.groupingBy(AssignmentDetails::employeeName));

        // In dữ liệu phân cấp
        groupedData.forEach((empName, details) -> {
            String dept = details.get(0).department(); // Lấy phòng ban từ dòng đầu tiên
            System.out.printf("Employee: %s (Dept: %s)%n", empName, dept);

            details.forEach(d -> System.out.printf("  -> Project: %-15s | Role: %s%n", d.projectName(), d.role()));
            System.out.println("-".repeat(40));
        });
    }

    // --- HELPER FUNCTION (DRY Principle) ---
    private boolean checkExists(String tableName, String columnName, Object value) {
        String sql = "SELECT 1 FROM " + tableName + " WHERE " + columnName + " = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (value instanceof String str) {
                stmt.setString(1, str);
            } else if (value instanceof Integer num) {
                stmt.setInt(1, num);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
