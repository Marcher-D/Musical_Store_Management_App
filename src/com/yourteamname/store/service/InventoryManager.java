package com.yourteamname.store.service;

import com.yourteamname.store.model.Product;
import java.util.List;
import java.util.ArrayList;


public class InventoryManager {

    private List<Product> inventory;

    public InventoryManager(){
        this.inventory = new ArrayList<>();
    }

    public void addItem(Product item){
        if (item != null){
            this.inventory.add(item);
        }
    }

    public List<Product> getAllItems(){
        return inventory;
    }

    public Product findItem(String itemId){

        if (inventory != null && itemId != null){
            for (Product item : inventory){
                if (item.getId().equals(itemId)){
                    return item;
                }
            }
        }
        // nhớ gọi saveData()
        return null;
    }

    public boolean deleteItem(String itemId){
        Product itemToRemove = findItem(itemId);

        if (itemId != null){
            inventory.remove(itemToRemove);
            return true;
        }
        return false;

        // nhớ gọi saveData()
    }

    // update stock, price, count totalvalue

    public void updateStock(String itemId, int newStock){
        
        Product updatedItem = findItem(itemId);
        if (updatedItem == null){
            System.out.println("No item with that ID is found!");
            return;
        }

        int currStock = updatedItem.getQuantityInStock();
        int updatedStock = currStock + newStock;
        updatedItem.setQuantity(updatedStock);
    }

    public void updatePrice(String itemId, int newPrice){
        
        Product updatedItem = findItem(itemId);
        if (updatedItem == null){
            System.out.println("No item with that ID is found!");
        }

        updatedItem.setPrice(newPrice);
    }
    
    public double totalValue(){
        double totalValue = 0;
        for(Product product : inventory){
            double productValue = product.getSellingPrice() * product.getQuantityInStock();
            totalValue += productValue;
        }

        return totalValue;        
    }

}
