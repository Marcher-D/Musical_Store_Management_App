package com.TamCa.store.service;

import com.TamCa.store.dao.CustomerDAO;
import com.TamCa.store.model.Customer;
import java.util.List;

// act like a service layer for Customer
public class CustomerManager {
    
    private final CustomerDAO customerDAO;

    public CustomerManager() {
        this.customerDAO = new CustomerDAO();
    }
    
    // get all the customer from the db
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }
    
    // add a new customer
    public boolean addNewCustomer(Customer customer) {
        // Không cho phép thêm nếu đã tồn tại CSN
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
                String numPart = c.getCSN().replaceAll("\\D+", ""); 
                if (!numPart.isEmpty()) {
                    int id = Integer.parseInt(numPart);
                    if (id > maxId) maxId = id;
                }
            } catch (Exception e) { continue; } 
        }
        // Format lại thành C00%
        return String.format("C%03d", maxId + 1);
    }
    
    // update customer's in4
    public boolean updateCustomer(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }
    
    // delete Customer by using the CSN
    public boolean deleteCustomer(String CSN) {
        return customerDAO.deleteCustomer(CSN);
    }
    
    public boolean addNewCustomer(String csn, String name, String phone, String email, String address) {
        Customer newCus = new Customer(name, csn, phone, email, address);
        return this.addNewCustomer(newCus);
    }
}
