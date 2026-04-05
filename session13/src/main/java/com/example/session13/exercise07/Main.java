package com.example.session13.exercise07;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var manager = new BookManager();
        var scanner = new Scanner(System.in);
        var isRunning = true;

        while (isRunning) {
            System.out.println("\n=== HỆ THỐNG QUẢN LÝ THƯ VIỆN ===");
            System.out.println("1. Thêm sách");
            System.out.println("2. Cập nhật thông tin sách");
            System.out.println("3. Xóa sách");
            System.out.println("4. Tìm kiếm sách theo tác giả");
            System.out.println("5. Hiển thị tất cả sách");
            System.out.println("0. Thoát");
            System.out.print("Lựa chọn của bạn: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                // Enhanced Switch (Java 14+)
                switch (choice) {
                    case 1 -> {
                        System.out.print("Nhập tiêu đề: ");
                        var title = scanner.nextLine().trim();
                        System.out.print("Nhập tác giả: ");
                        var author = scanner.nextLine().trim();
                        System.out.print("Nhập năm XB: ");
                        int year = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Nhập giá tiền: ");
                        var price = new BigDecimal(scanner.nextLine().trim());

                        boolean success = manager.addBook(new Book(title, author, year, price));
                        if (success) System.out.println("✅ Thêm sách thành công!");
                        else System.out.println("❌ Lỗi: Sách đã tồn tại hoặc có lỗi hệ thống.");
                    }
                    case 2 -> {
                        System.out.print("Nhập ID sách cần cập nhật: ");
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Nhập tiêu đề mới: ");
                        var title = scanner.nextLine().trim();
                        System.out.print("Nhập tác giả mới: ");
                        var author = scanner.nextLine().trim();
                        System.out.print("Nhập năm XB mới: ");
                        int year = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Nhập giá tiền mới: ");
                        var price = new BigDecimal(scanner.nextLine().trim());

                        boolean success = manager.updateBook(id, new Book(title, author, year, price));
                        if (success) System.out.println("✅ Cập nhật thành công!");
                        else System.out.println("❌ Lỗi: Không tìm thấy sách với ID = " + id);
                    }
                    case 3 -> {
                        System.out.print("Nhập ID sách cần xóa: ");
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        
                        boolean success = manager.deleteBook(id);
                        if (success) System.out.println("✅ Đã xóa sách thành công!");
                        else System.out.println("❌ Lỗi: Không tìm thấy sách với ID = " + id);
                    }
                    case 4 -> {
                        System.out.print("Nhập tên tác giả cần tìm: ");
                        var author = scanner.nextLine().trim();
                        var books = manager.findBooksByAuthor(author);
                        
                        if (books.isEmpty()) System.out.println("🔍 Không tìm thấy sách của tác giả này.");
                        else books.forEach(System.out::println);
                    }
                    case 5 -> {
                        var books = manager.listAllBooks();
                        if (books.isEmpty()) System.out.println("📚 Thư viện hiện đang trống.");
                        else books.forEach(System.out::println);
                    }
                    case 0 -> isRunning = false;
                    default -> System.out.println("⚠️ Lựa chọn không hợp lệ.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Vui lòng nhập đúng định dạng số!");
            }
        }
        scanner.close();
        System.out.println("👋 Tạm biệt!");
    }
}