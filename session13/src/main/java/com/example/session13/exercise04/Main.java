package main.java.com.example.session13.exercise04;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var repository = new StudentRepository();

        // try-with-resources cho Scanner — giống `using` trong C# hoặc cleanup trong useEffect
        try (var scanner = new Scanner(System.in)) {
            String keyword = promptKeyword(scanner);
            List<Student> students = repository.searchByName(keyword);
            printResults(students, keyword);
        }
    }

    // Tách method riêng để main() chỉ đọc như "kịch bản", không chứa logic chi tiết
    private static String promptKeyword(Scanner scanner) {
        while (true) {
            System.out.print("Nhập tên sinh viên cần tìm: ");
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.err.println("Tên không được để trống. Vui lòng nhập lại.");
                    continue;
                }
                return input;
            } catch (Exception e) {
                System.err.println("Dữ liệu không hợp lệ. Vui lòng nhập lại.");
            }
        }
    }

    private static void printResults(List<Student> students, String keyword) {
        if (students.isEmpty()) {
            System.out.println("Không tìm thấy sinh viên nào có tên chứa: \"" + keyword + "\"");
            return;
        }
        System.out.println("Tìm thấy " + students.size() + " sinh viên:");
        // forEach + lambda — giống .forEach() trong JS
        students.forEach(s ->
                System.out.printf("  ID: %d | Tên: %-30s | Tuổi: %d%n", s.id(), s.name(), s.age())
        );
    }
}
