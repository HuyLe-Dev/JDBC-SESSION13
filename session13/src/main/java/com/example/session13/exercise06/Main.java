package com.example.session13.exercise06;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final TaskManagement management = new TaskManagement();
    private static final Scanner scanner = new Scanner(System.in);

    // Hai giá trị status hợp lệ — dùng constant tránh magic string, giống enum
    // trong TS
    private static final String STATUS_PENDING = "chưa hoàn thành";
    private static final String STATUS_COMPLETED = "đã hoàn thành";

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = readInt("Chọn chức năng: ");

            // switch arrow (Java 14+) — giống switch expression trong TS, không cần break
            switch (choice) {
                case 1 -> handleAdd();
                case 2 -> handleList();
                case 3 -> handleUpdateStatus();
                case 4 -> handleDelete();
                case 5 -> handleSearch();
                case 6 -> management.taskStatistics();
                case 0 -> {
                    System.out.println("Thoát chương trình.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private static void handleAdd() {
        String taskName = readNonEmpty("Nhập tên công việc: ");
        String status = readStatus();
        management.addTask(taskName, status);
    }

    private static void handleList() {
        List<Task> tasks = management.listTasks();
        if (tasks.isEmpty()) {
            System.out.println("Danh sách công việc trống.");
            return;
        }
        printTaskTable(tasks);
    }

    private static void handleUpdateStatus() {
        int id = readInt("Nhập ID công việc cần cập nhật: ");
        String status = readStatus();
        management.updateTaskStatus(id, status);
    }

    private static void handleDelete() {
        int id = readInt("Nhập ID công việc cần xóa: ");
        management.deleteTask(id);
    }

    private static void handleSearch() {
        String keyword = readNonEmpty("Nhập tên công việc cần tìm: ");
        List<Task> tasks = management.searchTaskByName(keyword);
        if (tasks.isEmpty()) {
            System.out.println("Không tìm thấy công việc nào có tên chứa: \"" + keyword + "\"");
            return;
        }
        System.out.println("Tìm thấy " + tasks.size() + " công việc:");
        printTaskTable(tasks);
    }

    // --- UI helpers ---

    private static void printTaskTable(List<Task> tasks) {
        System.out.printf("%-5s %-30s %-20s%n", "ID", "Tên công việc", "Trạng thái");
        System.out.println("-".repeat(57));
        // forEach + lambda — giống .forEach() trong JS
        tasks.forEach(t -> System.out.printf("%-5d %-30s %-20s%n", t.id(), t.taskName(), t.status()));
    }

    // Chỉ cho phép nhập 1 hoặc 2, map sang string status — tránh user nhập sai giá
    // trị
    private static String readStatus() {
        while (true) {
            System.out.println("Chọn trạng thái: 1. Chưa hoàn thành  2. Đã hoàn thành");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice == 1)
                    return STATUS_PENDING;
                if (choice == 2)
                    return STATUS_COMPLETED;
                System.err.println("Vui lòng chọn 1 hoặc 2.");
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập một số nguyên hợp lệ.");
            }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                if (!input.isEmpty())
                    return input;
                System.err.println("Không được để trống, vui lòng nhập lại.");
            } catch (Exception e) {
                System.err.println("Dữ liệu không hợp lệ, vui lòng nhập lại.");
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập một số nguyên hợp lệ.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== QUẢN LÝ TO-DO LIST =====");
        System.out.println("1. Thêm công việc");
        System.out.println("2. Liệt kê công việc");
        System.out.println("3. Cập nhật trạng thái");
        System.out.println("4. Xóa công việc");
        System.out.println("5. Tìm kiếm công việc");
        System.out.println("6. Thống kê công việc");
        System.out.println("0. Thoát");
    }
}