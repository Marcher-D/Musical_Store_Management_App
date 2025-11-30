package com.TamCa.store.dao; 

import java.sql.*;

/**
 * AccountDAO: Class responsible for direct communication with the Account table 
 * (handling login).
 */
public class AccountDAO {

    // --- DATABASE CONFIGURATION ---
    private static final String BASE_DB_URL = "jdbc:mysql://localhost:3306"; 
    private static final String CONNECTION_PARAMS = "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC"; 
    private static final String DB_USER = "root";       
    private static final String DB_PASSWORD = "passcode"; 
    private static final String DATABASE_NAME = "musical_store_db";
    
    // --- DB CONNECTION METHOD ---
    public Connection getConnection() throws SQLException {
        String url = BASE_DB_URL + "/" + DATABASE_NAME + CONNECTION_PARAMS;
        
        try {
            return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database CONNECTION FAILED: " + e.getMessage());
            throw e; 
        }
    }
    
    // --- LOGIN LOGIC (Requires the Account table in DB) ---
    /**
     * Returns Role (Manager/Staff) if credentials are correct, returns null otherwise.
     */
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
            // e.printStackTrace(); 
        }
        return null; // Login failed
    }
}