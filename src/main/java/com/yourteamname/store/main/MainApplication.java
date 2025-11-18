package com.yourteamname.store.main; 

import com.yourteamname.store.service.InventoryManager;

import com.yourteamname.store.model.Product;

import java.util.Date;
import java.util.List;

public class MainApplication {

    public static void main(String[] args) {

        System.out.println("InventoryManager System has started...");
        InventoryManager manager = new InventoryManager(); // tạo list of Products
        
        Date importDate = new Date(); // Lấy ngày giờ hiện tại

        System.out.println("----------------------------------------");
        System.out.println("Adding new items...");

        // add guitar
        manager.addNewGuitar(
            "Fender Stratocaster", "Instrument", "USA", "Fender", 10, importDate, 1500.0,
            "Electric Guitar", "Alder", "Sunburst", true,
            "Electric", 6, "Stratocaster-Shape"
        );

        // add more guitar
        manager.addNewGuitar(
            "Taylor 814ce", "Instrument", "USA", "Taylor", 5, importDate, 2999.0,
            "Acoustic Guitar", "Rosewood", "Natural", false,
            "Acoustic", 6, "Grand Auditorium"
        );

        // add pinoa
        manager.addNewPiano(
            "Yamaha U1", "Instrument", "Japan", "Yamaha", 3, importDate, 5000.0,
            "Acoustic Piano", "Spruce", "Ebony", false,
            "Upright", 88, true
        );

        // Add keyboard
        manager.addNewKeyboard(
            "Korg Kronos", "Instrument", "Japan", "Korg", 7, importDate, 3500.0,
            "Digital Keyboard", "Plastic", "Black", true,
            "Workstation", 88, true
        );

        // add drumkit
        manager.addNewDrum(
            "Tama Starclassic", "Instrument", "Japan", "Tama", 4, importDate, 2200.0,
            "Acoustic Drum", "Maple", "Cherry Red", false,
            5, 4, "Remo", "Maple"
        );

        System.out.println("...Finished adding items.");
        System.out.println("----------------------------------------");
        System.out.println("Checking inventory:");

        // Get all products
        List<Product> allProducts = manager.getAllItems();

        // In ra mô tả của từng sản phẩm (Test tính đa hình)
        for (Product product : allProducts) {
            System.out.println(product.getDescription());
            System.out.println("---");
        }
        
        System.out.println("----------------------------------------");
        System.out.println("Total items in inventory: " + allProducts.size());
        System.out.println("Total inventory value: $" + manager.totalValue());
    }
}