package com.yourteamname.store.model;
import java.util.Date;

public abstract class Product {
    private String id, brand;
    private double sellingPrice;
    private int quantityInStock;

    private String namePro;       // Tên sản phẩm (ví dụ: "Stratocaster")
    private String catePro;       // Danh mục gốc (ví dụ: "Instrument")
    private String origin;        // Nguồn gốc (ví dụ: "USA")
    private Date importDate;      // Ngày nhập hàng

    public Product(String id, String namePro, String catePro, String origin, 
                   String brand, int quantityInStock, Date importDate, double sellingPrice)
    {
        this.id = id;
        this.namePro = namePro;
        this.catePro = catePro;
        this.origin = origin;
        this.brand = brand;
        this.quantityInStock = quantityInStock;
        this.importDate = importDate;
        this.sellingPrice = sellingPrice;
    }

    public String getId(){
        return this.id;
    }

    public String getBrand(){
        return this.brand;
    }

    public double getSellingPrice(){
        return this.sellingPrice;
    }

    public int getQuantityInStock(){
        return this.quantityInStock;
    }

    public String getNamePro() {
        return namePro;
    }

    public String getCatePro() {
        return catePro;
    }

    public String getOrigin() {
        return origin;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setPrice(double price) {
        this.sellingPrice = price;
    }

    public void setQuantity(int quantity) {
        this.quantityInStock = quantity;
    }

    public void setNamePro(String namePro) {
        this.namePro = namePro;
    }

    public void setCatePro(String catePro) {
        this.catePro = catePro;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public abstract String getDescription();
}
