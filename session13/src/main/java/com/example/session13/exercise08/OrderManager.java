package com.example.session13.exercise08;

import java.math.BigDecimal;
import java.sql.*;

import com.example.session13.exercise08.entity.Customer;
import com.example.session13.exercise08.entity.OrderDetail;
import com.example.session13.exercise08.entity.Product;
import com.example.session13.exercise08.entity.Order;

public class OrderManager {
    public boolean addProduct(Product product) {
        String checkSql = "SELECT 1 FROM product WHERE name = ?";
        String insertSql = "INSERT INTO product (name, price) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            checkStmt.setString(1, product.getName());
            if (checkStmt.executeQuery().next()) {
                System.out.println("Error: Product name already exists.");
                return false;
            }

            insertStmt.setString(1, product.getName());
            insertStmt.setBigDecimal(2, product.getPrice());
            return insertStmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Cập nhật khách hàng
    public boolean updateCustomer(int customerId, Customer customer) {
        String sql = "UPDATE customer SET name = ?, email = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setInt(3, customerId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Error: Customer ID not found.");
                return false;
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Tạo đơn hàng mới (SỬ DỤNG TRANSACTION NÂNG CAO)
    public boolean createOrder(Order order) {
        String priceSql = "SELECT price FROM product WHERE id = ?";
        String insertOrderSql = "INSERT INTO orders (customer_id, order_date, total_amount) VALUES (?, ?, ?)";
        String insertDetailSql = "INSERT INTO order_detail (product_id, order_id, quantity, price_sell) VALUES (?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            // BẮT ĐẦU TRANSACTION: Tắt auto-commit
            conn.setAutoCommit(false);

            BigDecimal totalAmount = BigDecimal.ZERO;

            // Bước 3.1: Lấy giá sản phẩm và tính tổng tiền
            try (PreparedStatement priceStmt = conn.prepareStatement(priceSql)) {
                for (OrderDetail detail : order.getDetails()) {
                    priceStmt.setInt(1, detail.getProductId());
                    ResultSet rs = priceStmt.executeQuery();
                    if (rs.next()) {
                        BigDecimal currentPrice = rs.getBigDecimal("price");
                        detail.setPriceSell(currentPrice); // Lưu giá chốt vào detail

                        BigDecimal lineTotal = currentPrice.multiply(BigDecimal.valueOf(detail.getQuantity()));
                        totalAmount = totalAmount.add(lineTotal);
                    } else {
                        throw new SQLException("Product ID " + detail.getProductId() + " not found.");
                    }
                }
            }
            order.setTotalAmount(totalAmount);

            // Bước 3.2: Insert vào bảng `orders` VÀ LẤY GENERATED KEY (ID tự tăng)
            int newOrderId;
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, order.getCustomerId());
                orderStmt.setDate(2, Date.valueOf(order.getOrderDate()));
                orderStmt.setBigDecimal(3, order.getTotalAmount());
                orderStmt.executeUpdate();

                ResultSet generatedKeys = orderStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    newOrderId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated Order ID.");
                }
            }

            // Bước 3.3: Insert hàng loạt vào `order_detail`
            try (PreparedStatement detailStmt = conn.prepareStatement(insertDetailSql)) {
                for (OrderDetail detail : order.getDetails()) {
                    detailStmt.setInt(1, detail.getProductId());
                    detailStmt.setInt(2, newOrderId);
                    detailStmt.setInt(3, detail.getQuantity());
                    detailStmt.setBigDecimal(4, detail.getPriceSell());

                    // AddBatch giúp gom các lệnh SQL lại gửi 1 lần -> Tối ưu hiệu năng
                    detailStmt.addBatch();
                }
                detailStmt.executeBatch();
            }

            // KẾT THÚC TRANSACTION: Lưu tất cả thay đổi
            conn.commit();
            System.out.println("Order created successfully! Order ID: " + newOrderId);
            return true;

        } catch (SQLException e) {
            System.out.println("Transaction Failed. Rolling back changes: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            // Luôn nhớ trả kết nối về trạng thái ban đầu và đóng kết nối
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // 4 & 5. Hiển thị đơn hàng (Dùng chung 1 private method để tuân thủ DRY
    // principle)
    public void listAllOrders() {
        String sql = "SELECT o.id, c.name as customer_name, o.order_date, o.total_amount " +
                "FROM orders o JOIN customer c ON o.customer_id = c.id";
        displayOrders(sql, null);
    }

    public void getOrdersByCustomer(int customerId) {
        String sql = "SELECT o.id, c.name as customer_name, o.order_date, o.total_amount " +
                "FROM orders o JOIN customer c ON o.customer_id = c.id WHERE o.customer_id = ?";
        displayOrders(sql, customerId);
    }

    private void displayOrders(String sql, Integer customerId) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (customerId != null) {
                stmt.setInt(1, customerId);
            }

            ResultSet rs = stmt.executeQuery();
            boolean hasData = false;

            System.out.printf("%-5s | %-20s | %-12s | %-10s%n", "ID", "Customer Name", "Order Date", "Total");
            System.out.println("---------------------------------------------------------");
            while (rs.next()) {
                hasData = true;
                System.out.printf("%-5d | %-20s | %-12s | $%,.2f%n",
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getDate("order_date"),
                        rs.getBigDecimal("total_amount"));
            }

            if (!hasData) {
                System.out.println("No orders found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
