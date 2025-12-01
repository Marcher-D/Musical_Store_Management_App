package com.TamCa.store.model;

import java.util.Date;

public abstract class Product {
    
    private String id, namePro, catePro, brand, origin; 
    private double sellingPrice;
    private int quantityInStock;
    private Date importDate; 

    public Product(String id, String namePro, String catePro, String origin, 
                   String brand, int quantityInStock, Date importDate, double sellingPrice) {
        this.id = id;
        this.namePro = namePro;
        this.catePro = catePro;
        this.origin = origin;
        this.brand = brand;
        this.quantityInStock = quantityInStock;
        this.importDate = importDate;
        this.sellingPrice = sellingPrice;
    }

    // --- Getters ---
    
    public String getId() {
        return this.id;
    }

    public String getBrand() {
        return this.brand;
    }
    
    public double getSellingPrice() {
        return this.sellingPrice;
    }

    public int getQuantityInStock() {
        return this.quantityInStock;
    }

    public String getNamePro() {
        return this.namePro;
    }

    public String getCatePro() {
        return this.catePro;
    }

    public String getOrigin() {
        return this.origin;
    }

    public Date getImportDate() {
        return this.importDate;
    }

    // --- Setters ---

    public void setPrice(double price) {
        this.sellingPrice = price;
    }

    public void setQuantity(int quantity) {
        this.quantityInStock = quantity;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
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

    @Override
    public String toString() {
        return namePro + " [" + id + "] - $" + sellingPrice;
    }
}