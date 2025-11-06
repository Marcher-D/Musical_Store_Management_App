package com.yourteamname.store.model;

public abstract class Instrument {
    private String type;
    private String brand;

    public Instrument(String brand, String type){
        this.brand = brand;
        this.type = type;
    }

    String getBrand(){
        return this.brand;
    }

    String getType(){
        return this.type;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }

    public void setType(String type){
        this.type = type;
    }

    public abstract String getDescription();
}
