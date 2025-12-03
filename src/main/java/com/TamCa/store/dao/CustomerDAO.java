package com.TamCa.store.dao;

import com.TamCa.store.model.Customer;
// import com.TamCa.store.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private Connection getConnection() throws SQLException {
        return new AccountDAO().getConnection();
    }
    
    // mapper map ResultSet -> Custromer
    private Customer buildCustomerFromResultSet(ResultSet rs) throws SQLException {
        String nameCus = rs.getString("nameCus");
        String CSN = rs.getString("CSN");
        String phoneNum = rs.getString("phoneNum");
        String emailCus = rs.getString("emailCus");
        String addCus = rs.getString("addCus");
        
        return new Customer(nameCus, CSN, phoneNum, emailCus, addCus);
    }
    
    // add customer
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
    
    // list the customers down
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
    
    // get customer by the CSN
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
    
    // update customer's information (just address & phone num only)
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
    
    // delete customer
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