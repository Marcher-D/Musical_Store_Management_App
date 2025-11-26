package com.yourteamname.store.model;

import java.util.Date;

// --- INTERFACE ---
interface OrderPersonnelData {
    
    // Phương thức bắt buộc phải triển khai để lấy Mã số Khách hàng
    String getCustomerCSN();
    
    // Phương thức bắt buộc phải triển khai để lấy Mã số Nhân viên
    String getEmployeeEID();
}

// --- CLASS ORDER (Triển khai Interface) ---
public class Order implements OrderPersonnelData {
    
    private String status, deliAdd;
    private Date sellDate, deliDate;

    // Composition: Order HAS-A Customer, Order HAS-A Employee
    private Customer customer;
    private Employee employee; 

    public Order(String status, String deliAdd, Date sellDate, Date deliDate, 
                 Customer customer, Employee employee) {
        
        this.status = status;
        this.deliAdd = deliAdd;
        this.sellDate = sellDate;
        this.deliDate = deliDate;

        this.customer = customer;
        this.employee = employee; 
    }

    // --- TRIỂN KHAI PHƯƠNG THỨC TỪ INTERFACE (Delegation) ---
    @Override
    public String getCustomerCSN() {
        // Ủy quyền (Delegate) cho đối tượng Customer để lấy dữ liệu
        return this.customer.getCSN(); 
    }

    @Override
    public String getEmployeeEID() {
        // Ủy quyền (Delegate) cho đối tượng Employee để lấy dữ liệu
        return this.employee.getEID();
    }
    
    // --- GETTERS & SETTERS CỦA ORDER ---
    final public String getStatus(){ return this.status; }
    final public String getDeliAdd(){ return this.deliAdd; }
    final public Date getSellDate(){ return this.sellDate; }
    final public Date getDeliDate(){ return this.deliDate; }
    
    public void setStatus(String status){ this.status = status; }
    public void setDeliAdd(String deliAdd){ this.deliAdd = deliAdd; }
    public void setSellDate(Date sellDate){ this.sellDate = sellDate; }
    public void setDeliDate(Date deliDate){ this.deliDate = deliDate; }

    // --- GETTERS CHO COMPOSITION ---
    final public Customer getCustomer() { return this.customer; }
    final public Employee getEmployee() { return this.employee; }
    
    public String getDescription(){
        return "Status: " + getStatus() + 
            "\nSelling Date: " + getSellDate() +
            "\nDelivering Date: " + getDeliDate() +
            "\nDelivering Address: " + getDeliAdd() +
            "\nCSN: " + getCustomerCSN() + 
            "\nEID: " + getEmployeeEID();
    }
}