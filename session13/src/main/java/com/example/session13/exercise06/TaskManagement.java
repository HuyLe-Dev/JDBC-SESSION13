package com.example.session13.exercise06;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskManagement {

    public void addTask(String taskName, String status) {
        try (Connection connection = DBConnection.getConnection();
                CallableStatement cs = connection.prepareCall("{call add_task(?, ?)}")) {
            cs.setString(1, taskName);
            cs.setString(2, status);
            cs.executeUpdate();

            System.out.println("Thêm công việc thành công!");
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm công việc: " + e.getMessage());
        }
    }

    public List<Task> listTasks() {
        var results = new ArrayList<Task>();

        try (Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall("{call list_tasks()}");
                ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                results.add(new Task(
                        rs.getInt("id"),
                        rs.getString("task_name"),
                        rs.getString("status")));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách công việc: " + e.getMessage());
        }

        return results;
    }

    public void updateTaskStatus(int taskId, String status) {
        try (Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall("{call update_task_status(?, ?)}")) {

            cs.setInt(1, taskId);
            cs.setString(2, status);

            // executeUpdate() trả về số rows bị ảnh hưởng
            int affected = cs.executeUpdate();
            if (affected == 0) {
                System.out.println("Không tìm thấy công việc với ID: " + taskId);
            } else {
                System.out.println("Cập nhật trạng thái thành công!");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật trạng thái: " + e.getMessage());
        }
    }

    public void deleteTask(int taskId) {
        try (Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall("{call delete_task(?)}")) {

            cs.setInt(1, taskId);

            int affected = cs.executeUpdate();
            if (affected == 0) {
                System.out.println("Không tìm thấy công việc với ID: " + taskId);
            } else {
                System.out.println("Xóa công việc thành công!");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa công việc: " + e.getMessage());
        }
    }

    public List<Task> searchTaskByName(String taskName) {
        var results = new ArrayList<Task>();

        try (Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall("{call search_task_by_name(?)}")) {

            cs.setString(1, taskName);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    results.add(new Task(
                            rs.getInt("id"),
                            rs.getString("task_name"),
                            rs.getString("status")));
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm công việc: " + e.getMessage());
        }

        return results;
    }

    public void taskStatistics() {
        try (Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall("{call task_statistics()}");
                ResultSet rs = cs.executeQuery()) {

            if (rs.next()) {
                System.out.println("Thống kê công việc:");
                System.out.println("  Đã hoàn thành  : " + rs.getInt("completed"));
                System.out.println("  Chưa hoàn thành: " + rs.getInt("pending"));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi thống kê: " + e.getMessage());
        }
    }

}