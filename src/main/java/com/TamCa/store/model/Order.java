package com.TamCa.store.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements OrderPersonnelData {
    
    private int orderId;
    private String status;
    private String deliAdd;
    private Date sellDate;
    private Date deliDate;

    private Customer customer;
    private List<OrderDetail> items; // Danh sách hàng

    public Order(String status, String deliAdd, Date sellDate, Date deliDate, Customer customer) {
        this.status = status;
        this.deliAdd = deliAdd;
        this.sellDate = sellDate;
        this.deliDate = deliDate;
        this.customer = customer;
        this.items = new ArrayList<>(); 
    }
    
    // getter and setter
    public void setOrderId(int orderId) { 
        this.orderId = orderId; 
    }

    public int getOrderId() { 
        return orderId; 
    }

    public void setItems(List<OrderDetail> items) { 
        this.items = items; 
    }

    public List<OrderDetail> getItems() { 
        return this.items; 
    }

    // method to get the total amount
    public double getTotalAmount() {
        double total = 0;
        for (OrderDetail item : items){
            total += item.getTotalPrice();
        }
        return total;
    }

    // interface implement here
    @Override
    public String getCustomerCSN() { return (customer != null) ? customer.getCSN() : null; }
    @Override
    public String getEmployeeEID() { return "N/A"; }

    public String getStatus() { 
        return this.status; 
    }

    public String getDeliAdd() { 
        return this.deliAdd; 
    }

    public Date getSellDate() { 
        return this.sellDate; 
    }

    public Date getDeliDate() { 
        return this.deliDate; 
    }

    public Customer getCustomer() { 
        return this.customer; 
    }

    // the Inner Class - OrderDetail
    public static class OrderDetail {
        private int orderDetailId; 
        private int orderId;
        private Product product;
        private int quantity;
        private double priceAtSale;

        public OrderDetail(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
            this.priceAtSale = product.getSellingPrice();
        }

        public double getTotalPrice() {
            return this.quantity * this.priceAtSale;
        }

        // getter and setter
        public Product getProduct() { 
            return product; 
        }

        public void setProduct(Product product) { 
            this.product = product; 
        }

        public int getQuantity() { 
            return quantity; 
        }

        public void setQuantity(int quantity) { 
            this.quantity = quantity; 
        }

        public double getPriceAtSale() { 
            return priceAtSale; 
        }

        public void setPriceAtSale(double priceAtSale) { 
            this.priceAtSale = priceAtSale; 
        }

        public int getOrderDetailId() { 
            return orderDetailId; 
        }

        public void setOrderDetailId(int orderDetailId) { 
            this.orderDetailId = orderDetailId; 
        }

        public int getOrderId() { 
            return orderId; 
        }

        public void setOrderId(int orderId) { 
            this.orderId = orderId; 
        }
    }
}

// interface 
interface OrderPersonnelData {
    String getCustomerCSN();
    String getEmployeeEID();
}