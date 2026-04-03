package main.java.com.example.session13.exercise05;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final MovieManagement management = new MovieManagement();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = readInt("Chọn chức năng: ");

            // switch arrow (Java 14+) — giống switch expression trong TS, không cần break
            switch (choice) {
                case 1 -> handleAdd();
                case 2 -> handleList();
                case 3 -> handleUpdate();
                case 4 -> handleDelete();
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
        String title    = readNonEmpty("Nhập tiêu đề phim: ");
        String director = readNonEmpty("Nhập đạo diễn: ");
        int year        = readInt("Nhập năm phát hành: ");
        management.addMovie(title, director, year);
    }

    private static void handleList() {
        List<Movie> movies = management.listMovies();
        if (movies.isEmpty()) {
            System.out.println("Chưa có phim nào trong danh sách.");
            return;
        }
        System.out.printf("%n%-5s %-30s %-20s %-6s%n", "ID", "Tiêu đề", "Đạo diễn", "Năm");
        System.out.println("-".repeat(65));
        // forEach + lambda — giống .forEach() trong JS
        movies.forEach(m ->
                System.out.printf("%-5d %-30s %-20s %-6d%n", m.id(), m.title(), m.director(), m.year())
        );
    }

    private static void handleUpdate() {
        int id          = readInt("Nhập ID phim cần sửa: ");
        String title    = readNonEmpty("Nhập tiêu đề mới: ");
        String director = readNonEmpty("Nhập đạo diễn mới: ");
        int year        = readInt("Nhập năm phát hành mới: ");
        management.updateMovie(id, title, director, year);
    }

    private static void handleDelete() {
        int id = readInt("Nhập ID phim cần xóa: ");
        management.deleteMovie(id);
    }

    // --- Input helpers ---

    // Vòng lặp nhập lại nếu rỗng — giống inquirer.js validate() trong Node CLI
    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) return input;
                System.err.println("Không được để trống, vui lòng nhập lại.");
            } catch (Exception e) {
                System.err.println("Dữ liệu không hợp lệ, vui lòng nhập lại.");
            }
        }
    }

    // Vòng lặp nhập lại nếu không phải số nguyên — giống parseInt với fallback trong JS
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
        System.out.println("\n===== QUẢN LÝ PHIM =====");
        System.out.println("1. Thêm phim");
        System.out.println("2. Liệt kê phim");
        System.out.println("3. Sửa phim");
        System.out.println("4. Xóa phim");
        System.out.println("0. Thoát");
    }
}
