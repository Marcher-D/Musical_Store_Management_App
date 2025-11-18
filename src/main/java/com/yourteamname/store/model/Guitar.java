package com.yourteamname.store.model;
import java.util.Date;

public class Guitar extends Instrument {
    private String cateGui;
    private int strNumGui;
    private String bodyShapeGui;

    public Guitar(
        // 8 parameters of Product
        String id, String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,

        // 4 parameters of Instrument
        String mateIns,
        String cateIns,
        String colorIns,
        boolean isElectric,

        // 3 parameters of Guitar
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

    public void setNumStrGui(int numStrGui){
        this.strNumGui = numStrGui;
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
