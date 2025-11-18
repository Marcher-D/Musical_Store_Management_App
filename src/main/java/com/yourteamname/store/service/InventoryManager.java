package com.yourteamname.store.service;

import com.yourteamname.store.model.DrumKit;
import com.yourteamname.store.model.Guitar;
import com.yourteamname.store.model.Keyboard;
import com.yourteamname.store.model.Piano;
import com.yourteamname.store.model.Product;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;


public class InventoryManager {

    private List<Product> inventory;

    // nhớ code logic loadData() để cập nhật/lưu bộ đếm này nha thầy
    private int guitarCounter = 1;
    private int pianoCounter = 1;
    private int keyboardCounter = 1;
    private int drumCounter = 1;
    private int accessoryCounter = 1;

    private static final String GUITAR_PREFIX = "111";
    private static final String PIANO_PREFIX = "112";
    private static final String DRUM_PREFIX = "114";
    private static final String KEYBOARD_PREFIX = "113";


    public InventoryManager(){
        this.inventory = new ArrayList<>();
        // gọi loadData() ở đây
    }

    private String generateNextGuitarID(){
        String formatCounter = String.format("%03d", this.guitarCounter);
        return GUITAR_PREFIX + formatCounter;
    }

    private String generateNextPianoId() {
        String formattedCounter = String.format("%03d", this.pianoCounter);
        return PIANO_PREFIX + formattedCounter;
    }

    private String generateNextKeyboardId() {
        String formattedCounter = String.format("%03d", this.keyboardCounter);
        return KEYBOARD_PREFIX + formattedCounter;
    }

    private String generateNextDrumId() {
        String formattedCounter = String.format("%03d", this.drumCounter);
        return DRUM_PREFIX + formattedCounter;
    }

    public void addNewGuitar(
        // 7 parameters of Product
        String namePro, String catePro, String origin, 
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
    ) {
        String nextGuiId = generateNextGuitarID();
        guitarCounter += 1;
        Guitar newGuitar = new Guitar(
                   nextGuiId, namePro, catePro, origin, brand,
                   quantityInStock, importDate, sellingPrice,
                   
                   cateIns, mateIns, colorIns, isElectric,
                   
                   cateGui, numOfString, bodyShapeGui);

        this.addItem(newGuitar);
    }

    public void addNewKeyboard(
            // 7 parameters of Product
            String namePro, String catePro, String origin, 
            String brand, int quantityInStock, Date importDate, double sellingPrice,

            // 4 parameters of Instrument
            String cateIns,
            String mateIns,
            String colorIns,
            boolean isElectric,

            // 3 parameters of Keyboard
            String cateKey,
            int keyNumKey,
            boolean hasLCD
    ) {
        String nextKeyboardId = generateNextKeyboardId();
        keyboardCounter += 1;
        
        // Create new Keyboard object with 15 params
        Keyboard newKeyboard = new Keyboard(
                // 8 Product params
                nextKeyboardId, namePro, catePro, origin, brand, 
                quantityInStock, importDate, sellingPrice,
                
                // 4 Instrument params
                cateIns, mateIns, colorIns, isElectric,
                
                // 3 Keyboard params
                cateKey, keyNumKey, hasLCD
        );

        this.addItem(newKeyboard);
    }

    public void addNewDrum(
        // 7 parameters of Product
        String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,

        // 4 parameters of Instrument
        String mateIns,
        String cateIns,
        String colorIns,
        boolean isElectric,

        // 4 paramters of DrumKit
        int numOfDrumPieces,
        int numOfCymbals,
        String headMaterial,
        String shellMaterial
    ) {
        String nextDrumId = generateNextDrumId();
        drumCounter += 1;
        DrumKit newDrumKit = new DrumKit(
                // 8 arguments for Product
                nextDrumId, namePro, catePro, origin, brand, 
                quantityInStock, importDate, sellingPrice,
                
                // 4 arguments for Instrument
                cateIns, mateIns, colorIns, isElectric,
                
                // 4 arguments DrumKit
                numOfDrumPieces, numOfCymbals, headMaterial, shellMaterial
        );

        this.addItem(newDrumKit);
    }

    public void addNewPiano(
        // 7 parameters of Product
        String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,

        // 4 parameters of Instrument
        String mateIns,
        String cateIns,
        String colorIns,
        boolean isElectric,

        // 3 paramters of Piano
        String catePi,
        int keyNumPi,
        boolean hasPedal
        ) {
            String nextPianoId = generateNextPianoId();
            pianoCounter += 1;
            Piano newPiano = new Piano(

                nextPianoId, namePro, catePro, origin, brand, 
                quantityInStock, importDate, sellingPrice,
                
                cateIns, mateIns, colorIns, isElectric,
                
                catePi, keyNumPi, hasPedal
        );

        this.addItem(newPiano);
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

        if (itemToRemove != null){
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

    public void updatePrice(String itemId, double newPrice){
        
        Product updatedItem = findItem(itemId);
        if (updatedItem == null){
            System.out.println("No item with that ID is found!");
            return;
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
