package main.java.com.example.session13.exercise03;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var repository = new StudentRepository();
        var scanner = new Scanner(System.in);

        System.out.println("Nhập tuổi để xóa những học sinh có độ tuổi nhỏ hơn :");

        try {
            var inputAge = Integer.parseInt(scanner.nextLine().trim());

            // Gọi hàm repository và nhận lại số lượng đã xóa
            int deletedCount = repository.deleteStudentsByAge(inputAge);

            // Logic rẽ nhánh UI dựa trên data trả về
            if (deletedCount == 0) {
                System.out.println("Không tìm thấy học sinh nào có tuổi nhỏ hơn : " + inputAge);
            } else {
                System.out.println("Xóa thành công " + deletedCount + " học sinh có tuổi nhỏ hơn : " + inputAge);
            }

        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập một số nguyên hợp lệ.");
        }
        scanner.close();
    }

}
