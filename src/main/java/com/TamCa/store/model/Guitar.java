package com.TamCa.store.model;
import java.util.Date;

public class Guitar extends Instrument {
    private String cateGui;
    private int strNumGui;
    private String bodyShapeGui;

    public Guitar(
        // parameters of Product
        String id, String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,

        // parameters of Instrument
        String cateIns,
        String mateIns,
        String colorIns,
        boolean isElectric,

        // parameters of Guitar
        String cateGui,
        int numOfString,
        String bodyShapeGui
        ) 
        
        {
            super(id, namePro, catePro, origin, brand, quantityInStock, importDate, sellingPrice,
                  cateIns, mateIns, colorIns, isElectric);

            this.cateGui = cateGui;
            this.strNumGui = numOfString;
            this.bodyShapeGui = bodyShapeGui;
        }
    
    // getter and setter
    public String getGuiCategory(){
        return this.cateGui;
    }

    public int getStrNumGui(){
        return this.strNumGui;
    }

    public String getBodyShapeGui(){
        return this.bodyShapeGui;
    }

    public void setGuiCategory(String cateGui){
        this.cateGui = cateGui;
    }

    public void setStrNumGui(int strNumGui){
        this.strNumGui = strNumGui;
    }

    public void setBodyShapeGui(String bodyShapeGui){
        this.bodyShapeGui = bodyShapeGui;
    }

    @Override
    public String getDescription(){
    return "Guitar ID: " + getId() + 
        "\n Model: " + getNamePro() +
        "\n Brand: " + getBrand() +
        "\n Price: " + getSellingPrice() +
        "\n Category: " + getGuiCategory() +
        "\n Number of strings: " + getStrNumGui()+
        "\n Guitar shape: " + getBodyShapeGui();
    }
}
