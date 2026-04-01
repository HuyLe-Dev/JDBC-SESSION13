package main.java.com.example.session13.exercise01;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        var repository = new StudentRepository();

        List<Student> students = List.of(
                new Student(0, "Nguyen Van A", 20),
                new Student(0, "Nguyen Van B", 22),
                new Student(0, "Nguyen Van A", 28));

        repository.addStudents(students);
    }
}
