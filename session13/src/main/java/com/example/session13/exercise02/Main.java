package com.example.session13.exercise02;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final StudentRepository repository = new StudentRepository();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = readInt("Chọn chức năng: ");

            switch (choice) {
                case 1 -> handleList();
                case 2 -> handleAdd();
                case 3, 4, 5 -> System.out.println("Chức năng đang được phát triển...");
                case 6 -> {
                    System.out.println("Thoát chương trình.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private static void handleList() {
        List<Student> students = repository.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("Chưa có sinh viên nào trong danh sách.");
            return;
        }
        System.out.printf("%n%-5s %-25s %-15s %-30s%n", "ID", "Họ tên", "Ngày sinh", "Email");
        System.out.println("-".repeat(77));
        students.forEach(s -> System.out.printf("%-5d %-25s %-15s %-30s%n",
                s.studentId(),
                s.fullName(),
                s.dateOfBirth().format(DATE_FORMAT),
                s.email()));
    }

    private static void handleAdd() {
        String fullName = readNonEmpty("Nhập họ tên: ");
        LocalDate dob = readDate("Nhập ngày sinh (dd/MM/yyyy): ");
        String email = readEmail("Nhập email: ");
        repository.addStudent(fullName, dob, email);
    }

    // --- Input helpers ---

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

    // LocalDate.parse() ném DateTimeParseException nếu sai format — giống new
    // Date('invalid') trong JS
    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine().trim(), DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.err.println("Định dạng ngày không hợp lệ. Vui lòng nhập theo dd/MM/yyyy.");
            }
        }
    }

    // Validate email đơn giản bằng contains("@") — đủ dùng cho bài tập
    private static String readEmail(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                if (!input.isEmpty() && input.contains("@"))
                    return input;
                System.err.println("Email không hợp lệ, vui lòng nhập lại.");
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
        System.out.println("\n===== QUẢN LÝ SINH VIÊN =====");
        System.out.println("1. Hiển thị danh sách sinh viên");
        System.out.println("2. Thêm mới sinh viên");
        System.out.println("3. Sửa sinh viên");
        System.out.println("4. Xóa sinh viên");
        System.out.println("5. Tìm kiếm sinh viên");
        System.out.println("6. Thoát");
    }
}
