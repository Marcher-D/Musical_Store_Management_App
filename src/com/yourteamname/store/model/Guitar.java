package com.yourteamname.store.model;

public class Guitar extends Instrument {
    private String category;
    private String model_name;
    private int price;
    private int stock;

    public Guitar(String brand, String type, String category, String model_name, int price, int stock){
        super(brand, type);

        this.category = category;
        this.model_name = model_name;
        this.price = price;
        this.stock = stock;
    }

    public String getCategory(){
        return this.category;
    }

    public String getModel(){
        return this.model_name;
    }

    public int getPrice(){
        return this.price;
    }

    public int getStock(){
        return this.stock;
    }

    @Override
    public String getDescription(){
    return "Type: " + getType() 
           + " | Brand: " + getBrand() 
           + " | Model: " + getModel() 
           + " | Price: $" + getPrice() 
           + " | Stock: " + getStock();
    }  
}
