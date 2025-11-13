package com.yourteamname.store.model;

public abstract class Product {
    private String id, brand;
    private double sellingPrice;
    private int quantityInStock;

    public Product(String id, String brand, double price, int quantity){
        this.id = id;
        this.brand = brand;
        this.sellingPrice = price;
        this.quantityInStock = quantity;
    }

    public String getBrand(){
        return this.brand;
    }

    public String getId(){
        return this.id;
    }

    public double getSellingPrice(){
        return this.sellingPrice;
    }

    public int getQuantityInStock(){
        return this.quantityInStock;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }

    public void setPrice(double price){
        this.sellingPrice = price;
    }

    public void setQuantity(int quantity){
        this.quantityInStock = quantity;
    }

    public abstract String getDescription();
}
