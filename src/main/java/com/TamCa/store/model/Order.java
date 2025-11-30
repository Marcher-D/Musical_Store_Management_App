package com.TamCa.store.model;
import com.TamCa.store.model.OrderPersonnelData;
import com.TamCa.store.model.OrderDetail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Date;



// --- CLASS ORDER ---
public class Order implements OrderPersonnelData {
    
    private int orderId;
    private String status, deliAdd;
    private Date sellDate, deliDate;

    private Customer customer;
    private List<OrderDetail> items;

    // Constructor rút gọn (Bỏ tham số Employee)
    public Order(String status, String deliAdd, Date sellDate, Date deliDate, 
                 Customer customer) {
        this.status = status;
        this.deliAdd = deliAdd;
        this.sellDate = sellDate;
        this.deliDate = deliDate;
        this.customer = customer;
        this.items = new ArrayList<>(); 
    }
    
    // Constructor đầy đủ (Bỏ Employee)
    public Order(int orderId, String status, String deliAdd, Date sellDate, Date deliDate, 
                 Customer customer) {
        this(status, deliAdd, sellDate, deliDate, customer);
        this.orderId = orderId;
    }

    public void addProduct(Product product, int quantity){
        OrderDetail item = new OrderDetail(product, quantity);
        this.items.add(item);
    }

    public void setItems(List<OrderDetail> items) {
        this.items = items;
    }

    public List<OrderDetail> getItems() { return this.items; }

    public double getTotalAmount() {
        double total = 0;
        for (OrderDetail item : items){
            total += item.getTotalPrice();
        }
        return total;
    }



    @Override
    public String getCustomerCSN() { return (customer != null) ? customer.getCSN() : "N/A"; }

    @Override
    public String getEmployeeEID() { 
        return "N/A";
    }
        
    // --- GETTERS & SETTERS ---
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    final public String getStatus(){ return this.status; }
    final public String getDeliAdd(){ return this.deliAdd; }
    final public Date getSellDate(){ return this.sellDate; }
    final public Date getDeliDate(){ return this.deliDate; }
    
    public void setStatus(String status){ this.status = status; }
    public void setDeliAdd(String deliAdd){ this.deliAdd = deliAdd; }
    public void setSellDate(Date sellDate){ this.sellDate = sellDate; }
    public void setDeliDate(Date deliDate){ this.deliDate = deliDate; }

    final public Customer getCustomer() { return this.customer; }
    
    public String getDescription(){
        return "Order ID: " + getOrderId() +
               "\nStatus: " + getStatus() + 
               "\nTotal: $" + getTotalAmount();
    }
}