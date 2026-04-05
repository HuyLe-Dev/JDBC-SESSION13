package com.example.session13.exercise08.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Order {
    private int id;
    private int customerId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private List<OrderDetail> details; // Chứa danh sách các món hàng

    // Dùng khi tạo đơn mới
    public Order(int customerId, List<OrderDetail> details) {
        this.customerId = customerId;
        this.details = details;
        this.orderDate = LocalDate.now();
        this.totalAmount = BigDecimal.ZERO;
    }

    // Getters/Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }
}
