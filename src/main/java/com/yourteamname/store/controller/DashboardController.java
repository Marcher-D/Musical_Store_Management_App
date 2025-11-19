package com.yourteamname.store.controller;

import com.yourteamname.store.model.Product;
import com.yourteamname.store.service.InventoryManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    
    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> colId;

    @FXML
    private TableColumn<Product, String> colName;

    @FXML
    private TableColumn<Product, String> colBrand;

    @FXML
    private TableColumn<Product, Double> colPrice;

    @FXML
    private TableColumn<Product, String> colCategory;

    private InventoryManager inventoryManager;
    private ObservableList<Product> productList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Dashboard is loading...");

        inventoryManager = new InventoryManager();

        inventoryManager.addNewGuitar(
            "Fender Stratocaster", "Instrument", "USA", "Fender", 10, new Date(), 1500.0,
            "Maple", "Electric", "Sunburst", true, "Solid Body", 6, "Strat"
        );
        
        inventoryManager.addNewPiano(
            "Yamaha U1", "Instrument", "Japan", "Yamaha", 5, new Date(), 5000.0,
            "Spruce", "Acoustic", "Black", false, "Upright", 88, true
        );

        System.out.println("Number of items in inventory: " + inventoryManager.getAllItems().size());

        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamePro()));
        colBrand.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBrand()));        
        colPrice.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSellingPrice()));
        colCategory.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCatePro()));

        loadDataToTable();
    }

    private void loadDataToTable(){
        if (inventoryManager.getAllItems().isEmpty()) {
            System.out.println("Warning: List is empty!");
            return;
        }
        
        // get List from Service, then convert it to an ObservableList object of JavaFX

        productList = FXCollections.observableArrayList(inventoryManager.getAllItems());

        // bring it to the table
        productTable.setItems(productList);
        System.out.println("Successfully loaded data into table!");
    }
}
