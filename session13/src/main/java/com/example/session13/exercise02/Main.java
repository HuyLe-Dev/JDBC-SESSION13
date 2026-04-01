package main.java.com.example.session13.exercise02;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        var repository = new StudentRepository();

        List<Student> studentsToUpdate = List.of(
                new Student(1, "Nguyễn Văn A - Đã sửa", 21),
                new Student(2, "Trần Thị B - Đã sửa", 23),
                new Student(3, "Lê Văn C - Đã sửa", 20));

        System.out.println("Đang tiến hành cập nhật thông tin sinh viên...");

        boolean isSuccess = repository.updateStudents(studentsToUpdate);

        if (isSuccess) {
            System.out.println("Cập nhật thành công toàn bộ sinh viên!");
        } else {
            System.out.println("Cập nhật thất bại. Hệ thống đã khôi phục lại trạng thái ban đầu.");
        }

    }
}
