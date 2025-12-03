package com.TamCa.store.dao; 

import java.sql.*;

// responsible for communicate with the Account table in the DB
public class AccountDAO {
    //* connect to the DB
    public Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
    
    public String checkLogin(String username, String password) {
        String sql = "SELECT role FROM Account WHERE username = ? AND password = ?";
        
        //* try-with-resource using here!
        //* the statement inside try(...) will automatically close when it out of the block, even if it cause error or not!
        //* pstmt was created to input the parameter (?) safely
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role"); //* successful login if the rs could jump down 1 line and return the role <-> username & password inputed
                }
            }
        } 
        catch (SQLException e) {
            System.err.println("DB Error while checking login: " + e.getMessage());
        }
        return null; //* login failed as it have no data, so the pointer rs cannot jump down
    }
}