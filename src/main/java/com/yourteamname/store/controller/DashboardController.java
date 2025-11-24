package com.yourteamname.store.controller;

import com.yourteamname.store.model.Product;
import com.yourteamname.store.service.InventoryManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    
    // bảng dữ liệu
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colBrand;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, String> colCategory;

    // màn hình chính
    @FXML private AnchorPane homeView;
    @FXML private VBox cardProduct;
    @FXML private VBox cardCustomer;
    @FXML private VBox cardRevenue;

    @FXML private AreaChart<String, Number> revenueChart;
    @FXML private javafx.scene.chart.XYChart.Series<String, Number> currentSeries;

    // sidebar
    @FXML private Button btnHome;
    @FXML private Button btnProducts;

    // logic và data
    private InventoryManager inventoryManager;
    private ObservableList<Product> productList;

    // --- KHAI BÁO FORM NHẬP LIỆU MỚI ---
    @FXML private AnchorPane addProductView; // Cái màn hình Form
    
    
    // SECTION 1: Product Info (Bro đã làm)
    @FXML private TextField txtNamePro;
    @FXML private TextField txtBrand;   
    @FXML private TextField txtOrigin;  
    @FXML private TextField txtPrice;   
    @FXML private TextField txtQuantity;
    @FXML private TextField txtCatePro; 
    @FXML private DatePicker dpImportDate; 

    // SECTION 2: Instrument Info 
    @FXML private TextField txtCateIns; 
    @FXML private TextField txtMateIns; 
    @FXML private TextField txtColorIns; 
    @FXML private CheckBox chkIsElectric; // Dùng Checkbox cho boolean

    // SECTION 3: Guitar Info
    @FXML private TextField txtCateGui; 
    @FXML private TextField txtNumStr; 
    @FXML private TextField txtBodyShape;
    

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

        // thêm vụ click vào 3 cái thẻ ở homepage
        setupChart("Revenue");

        cardProduct.setOnMouseClicked(event -> {setupChart("Products");});

        cardCustomer.setOnMouseClicked(event -> {setupChart("Customers");});

        cardRevenue.setOnMouseClicked(event -> {setupChart("Revenue");});
        showHome();
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

    private void resetButtonStyles() {
        btnHome.getStyleClass().remove("active");
        btnProducts.getStyleClass().remove("active");
    }

    @FXML 
    private void showHome() {
        homeView.setVisible(true);
        homeView.toFront();
        productTable.setVisible(false);

        resetButtonStyles();

        btnHome.getStyleClass().add("active");
    }

    @FXML
    private void showProducts() {
        homeView.setVisible(false); // Ẩn Home
        productTable.setVisible(true);
        productTable.toFront(); // Đưa Bảng lên trên
        
        resetButtonStyles();

        btnProducts.getStyleClass().add("active");
    }

    @FXML
    private void showAddProductForm() {
        // Ẩn bảng, Hiện form
        productTable.setVisible(false);
        homeView.setVisible(false);
        
        addProductView.setVisible(true);
        addProductView.toFront();
        
        // Reset form mỗi khi mở
        clearForm();
    }

    @FXML
    private void handleCancelAdd() {
        // Quay về bảng
        addProductView.setVisible(false);
        productTable.setVisible(true);
    }

    private void clearForm() {
        // Xóa dữ liệu cũ nếu có
        if(txtNamePro != null) txtNamePro.clear();
        if(txtBrand != null) txtBrand.clear();
        if(txtPrice != null) txtPrice.clear();
        if(txtQuantity != null) txtQuantity.clear();
        if(dpImportDate != null) dpImportDate.setValue(LocalDate.now());
        // ... Clear tiếp các trường khác khi bro vẽ xong
    }

    @FXML
    private void handleSaveProduct() {
        try {
            System.out.println("Saving product...");

            // --- A. LẤY DỮ LIỆU TỪ UI ---
            String name = txtNamePro.getText();
            String brand = txtBrand.getText();
            String origin = txtOrigin.getText(); 
            String catePro = "Instrument"; // Tạm thời fix cứng hoặc lấy từ txtCatePro
            
            // Xử lý số (Có thể gây lỗi nếu để trống -> Cần try catch)
            double price = Double.parseDouble(txtPrice.getText());
            int quantity = Integer.parseInt(txtQuantity.getText());
            
            // Xử lý ngày tháng (DatePicker -> java.util.Date)
            LocalDate localDate = dpImportDate.getValue();
            java.util.Date importDate = java.sql.Date.valueOf(localDate);

            // --- B. DỮ LIỆU CÒN THIẾU (7 Trường bro CHƯA VẼ) ---
            // Tui để giá trị mặc định để test logic Save. 
            // Khi nào bro vẽ xong UI thì thay bằng txtMateIns.getText()...
            
            String mateIns = "Unknown Wood"; // Thay bằng: txtMateIns.getText()
            String cateIns = "String";       // Thay bằng: txtCateIns.getText()
            String colorIns = "Black";       // Thay bằng: txtColorIns.getText()
            boolean isElectric = false;      // Thay bằng: chkIsElectric.isSelected()
            
            String cateGui = "Electric";     // Thay bằng: txtCateGui.getText()
            int numStr = 6;                  // Thay bằng: Integer.parseInt(txtNumStr.getText())
            String bodyShape = "Standard";   // Thay bằng: txtBodyShape.getText()

            // --- C. GỌI SERVICE ---
            inventoryManager.addNewGuitar(
                name, catePro, origin, brand, quantity, importDate, price, // 7 cái có thật
                cateIns, mateIns, colorIns, isElectric,                    // 4 cái Instrument
                cateGui, numStr, bodyShape                                 // 3 cái Guitar
            );

            // --- D. THÀNH CÔNG ---
            System.out.println("✅ Saved: " + name);
            
            // Nạp lại bảng và đóng form
            loadDataToTable();
            handleCancelAdd();

        } catch (NumberFormatException e) {
            System.out.println("❌ Lỗi: Giá hoặc Số lượng phải là số!");
            // Sau này mình sẽ hiện thông báo lỗi (Alert) ở đây
        } catch (Exception e) {
            System.out.println("❌ Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupChart(String type){
        revenueChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(type + " Overview");

        switch (type){
            case "Products":
                series.getData().add(new javafx.scene.chart.XYChart.Data<>("Jan", 50));
                series.getData().add(new javafx.scene.chart.XYChart.Data<>("Feb", 80));
                series.getData().add(new javafx.scene.chart.XYChart.Data<>("Mar", 150));
                revenueChart.setTitle("Monthly Product Import");
                break;
            case "Customers":
                // data ảo
                series.getData().add(new javafx.scene.chart.XYChart.Data<>("Jan", 20));
                series.getData().add(new javafx.scene.chart.XYChart.Data<>("Feb", 45));
                series.getData().add(new javafx.scene.chart.XYChart.Data<>("Mar", 90));
                revenueChart.setTitle("Customers Growth");
                break;
            case "Revenue":
                series.getData().add(new javafx.scene.chart.XYChart.Data<>("Jan", 5000));
                series.getData().add(new javafx.scene.chart.XYChart.Data<>("Feb", 12000));
                series.getData().add(new javafx.scene.chart.XYChart.Data<>("Mar", 25000));
                revenueChart.setTitle("Monthly Revenue ($)");
                break;
        }
        revenueChart.getData().add(series);
    }

    private void clearInputFields() {
        if(txtNamePro != null) txtNamePro.clear();
        if(txtBrand != null) txtBrand.clear();
        // ... clear các ô khác
    }
}
