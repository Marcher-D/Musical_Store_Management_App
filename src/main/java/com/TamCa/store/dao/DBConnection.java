package com.TamCa.store.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection: Class quản lý kết nối Database tập trung (Singleton Pattern đơn giản).
 * Thay thế cho việc khai báo URL/User/Pass rải rác ở nhiều nơi.
 */
public class DBConnection {
    
    // Thông tin cấu hình Database (Chỉ cần sửa ở đây là áp dụng cho toàn project)
    private static final String BASE_DB_URL = "jdbc:mysql://localhost:3306"; 
    private static final String DATABASE_NAME = "musical_store_db";
    private static final String CONNECTION_PARAMS = "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC"; 
    
    private static final String DB_USER = "root";       
    private static final String DB_PASSWORD = "Peter@18122005"; // <-- Kiểm tra lại pass của bạn

    public static Connection getConnection() throws SQLException {
        String url = BASE_DB_URL + "/" + DATABASE_NAME + CONNECTION_PARAMS;
        try {
            return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối Database tại DBConnection: " + e.getMessage());
            throw e;
        }
    }
}