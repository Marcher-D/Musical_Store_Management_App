package com.yourteamname.store.model;

public abstract class Product {
    private String id, nameProduct, cateProduct, brand;
    private double sellingPrice;
    private int quantityInStock;

    final public String getBrand(){
        return this.brand;
    }

    final public String getName(){
        return this.nameProduct;
    }

    final public String getCate(){
        return this.cateProduct;
    }

    final public String getId(){
        return this.id;
    }

    final public double getSellingPrice(){
        return this.sellingPrice;
    }

    final public int getQuantityInStock(){
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
