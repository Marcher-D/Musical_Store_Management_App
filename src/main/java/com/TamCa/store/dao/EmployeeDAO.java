package com.TamCa.store.dao;

import com.TamCa.store.model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * EmployeeDAO: Lớp chịu trách nhiệm giao tiếp trực tiếp với bảng Employee trong Database.
 */
public class EmployeeDAO {
    
    // Sử dụng lại logic kết nối từ ProductDAO/AccountDAO
    private Connection getConnection() throws SQLException {
        return new AccountDAO().getConnection();
    }
    
    // --- MAPPER: Chuyển đổi ResultSet thành đối tượng Employee ---
    private Employee buildEmployeeFromResultSet(ResultSet rs) throws SQLException {
        String nameEmp = rs.getString("nameEmp");
        String EID = rs.getString("EID");
        String posEmp = rs.getString("posEmp");
        int salEmp = rs.getInt("salEmp");
        Date hireDate = rs.getDate("hireDate"); // Sử dụng java.sql.Date -> chuyển về java.util.Date
        
        return new Employee(nameEmp, EID, posEmp, salEmp, hireDate);
    }
    
    // --- CRUD: CREATE ---
    public boolean addEmployee(Employee employee) {
        String sql = "INSERT INTO Employee (EID, nameEmp, posEmp, salEmp, hireDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employee.getEID());
            pstmt.setString(2, employee.getNameEmp());
            pstmt.setString(3, employee.getPosEmp());
            pstmt.setInt(4, employee.getSalEmp());
            // Chuyển java.util.Date sang java.sql.Date cho DB
            pstmt.setDate(5, new java.sql.Date(employee.getHireDate().getTime())); 
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("DB Error: Failed to add employee: " + e.getMessage());
            return false;
        }
    }
    
    // --- CRUD: READ ALL ---
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                employees.add(buildEmployeeFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("DB Error: Failed to load all employees: " + e.getMessage());
        }
        return employees;
    }
    
    // --- CRUD: READ by ID ---
    public Employee getEmployeeByEID(String EID) {
        String sql = "SELECT * FROM Employee WHERE EID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, EID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return buildEmployeeFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("DB Error: Failed to get employee by EID: " + e.getMessage());
        }
        return null;
    }
    
    // --- CRUD: UPDATE ---
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE Employee SET nameEmp = ?, posEmp = ?, salEmp = ?, hireDate = ? WHERE EID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employee.getNameEmp());
            pstmt.setString(2, employee.getPosEmp());
            pstmt.setInt(3, employee.getSalEmp());
            pstmt.setDate(4, new java.sql.Date(employee.getHireDate().getTime()));
            pstmt.setString(5, employee.getEID());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("DB Error: Failed to update employee: " + e.getMessage());
            return false;
        }
    }
    
    // --- CRUD: DELETE ---
    public boolean deleteEmployee(String EID) {
        String sql = "DELETE FROM Employee WHERE EID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, EID);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("DB Error: Failed to delete employee: " + e.getMessage());
            return false;
        }
    }
}