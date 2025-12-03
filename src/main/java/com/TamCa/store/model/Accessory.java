package com.TamCa.store.model;

import java.util.Date;

public class Accessory extends Product {
    private String cateAcc, mateAcc, colorAcc, compatibleWith;

    public Accessory(
        // parameters of Product
        String id, String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,
        
        String cateAcc,
        String mateAcc,
        String colorAcc,
        String compatibleWith 
    ) {
        super(id, namePro, catePro, origin, brand, quantityInStock, importDate, sellingPrice);

        this.cateAcc = cateAcc;
        this.mateAcc = mateAcc;
        this.colorAcc = colorAcc;
        this.compatibleWith = compatibleWith;
    }

    // getter & setter
    
    final public String getCateAcc() { return this.cateAcc; }
    final public String getMateAcc() { return this.mateAcc; }
    final public String getColorAcc() { return this.colorAcc; }
    final public String getCompatibleWith() { return this.compatibleWith; } 
    
    public void setCateAcc(String cateAcc) { this.cateAcc = cateAcc; }
    public void setMateAcc(String mateAcc) { this.mateAcc = mateAcc; }
    public void setColorAcc(String colorAcc) { this.colorAcc = colorAcc; }
    public void setCompatibleWith(String compatibleWith) { this.compatibleWith = compatibleWith; } 
    
    @Override
    public String getDescription(){
        return "Accessory ID: " + getId() +
               "\n Product Name: " + getNamePro() +
               "\n Brand: " + getBrand() +
               "\n Category: " + getCateAcc() +
               "\n Material: " + getMateAcc() +
               "\n Color: " + getColorAcc() +
               "\n Compatible With: " + getCompatibleWith() +
               "\n Selling Price: " + getSellingPrice() +
               "\n Quantity in Stock: " + getQuantityInStock();
    }
}