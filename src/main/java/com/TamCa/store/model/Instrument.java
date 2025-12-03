package com.TamCa.store.model;
import java.util.Date;

public abstract class Instrument extends Product {
    private String cateIns;
    private String mateIns;
    private String colorIns;
    private boolean isElectric;

    public Instrument(

        // parameter of Product
        String id, String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,

        String cateIns,
        String mateIns,
        String colorIns,
        boolean isElectric
    ) {
        super(id, namePro, catePro, origin, brand, quantityInStock, importDate, sellingPrice);

        this.cateIns = cateIns;
        this.mateIns = mateIns;
        this.colorIns = colorIns;
        this.isElectric = isElectric;
    }

    // getter and setter
    public String getCateIns(){
        return this.cateIns;
    }

    public String getMateIns(){
        return this.mateIns;
    }

    public String getColorIns(){
        return this.colorIns;
    }

    public boolean isElectric(){
        return this.isElectric;
    }

    public void setCateIns(String category){
        this.cateIns = category;
    }

    public void setMateIns(String material){
        this.mateIns = material;
    }

    public void setColorIns(String color){
        this.colorIns = color;
    }

    public void setIsElectric(boolean isElectric){
        this.isElectric = isElectric;
    }
}
