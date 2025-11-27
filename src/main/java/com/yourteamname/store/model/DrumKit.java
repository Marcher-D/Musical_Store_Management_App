package com.yourteamname.store.model;
import java.util.Date;

public class DrumKit extends Instrument {
    int numOfDrumPieces;
    int numOfCymbals;

    String headMaterial;
    String shellMaterial;
    
    public DrumKit(
        // 8 parameters of Product
        String id, String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,

        // 4 parameters of Instrument
        String cateIns,
        String mateIns,
        String colorIns,
        boolean isElectric,

        // 4 parameters of DrumKit
        int numOfDrumPieces,
        int numOfCymbals,
        String headMaterial,
        String shellMaterial
    ){
        super(id, namePro, catePro, origin, brand, quantityInStock, importDate, sellingPrice,
                  cateIns, mateIns, colorIns, isElectric);
        this.numOfDrumPieces = numOfDrumPieces;
        this.numOfCymbals = numOfCymbals;
        this.headMaterial = headMaterial;
        this.shellMaterial = shellMaterial;
    }

    public int getNumOfDrumPieces(){
        return this.numOfDrumPieces;
    }

    public int getNumOfCymbals(){
        return this.numOfCymbals;
    }

    public String getHeadMaterial(){
        return this.headMaterial;
    }

    public String getShellMaterial(){
        return this.shellMaterial;
    }

    public void setNumofDrumPieces(int numOfDrumPieces){
        this.numOfDrumPieces = numOfDrumPieces;
    }

    public void setNumofCymbals(int numOfCymbals){
        this.numOfCymbals = numOfCymbals;
    }

    public void setHeadMaterial(String headMaterial){
        this.headMaterial = headMaterial;
    }

    public void setShellMaterial(String shellMaterial){
        this.shellMaterial = shellMaterial;
    }

    @Override
    public String getDescription(){
    return "Drum ID: " + getId() + // lấy từ lớp Product (lớp ông nội)
        "\n Brand: " + getBrand() +
        "\n Price: " + getSellingPrice() +
        "\n Category: " + getCateIns() +
        "\n Head material: " + getHeadMaterial() +
        "\n Shell material: " + getShellMaterial();
    }
}
