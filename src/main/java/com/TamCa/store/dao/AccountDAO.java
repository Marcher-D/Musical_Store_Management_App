package com.TamCa.store.dao; 

import java.sql.*;

/**
 * AccountDAO: Chịu trách nhiệm giao tiếp với bảng Account (Login).
 * Đã được Refactor để sử dụng DBConnection chung.
 */
public class AccountDAO {
    
    // --- DB CONNECTION METHOD ---
    public Connection getConnection() throws SQLException {
        // Gọi class tiện ích chung
        return DBConnection.getConnection();
    }
    
    // --- LOGIN LOGIC ---
    public String checkLogin(String username, String password) {
        String sql = "SELECT role FROM Account WHERE username = ? AND password = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role"); // Login successful
                }
            }
        } catch (SQLException e) {
            System.err.println("DB Error while checking login: " + e.getMessage());
        }
        return null; // Login failed
    }
}