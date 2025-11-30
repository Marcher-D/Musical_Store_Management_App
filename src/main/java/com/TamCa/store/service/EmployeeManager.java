package com.TamCa.store.service;

import com.TamCa.store.dao.EmployeeDAO;
import com.TamCa.store.model.Employee;
import java.util.List;

/**
 * EmployeeManager: Lớp Logic nghiệp vụ (Service Layer) cho Employee.
 * Xử lý các quy tắc kinh doanh liên quan đến Nhân viên (Ví dụ: kiểm tra quyền, định mức lương).
 */
public class EmployeeManager {
    
    private final EmployeeDAO employeeDAO;

    public EmployeeManager() {
        this.employeeDAO = new EmployeeDAO();
    }
    
    /**
     * Tải tất cả nhân viên từ CSDL.
     */
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }
    
    /**
     * Thêm một nhân viên mới.
     * Có thể thêm logic kiểm tra định mức lương, vị trí hợp lệ tại đây.
     */
    public boolean addNewEmployee(Employee employee) {
        // Ví dụ: Business Rule: Kiểm tra mức lương tối thiểu
        if (employee.getSalEmp() < 1000) { 
            System.err.println("Business Rule Violated: Salary cannot be less than 1000.");
            return false;
        }
        if (employeeDAO.getEmployeeByEID(employee.getEID()) != null) {
             System.err.println("Error: Employee ID " + employee.getEID() + " already exists.");
             return false;
        }
        return employeeDAO.addEmployee(employee);
    }

    public String generateNextEmployeeId() {
        List<Employee> list = getAllEmployees();
        if (list.isEmpty()) return "E001";

        int maxId = 0;
        for (Employee emp : list) {
            try {
                String numPart = emp.getEID().replaceAll("\\D+", "");
                if (!numPart.isEmpty()) {
                    int id = Integer.parseInt(numPart);
                    if (id > maxId) maxId = id;
                }
            } catch (Exception e) { continue; }
        }
        return String.format("E%03d", maxId + 1);
    }
    
    /**
     * Cập nhật thông tin nhân viên.
     */
    public boolean updateEmployee(Employee employee) {
        return employeeDAO.updateEmployee(employee);
    }
    
    /**
     * Xóa nhân viên theo EID.
     */
    public boolean deleteEmployee(String EID) {
        return employeeDAO.deleteEmployee(EID);
    }
    
    // Thêm các hàm nghiệp vụ khác (ví dụ: tìm kiếm, thống kê hiệu suất) nếu cần
}