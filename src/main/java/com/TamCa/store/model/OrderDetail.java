package com.TamCa.store.model;

public class OrderDetail {
    private int orderDetailId; // ID tự tăng trong DB (có thể để 0 khi tạo mới)
    private int orderId;       // Link tới Order cha
    private Product product;   // Link tới sản phẩm (Composition)
    private int quantity;      // Số lượng mua
    private double priceAtSale; // Giá tại thời điểm bán (Quan trọng, vì giá gốc có thể đổi sau này)

    public OrderDetail(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.priceAtSale = product.getSellingPrice(); // Lấy giá hiện tại làm giá bán
    }

    // Constructor đầy đủ khi load từ DB lên
    public OrderDetail(int orderDetailId, int orderId, Product product, int quantity, double priceAtSale) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.priceAtSale = priceAtSale;
    }

    public double getTotalPrice() {
        return this.quantity * this.priceAtSale;
    }

    // --- GETTERS & SETTERS ---
    public int getOrderDetailId() { return orderDetailId; }
    public void setOrderDetailId(int orderDetailId) { this.orderDetailId = orderDetailId; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPriceAtSale() { return priceAtSale; }
    public void setPriceAtSale(double priceAtSale) { this.priceAtSale = priceAtSale; }
}
