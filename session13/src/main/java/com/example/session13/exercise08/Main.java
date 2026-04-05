package com.example.session13.exercise08;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.session13.exercise08.entity.Customer;
import com.example.session13.exercise08.entity.OrderDetail;
import com.example.session13.exercise08.entity.Product;
import com.example.session13.exercise08.entity.Order;

public class Main {
    public static void main(String[] args) {
        var manager = new OrderManager();
        var scanner = new Scanner(System.in);
        var isRunning = true;

        while (isRunning) {
            System.out.println("\n=== STORE MANAGEMENT SYSTEM ===");
            System.out.println("1. Add New Product");
            System.out.println("2. Update Customer Info");
            System.out.println("3. Create New Order");
            System.out.println("4. Display All Orders");
            System.out.println("5. Find Orders by Customer ID");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Product Name: ");
                        var name = scanner.nextLine().trim();
                        System.out.print("Enter Price: ");
                        var price = new BigDecimal(scanner.nextLine().trim());

                        if (manager.addProduct(new Product(name, price))) {
                            System.out.println("Success: Product added.");
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter Customer ID to update: ");
                        int customerId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter New Name: ");
                        var name = scanner.nextLine().trim();
                        System.out.print("Enter New Email: ");
                        var email = scanner.nextLine().trim();

                        if (manager.updateCustomer(customerId, new Customer(name, email))) {
                            System.out.println("Success: Customer info updated.");
                        }
                    }
                    case 3 -> {
                        System.out.print("Enter Customer ID for this order: ");
                        int customerId = Integer.parseInt(scanner.nextLine().trim());

                        List<OrderDetail> items = new ArrayList<>();
                        while (true) {
                            System.out.print("Enter Product ID (or 0 to finish adding items): ");
                            int productId = Integer.parseInt(scanner.nextLine().trim());
                            if (productId == 0)
                                break;

                            System.out.print("Enter Quantity: ");
                            int quantity = Integer.parseInt(scanner.nextLine().trim());

                            items.add(new OrderDetail(productId, quantity));
                        }

                        if (items.isEmpty()) {
                            System.out.println("Operation cancelled. Order must have at least one product.");
                        } else {
                            Order newOrder = new Order(customerId, items);
                            manager.createOrder(newOrder);
                        }
                    }
                    case 4 -> {
                        System.out.println("--- All Orders ---");
                        manager.listAllOrders();
                    }
                    case 5 -> {
                        System.out.print("Enter Customer ID to search: ");
                        int searchId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println("--- Orders for Customer ID " + searchId + " ---");
                        manager.getOrdersByCustomer(searchId);
                    }
                    case 0 -> isRunning = false;
                    default -> System.out.println("Warning: Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format.");
            }
        }
        scanner.close();
        System.out.println("Goodbye!");
    }
}
