package com.example.session13.exercise10;

import java.util.Scanner;

import com.example.session13.exercise10.entity.Course;
import com.example.session13.exercise10.entity.Student;

public class Main {
    public static void main(String[] args) {
        var manager = new StudentManager();
        var scanner = new Scanner(System.in);
        var isRunning = true;

        while (isRunning) {
            System.out.println("\n=== UNIVERSITY MANAGEMENT SYSTEM ===");
            System.out.println("1. Add New Student");
            System.out.println("2. Add New Course");
            System.out.println("3. Enroll Student to Course");
            System.out.println("4. Update Student Grade");
            System.out.println("5. Display Students & Grades");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Student Name: ");
                        var name = scanner.nextLine().trim();
                        System.out.print("Enter Email: ");
                        var email = scanner.nextLine().trim();

                        if (manager.addStudent(new Student(name, email))) {
                            System.out.println("Success: Student added.");
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter Course Title: ");
                        var title = scanner.nextLine().trim();
                        System.out.print("Enter Credits: ");
                        int credits = Integer.parseInt(scanner.nextLine().trim());

                        if (manager.addCourse(new Course(title, credits))) {
                            System.out.println("Success: Course added.");
                        }
                    }
                    case 3 -> {
                        System.out.print("Enter Student ID: ");
                        int studentId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Course ID: ");
                        int courseId = Integer.parseInt(scanner.nextLine().trim());

                        if (manager.enrollStudent(studentId, courseId)) {
                            System.out.println("Success: Student enrolled.");
                        }
                    }
                    case 4 -> {
                        System.out.print("Enter Student ID: ");
                        int studentId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Course ID: ");
                        int courseId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter New Grade: ");
                        double grade = Double.parseDouble(scanner.nextLine().trim());

                        if (manager.updateStudentGrade(studentId, courseId, grade)) {
                            System.out.println("Success: Grade updated.");
                        }
                    }
                    case 5 -> {
                        System.out.println("\n--- Grades Report ---");
                        manager.listStudentsAndGrades();
                    }
                    case 0 -> isRunning = false;
                    default -> System.out.println("Warning: Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
        scanner.close();
        System.out.println("Goodbye!");
    }
}
