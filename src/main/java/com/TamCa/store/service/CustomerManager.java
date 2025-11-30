package com.TamCa.store.service;

import com.TamCa.store.dao.CustomerDAO;
import com.TamCa.store.model.Customer;
import java.util.List;

/**
 * CustomerManager: Lớp Logic nghiệp vụ (Service Layer) cho Customer.
 * Xử lý các quy tắc kinh doanh liên quan đến Khách hàng.
 */
public class CustomerManager {
    
    private final CustomerDAO customerDAO;

    public CustomerManager() {
        this.customerDAO = new CustomerDAO();
    }
    
    /**
     * Tải tất cả khách hàng từ CSDL.
     */
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }
    
    /**
     * Thêm một khách hàng mới.
     * Có thể thêm logic kiểm tra trùng lặp CSN, định dạng email/sdt tại đây.
     */
    public boolean addNewCustomer(Customer customer) {
        // Ví dụ: Business Rule: Không cho phép thêm nếu đã tồn tại CSN
        if (customerDAO.getCustomerByCSN(customer.getCSN()) != null) {
            System.err.println("Business Rule Violated: Customer with CSN " + customer.getCSN() + " already exists.");
            return false;
        }
        return customerDAO.addCustomer(customer);
    }

    public String generateNextCustomerId() {
        List<Customer> list = getAllCustomers();
        if (list.isEmpty()) return "C001";
        
        int maxId = 0;
        for (Customer c : list) {
            try {
                // Cắt bỏ chữ "C", lấy phần số
                String numPart = c.getCSN().replaceAll("\\D+", ""); 
                if (!numPart.isEmpty()) {
                    int id = Integer.parseInt(numPart);
                    if (id > maxId) maxId = id;
                }
            } catch (Exception e) { continue; } 
        }
        // Format lại thành C00x
        return String.format("C%03d", maxId + 1);
    }
    
    /**
     * Cập nhật thông tin khách hàng.
     */
    public boolean updateCustomer(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }
    
    /**
     * Xóa khách hàng theo CSN.
     */
    public boolean deleteCustomer(String CSN) {
        return customerDAO.deleteCustomer(CSN);
    }
    
    // Thêm các hàm nghiệp vụ khác (ví dụ: tìm kiếm, thống kê khách hàng thân thiết) nếu cần
}