package com.TamCa.store.dao;

import com.TamCa.store.model.Order;
import java.sql.*;

public class OrderDAO {
    
    // Sử dụng lại connection từ AccountDAO để đảm bảo tính nhất quán cấu hình
    private Connection getConnection() throws SQLException {
        return new AccountDAO().getConnection(); 
    }

    public boolean createOrder(Order order) {
        Connection conn = null;
        PreparedStatement pstmtOrder = null;
        PreparedStatement pstmtDetail = null;
        PreparedStatement pstmtUpdateStock = null;

        // Bảng Orders không còn cột employee_eid
        String sqlOrder = "INSERT INTO Orders (status, sellDate, deliDate, deliAdd, customer_csn) VALUES (?, ?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO OrderDetail (order_id, product_id, quantity, price_at_sale) VALUES (?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE Product SET quantityInStock = quantityInStock - ? WHERE id = ?";

        try {
            conn = getConnection();
            // 1. TẮT AUTO COMMIT (Bắt đầu Transaction)
            conn.setAutoCommit(false);

            // 2. Insert bảng Orders
            pstmtOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            pstmtOrder.setString(1, order.getStatus());
            pstmtOrder.setDate(2, new java.sql.Date(order.getSellDate().getTime()));
            pstmtOrder.setDate(3, new java.sql.Date(order.getDeliDate().getTime()));
            pstmtOrder.setString(4, order.getDeliAdd());
            pstmtOrder.setString(5, order.getCustomerCSN());
            
            int affectedRows = pstmtOrder.executeUpdate();
            if (affectedRows == 0) throw new SQLException("Creating order failed, no rows affected.");

            int orderId = 0;
            try (ResultSet generatedKeys = pstmtOrder.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                    order.setOrderId(orderId);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }

            // 3. Insert Detail và Trừ kho
            for (Order.OrderDetail item : order.getItems()) {
                // A. Insert Detail
                pstmtDetail = conn.prepareStatement(sqlDetail);
                pstmtDetail.setInt(1, orderId);
                pstmtDetail.setString(2, item.getProduct().getId());
                pstmtDetail.setInt(3, item.getQuantity());
                pstmtDetail.setDouble(4, item.getPriceAtSale());
                pstmtDetail.executeUpdate();

                // B. Trừ kho (TRANSACTION: Phải nằm trong cùng khối commit)
                pstmtUpdateStock = conn.prepareStatement(sqlUpdateStock);
                pstmtUpdateStock.setInt(1, item.getQuantity()); 
                pstmtUpdateStock.setString(2, item.getProduct().getId());
                pstmtUpdateStock.executeUpdate();
            }

            // 4. COMMIT (Lưu tất cả thay đổi)
            conn.commit();
            System.out.println("✅ Order created successfully! ID: " + orderId);
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error creating order: " + e.getMessage());
            // ROLLBACK (Hoàn tác nếu có lỗi)
            if (conn != null) {
                try {
                    System.err.println("Rolling back transaction...");
                    conn.rollback();
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            // Đóng kết nối
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
