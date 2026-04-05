package com.example.session13.exercise08.entity;

import java.math.BigDecimal;

public class OrderDetail {
    private int productId;
    private int quantity;
    private BigDecimal priceSell; // Sẽ được lấy từ DB khi tạo đơn

    public OrderDetail(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPriceSell() {
        return priceSell;
    }

    public void setPriceSell(BigDecimal priceSell) {
        this.priceSell = priceSell;
    }
}
