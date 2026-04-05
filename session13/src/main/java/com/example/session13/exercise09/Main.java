package com.example.session13.exercise09;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var manager = new Management();
        var scanner = new Scanner(System.in);
        var isRunning = true;

        while (isRunning) {
            System.out.println("\n=== HR & PROJECT MANAGEMENT ===");
            System.out.println("1. Add New Employee");
            System.out.println("2. Add New Project");
            System.out.println("3. Assign Employee to Project");
            System.out.println("4. Display Assignments (Grouped)");
            System.out.println("5. Update Employee Salary");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> {
                        System.out.print("Employee Name: ");
                        var name = scanner.nextLine().trim();
                        System.out.print("Department: ");
                        var dept = scanner.nextLine().trim();
                        System.out.print("Salary: ");
                        var salary = new BigDecimal(scanner.nextLine().trim());

                        if (manager.addEmployee(new Employee(name, dept, salary))) {
                            System.out.println("Success: Employee added.");
                        }
                    }
                    case 2 -> {
                        System.out.print("Project Name: ");
                        var name = scanner.nextLine().trim();
                        System.out.print("Budget: ");
                        var budget = new BigDecimal(scanner.nextLine().trim());

                        if (manager.addProject(new Project(name, budget))) {
                            System.out.println("Success: Project added.");
                        }
                    }
                    case 3 -> {
                        System.out.print("Employee ID: ");
                        int empId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Project ID: ");
                        int projId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Role: ");
                        var role = scanner.nextLine().trim();

                        if (manager.assignEmployeeToProject(empId, projId, role)) {
                            System.out.println("Success: Assignment created.");
                        }
                    }
                    case 4 -> {
                        System.out.println("\n--- Current Assignments ---");
                        manager.listEmployeesAndProjects();
                    }
                    case 5 -> {
                        System.out.print("Employee ID: ");
                        int empId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("New Salary: ");
                        double newSalary = Double.parseDouble(scanner.nextLine().trim());

                        if (manager.updateEmployeeSalary(empId, newSalary)) {
                            System.out.println("Success: Salary updated.");
                        }
                    }
                    case 0 -> isRunning = false;
                    default -> System.out.println("Warning: Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
        scanner.close();
        System.out.println("System gracefully shut down.");
    }
}
