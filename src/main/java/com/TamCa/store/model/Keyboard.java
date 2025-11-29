package com.TamCa.store.model;

import java.util.Date;

public class Keyboard extends Instrument {
    
    private String cateKey;     
    private int keyNumKey;      
    private boolean hasLCD;     

    public Keyboard(
            // 8 parameters for Product
            String id, String namePro, String catePro, String origin, 
            String brand, int quantityInStock, Date importDate, double sellingPrice,
            
            // 4 parameters for Instrument
            String cateIns, String mateIns, String colorIns, boolean isElectric,
            
            // 3 parameters for Keyboard
            String cateKey, int keyNumKey, boolean hasLCD
    ) {
        super(id, namePro, catePro, origin, brand, quantityInStock, importDate, sellingPrice,
              cateIns, mateIns, colorIns, isElectric);
        
        this.cateKey = cateKey;
        this.keyNumKey = keyNumKey;
        this.hasLCD = hasLCD;
    }

    // --- Getters / Setters ---

    public String getCateKey() {
        return cateKey;
    }

    public void setCateKey(String cateKey) {
        this.cateKey = cateKey;
    }

    public int getKeyNumKey() {
        return keyNumKey;
    }

    public void setKeyNumKey(int keyNumKey) {
        this.keyNumKey = keyNumKey;
    }

    public boolean isHasLCD() {
        return hasLCD;
    }

    public void setHasLCD(boolean hasLCD) {
        this.hasLCD = hasLCD;
    }

    @Override
    public String getDescription() {
        return "Keyboard ID: " + getId() + 
               "\n Name: " + getNamePro() +
               "\n Brand: " + getBrand() +
               "\n Price: " + getSellingPrice() +
               "\n Category: " + this.cateKey +
               "\n Number of Keys: " + this.keyNumKey +
               "\n Has LCD: " + (this.hasLCD ? "Yes" : "No");
    }
}
