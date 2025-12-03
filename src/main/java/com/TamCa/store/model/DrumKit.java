package com.TamCa.store.model;
import java.util.Date;

public class DrumKit extends Instrument {
    int numOfDrumPieces;
    int numOfCymbals;

    String headMaterial;
    String shellMaterial;
    
    public DrumKit(
        // parameters of Product
        String id, String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,

        // parameters of Instrument
        String cateIns,
        String mateIns,
        String colorIns,
        boolean isElectric,

        // parameters of DrumKit
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

    // getter and setter
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
    return "Drum ID: " + getId() + 
        "\n Brand: " + getBrand() +
        "\n Price: " + getSellingPrice() +
        "\n Category: " + getCateIns() +
        "\n Head material: " + getHeadMaterial() +
        "\n Shell material: " + getShellMaterial();
    }
}
