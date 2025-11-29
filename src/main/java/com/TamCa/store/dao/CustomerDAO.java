package com.TamCa.store.dao;

import com.TamCa.store.model.Customer;
// import com.TamCa.store.utils.DBConnection; // Đã loại bỏ import giả định này

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerDAO: Lớp chịu trách nhiệm giao tiếp trực tiếp với bảng Customer trong Database.
 */
public class CustomerDAO {
    // Sử dụng lại logic kết nối từ ProductDAO/AccountDAO
    private Connection getConnection() throws SQLException {
        // Gọi AccountDAO để lấy Connection, đảm bảo tính nhất quán về cấu hình DB
        return new AccountDAO().getConnection();
    }
    
    // --- MAPPER: Chuyển đổi ResultSet thành đối tượng Customer ---
    private Customer buildCustomerFromResultSet(ResultSet rs) throws SQLException {
        String nameCus = rs.getString("nameCus");
        String CSN = rs.getString("CSN");
        String phoneNum = rs.getString("phoneNum");
        String emailCus = rs.getString("emailCus");
        String addCus = rs.getString("addCus");
        
        return new Customer(nameCus, CSN, phoneNum, emailCus, addCus);
    }
    
    // --- CRUD: CREATE ---
    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO Customer (CSN, nameCus, phoneNum, emailCus, addCus) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getCSN());
            pstmt.setString(2, customer.getNameCus());
            pstmt.setString(3, customer.getPhoneNum());
            pstmt.setString(4, customer.getEmailCus());
            pstmt.setString(5, customer.getAddCus());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("DB Error: Failed to add customer: " + e.getMessage());
            return false;
        }
    }
    
    // --- CRUD: READ ALL ---
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                customers.add(buildCustomerFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("DB Error: Failed to load all customers: " + e.getMessage());
        }
        return customers;
    }
    
    // --- CRUD: READ by ID ---
    public Customer getCustomerByCSN(String CSN) {
        String sql = "SELECT * FROM Customer WHERE CSN = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, CSN);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return buildCustomerFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("DB Error: Failed to get customer by CSN: " + e.getMessage());
        }
        return null;
    }
    
    // --- CRUD: UPDATE (Chỉ cho phép cập nhật thông tin liên hệ/địa chỉ) ---
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE Customer SET nameCus = ?, phoneNum = ?, emailCus = ?, addCus = ? WHERE CSN = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getNameCus());
            pstmt.setString(2, customer.getPhoneNum());
            pstmt.setString(3, customer.getEmailCus());
            pstmt.setString(4, customer.getAddCus());
            pstmt.setString(5, customer.getCSN());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("DB Error: Failed to update customer: " + e.getMessage());
            return false;
        }
    }
    
    // --- CRUD: DELETE ---
    public boolean deleteCustomer(String CSN) {
        String sql = "DELETE FROM Customer WHERE CSN = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, CSN);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("DB Error: Failed to delete customer: " + e.getMessage());
            return false;
        }
    }
}