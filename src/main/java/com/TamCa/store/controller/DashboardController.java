package com.TamCa.store.controller;

import com.TamCa.store.model.*;
import com.TamCa.store.model.Order.OrderDetail;
import com.TamCa.store.service.*; 
import com.TamCa.store.dao.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.scene.layout.HBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.Optional;

public class DashboardController implements Initializable {
    
    // --- Services and Data ---
    // Khai báo final để đảm bảo khởi tạo ngay từ đầu
    private final InventoryManager inventoryManager = new InventoryManager(); 
    private final CustomerManager customerManager = new CustomerManager(); 
    private final EmployeeManager employeeManager = new EmployeeManager(); 
    private ObservableList<Product> productList;
    private ObservableList<Customer> customerList; 
    private ObservableList<Employee> employeeList; 

    // --- FXML UI Elements ---
    // --- SIDEBAR ---
    @FXML private VBox sidebar;
    @FXML private Button btnMenu;
    @FXML private Button btnHome;
    @FXML private Button btnProducts;
    @FXML private Button btnCustomers;
    @FXML private Button btnEmployees;

    // --- HOME VIEW ---
    @FXML private AnchorPane homeView;
    @FXML private VBox cardProduct;
    @FXML private VBox cardCustomer;
    @FXML private VBox cardRevenue;
    @FXML private AreaChart<String, Number> revenueChart; // FX:ID GỐC TRONG FXML
    @FXML private VBox pnBestSellers;
    // Label Count (FX:ID GỐC TRONG FXML)
    @FXML private Label lblTotalProductsCount;
    @FXML private Label lblTotalCustomersCount;
    @FXML private Label lblTotalRevenueValue;

    // --- PRODUCT VIEW ---
    @FXML private AnchorPane productView;
    @FXML private TableView<Product> productTable;
    // Cột Product (FX:ID GỐC TRONG FXML)
    @FXML private TableColumn<Product, String> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colBrand;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, String> colOrigin;
    @FXML private TableColumn<Product, Integer> colQuantity;
    @FXML private TableColumn<Product, Date> colDate;
    @FXML private TableColumn<Product, String> colInfo;
    @FXML private TextField txtSearchProduct;

    // --- CUSTOMER VIEW ---
    @FXML private AnchorPane customerView;
    @FXML private Label lblTotalCus;
    @FXML private Label lblVipCus;
    @FXML private Label lblNewestCus;
    @FXML private TableView<Customer> customerTable;
    // Cột Customer (FX:ID GỐC TRONG FXML)
    @FXML private TableColumn<Customer, String> colCusName;
    @FXML private TableColumn<Customer, String> colCusCSN;
    @FXML private TableColumn<Customer, String> colCusPhone;
    @FXML private TableColumn<Customer, String> colCusEmail;
    @FXML private TableColumn<Customer, String> colCusAddress;

    @FXML private TextField txtSearchCustomer;
    // --- ADD CUSTOMER FORM ---
    @FXML private AnchorPane addCustomerView;
    @FXML private TextField txtCusNameForm, txtCusCSNForm, txtCusPhoneForm, txtCusEmailForm;
    @FXML private TextArea txtCusAddressForm;

    // --- EMPLOYEE VIEW ---
    @FXML private AnchorPane employeeView;
    @FXML private Label lblTotalEmp;
    @FXML private Label lblTotalSalary;
    @FXML private Label lblManagerName;
    @FXML private TableView<Employee> employeeTable;
    // Cột Employee (FX:ID GỐC TRONG FXML)
    @FXML private TableColumn<Employee, String> colEmpID;
    @FXML private TableColumn<Employee, String> colEmpName;
    @FXML private TableColumn<Employee, String> colEmpPos;
    @FXML private TableColumn<Employee, Integer> colEmpSal;
    @FXML private TableColumn<Employee, Date> colEmpDate;

    @FXML private TextField txtSearchEmployee;
    // --- ADD EMPLOYEE FORM ---
    @FXML private AnchorPane addEmployeeView;
    @FXML private TextField txtEmpIDForm, txtEmpNameForm, txtEmpPosForm, txtEmpSalForm;
    @FXML private DatePicker dpEmpHireDateForm;

    // --- ADD PRODUCT FORM (Giữ nguyên các FXML) ---
    @FXML private AnchorPane addProductView; 
    @FXML private TitledPane tpInstrumentInfo;
    @FXML private TitledPane tpDetailInfo;
    @FXML private TextField txtNamePro, txtBrand, txtOrigin, txtPrice, txtQuantity;
    @FXML private ComboBox<String> cbCatePro; 
    @FXML private DatePicker dpImportDate; 
    @FXML private Label lblCateIns; 
    @FXML private Label lblIsElectric; 
    @FXML private TextField txtMateIns, txtColorIns;
    @FXML private ComboBox<String> cbSubCategory; 
    @FXML private CheckBox chkIsElectric; 
    @FXML private AnchorPane apDetailContainer; 
    @FXML private GridPane gpGuitarDetail;
    @FXML private GridPane gpPianoDetail;
    @FXML private GridPane gpKeyboardDetail;
    @FXML private GridPane gpDrumKitDetail;
    @FXML private GridPane gpAccessoryDetail;
    @FXML private TextField txtCateGui, txtStrNumGui, txtBodyShapeGui; 
    @FXML private TextField txtCatePi, txtKeyNumPi; @FXML private CheckBox chkHasPedal; 
    @FXML private TextField txtCateKey, txtKeyNumKey; @FXML private CheckBox chkHasLCD; 
    @FXML private TextField txtNumOfDrumPieces, txtNumOfCymbals, txtHeadMaterial, txtShellMaterial; 
    @FXML private TextField txtCateAcc, txtCompatibleWith; 

    // --- ORDER VIEW VARIABLES ---
    @FXML private AnchorPane orderView;
    @FXML private Button btnOrders; // Nhớ đặt fx:id cho nút Order bên Sidebar nha

    // Left Side
    @FXML private TextField txtCustomerSearch; 
    @FXML private Label lblSelectedCustomerName;
    @FXML private DatePicker dpSellDate, dpDeliDate;
    @FXML private TextArea txtDeliAddress;

    // Right Side
    @FXML private TextField txtProductSearch;
    @FXML private TableView<Order.OrderDetail> orderTable; // Dùng Inner Class
    @FXML private Label lblOrderTotal;
    
    // Data (RAM)
    // Giỏ hàng tạm thời
    private ObservableList<Order.OrderDetail> cartItems = FXCollections.observableArrayList();
    // Khách hàng đang chọn
    private Customer selectedCustomer = null;

    // DAOs (Để truy vấn)
    private CustomerDAO customerDAO = new CustomerDAO();
    private OrderDAO orderDAO = new OrderDAO();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Dashboard initialized (DB Integrated)!");

        // 1. Load Data (Khởi tạo ObservableList nếu cần và tải dữ liệu thật)
        if (productList == null) productList = FXCollections.observableArrayList(); 
        loadProductData();     
        loadCustomerData();    
        loadEmployeeData();    
        
        // 2. Setup Tables (Sử dụng dữ liệu thật đã tải)
        setupProductTable();   
        setupCustomerTable();  
        setupEmployeeTable();  
        
        // 3. Setup UI & Logic
        setupChart("Revenue");
        loadBestSellers();
        
        sidebar.setTranslateX(-250);
        btnMenu.setOnMouseEntered(event -> openSidebar());
        sidebar.setOnMouseExited(event -> closeSidebar());

        setupFormLogic();
        setupSearchAndSort();
        updateHomeStats(); 

        // Click listeners cho Cards (giữ nguyên logic)
        if (cardProduct != null){
            cardProduct.setOnMouseClicked(event -> {
                setupChart("Products");
                resetCardStyles();
                cardProduct.setStyle("-fx-background-color: #e8f6f3; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
            });
        }

        if (cardCustomer != null){
            cardCustomer.setOnMouseClicked(event -> {
                setupChart("Customers");
                resetCardStyles();
                cardCustomer.setStyle("-fx-background-color: #e8f6f3; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
            });
        }

        if (cardRevenue != null) {
            cardRevenue.setOnMouseClicked(event -> {
                setupChart("Revenue");
                resetCardStyles();
                cardRevenue.setStyle("-fx-background-color: #e8f6f3; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
            });
        }

        setupOrderTable();
        setupOrderLogic();

        showHome();
    }
    
    // ==================== LOGIC DATA LOADING (DB THẬT) ====================
    
    // Tải dữ liệu Product
    private void loadProductData(){
        List<Product> items = inventoryManager.getAllItems();
        productList.setAll(items);
        System.out.println("Product Data refreshed. Count: " + productList.size());
    }

    // Tải dữ liệu Customer (MỚI - Dùng DB)
    private void loadCustomerData() {
        List<Customer> customers = customerManager.getAllCustomers();
        customerList = FXCollections.observableArrayList(customers);
        System.out.println("Customer Data loaded. Count: " + customerList.size());
    }
    
    // Tải dữ liệu Employee (MỚI - Dùng DB)
    private void loadEmployeeData() {
        List<Employee> employees = employeeManager.getAllEmployees();
        employeeList = FXCollections.observableArrayList(employees);
        System.out.println("Employee Data loaded. Count: " + employeeList.size());
    }


    // ==================== LOGIC TABLE SETUP & STATS ====================

    private void setupProductTable() {
        // Cột Product (Sử dụng fx:id đã có: colId, colName, colBrand, ...)
        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamePro()));
        colBrand.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBrand()));        
        colPrice.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSellingPrice()));
        colCategory.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCatePro()));
        colOrigin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrigin()));
        colQuantity.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQuantityInStock()));
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getImportDate()));

        colInfo.setCellValueFactory(cellData -> {
            Product p = cellData.getValue();
            String info = "";
            if (p instanceof com.TamCa.store.model.Instrument){
                var i = (com.TamCa.store.model.Instrument) p;
                info = i.getColorIns() + " | " + i.getMateIns();
            } else if (p instanceof com.TamCa.store.model.Accessory){
                var a = (com.TamCa.store.model.Accessory) p;
                info = a.getColorAcc() + " | " + a.getMateAcc();
            }
            return new SimpleStringProperty(info);
        });
        productTable.setItems(new SortedList<>(new FilteredList<>(productList, p -> true)));
    }

    private void setupCustomerTable() {
        if (customerTable != null) {
            // Cột Customer
            colCusName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNameCus()));
            colCusCSN.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCSN()));
            colCusPhone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNum()));
            colCusEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmailCus()));
            colCusAddress.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddCus()));

            customerTable.setItems(customerList);
            
            // Cập nhật các Label trong Customer View
            if(lblTotalCus != null) lblTotalCus.setText(String.valueOf(customerList.size()));
            if(lblVipCus != null) lblVipCus.setText("Need Logic"); 
            if(lblNewestCus != null) lblNewestCus.setText("Need Logic"); 
        }
    }
    
    private void setupEmployeeTable() {
        if (employeeTable != null) {
            // Cột Employee
            colEmpID.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEID()));
            colEmpName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNameEmp()));
            colEmpPos.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosEmp()));
            colEmpSal.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSalEmp()));
            colEmpDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getHireDate()));

            employeeTable.setItems(employeeList);
            
            // Cập nhật các Label trong Employee View
            if(lblTotalEmp != null) lblTotalEmp.setText(String.valueOf(employeeList.size()));
            // Giữ lại logic tạm cho các label khác
            if(lblTotalSalary != null) lblTotalSalary.setText("$3500");
            if(lblManagerName != null) lblManagerName.setText("Phan Anh");
        }
    }

    // --- CẬP NHẬT THỐNG KÊ HOME VIEW ---
    private void updateHomeStats() {
        // 1. Lấy dữ liệu
        int totalProducts = inventoryManager.getExistingProductCount();
        double totalRevenue = inventoryManager.totalValue();
        int totalCustomers = customerList != null ? customerList.size() : 0; 
        
        // 2. Cập nhật UI (Sử dụng đúng fx:id GỐC trong FXML: lblTotalProductsCount, lblTotalCustomersCount, lblTotalRevenueValue)
        
        if (lblTotalProductsCount != null) {
            lblTotalProductsCount.setText(String.valueOf(totalProducts));
            lblTotalProductsCount.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 30px; -fx-font-weight: bold;"); 
        }

        if (lblTotalCustomersCount != null) {
            lblTotalCustomersCount.setText(String.valueOf(totalCustomers));
            lblTotalCustomersCount.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 30px; -fx-font-weight: bold;");
        }

        if (lblTotalRevenueValue != null) {
            lblTotalRevenueValue.setText(String.format("$%,.2f", totalRevenue));
            lblTotalRevenueValue.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 30px; -fx-font-weight: bold;");
        }
    }
    
    // ==================== LOGIC UI & FORM ====================

    @FXML private void handleMenu() {
        double currentX = sidebar.getTranslateX();
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), sidebar);
        transition.setToX((currentX == 0) ? -250 : 0);
        transition.play();
    }

    private void resetButtonStyles() {
        btnHome.getStyleClass().remove("active");
        btnProducts.getStyleClass().remove("active");
        if(btnCustomers != null) btnCustomers.getStyleClass().remove("active");
        if(btnEmployees != null) btnEmployees.getStyleClass().remove("active");
    }

    @FXML private void showHome() {
        homeView.setVisible(true); homeView.toFront();
        productView.setVisible(false); customerView.setVisible(false); employeeView.setVisible(false); addProductView.setVisible(false);
        
        updateHomeStats();
        
        resetButtonStyles(); btnHome.getStyleClass().add("active");
    }

    @FXML private void showProducts() {

        loadProductData();
        // Cần cập nhật lại items cho table sau khi load, để search/sort hoạt động đúng
        // SortedList<Product> sortedData = new SortedList<>(new FilteredList<>(productList, p -> true));
        // sortedData.comparatorProperty().bind(productTable.comparatorProperty());
        // productTable.setItems(sortedData);
        
        homeView.setVisible(false); 
        productView.setVisible(true); 
        productView.toFront();
        customerView.setVisible(false); 
        employeeView.setVisible(false); 
        addProductView.setVisible(false);
        resetButtonStyles(); btnProducts.getStyleClass().add("active");
    }

    @FXML private void showCustomers() {
        loadCustomerData(); // Load data mới nhất
        setupCustomerTable(); // Update TableView và Stats
        
        homeView.setVisible(false); productView.setVisible(false); employeeView.setVisible(false); addProductView.setVisible(false);
        customerView.setVisible(true); customerView.toFront();
        resetButtonStyles(); if(btnCustomers != null) btnCustomers.getStyleClass().add("active");
    }

    @FXML private void showEmployees() {
        loadEmployeeData(); // Load data mới nhất
        setupEmployeeTable(); // Update TableView và Stats
        
        homeView.setVisible(false); productView.setVisible(false); customerView.setVisible(false); addProductView.setVisible(false);
        employeeView.setVisible(true); employeeView.toFront();
        resetButtonStyles(); if(btnEmployees != null) btnEmployees.getStyleClass().add("active");
    }

    @FXML private void showAddProductForm() {
        if(productTable != null) productTable.setVisible(false);
        if(homeView != null) homeView.setVisible(false); 
        addProductView.setVisible(true); addProductView.toFront();
        clearForm();
    }
    
    @FXML private void handleCancelAdd() {
        addProductView.setVisible(false);
        if(productTable != null) productTable.setVisible(true);
    }

    private void clearForm() {
        if(txtNamePro != null) txtNamePro.clear();
        if(txtBrand != null) txtBrand.clear();
        if(txtOrigin != null) txtOrigin.clear();
        if(txtPrice != null) txtPrice.clear();
        if(txtQuantity != null) txtQuantity.clear();
        if(dpImportDate != null) dpImportDate.setValue(LocalDate.now());

        if(cbCatePro != null) cbCatePro.getSelectionModel().selectFirst();
        
        // Reset sub-forms
        if(txtMateIns != null) txtMateIns.clear();
        if(cbSubCategory != null) cbSubCategory.getSelectionModel().clearSelection();
        setAllDetailFormsVisible(false);
    }

    // --- LOGIC FORM (Giữ nguyên) ---

    private void setupFormLogic() {
        if (cbCatePro != null) {
            cbCatePro.setItems(FXCollections.observableArrayList("Instrument", "Accessory"));
            cbCatePro.valueProperty().addListener((obs, oldVal, newVal) -> handleMajorCategoryChange(newVal));
        }
        
        if (cbSubCategory != null) {
            cbSubCategory.setItems(FXCollections.observableArrayList("Guitar", "Piano", "Keyboard", "Drumkit"));

            cbSubCategory.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && "Instrument".equals(cbCatePro.getValue())) {
                    handleSubCategoryChange(newVal);
                };
            });
        }
    }

    private void handleMajorCategoryChange(String majorCategory) {
        boolean isInstrument = "Instrument".equals(majorCategory);
        boolean isAccessory = "Accessory".equals(majorCategory);
        
        if (tpInstrumentInfo != null) tpInstrumentInfo.setText(isInstrument ? "II. GENERAL INFORMATION FOR INSTRUMENT" : "II. GENERAL INFORMATION FOR ACCESSORY");
        if (lblCateIns != null) lblCateIns.setText(isInstrument ? "Instrument Type (Sub Category)" : "Material");
        if (lblIsElectric != null) lblIsElectric.setVisible(isInstrument);
        if (chkIsElectric != null) chkIsElectric.setVisible(isInstrument);

        if (isInstrument) {
            if (tpDetailInfo != null) tpDetailInfo.setText("III. DETAILED INFORMATION");
            if (cbSubCategory != null && cbSubCategory.getValue() != null){
                handleSubCategoryChange(cbSubCategory.getValue());
            } else {
                setAllDetailFormsVisible(false);
            }

        } else if (isAccessory) {
            if (tpDetailInfo != null) tpDetailInfo.setText("III. DETAILED INFORMATION");
            setAllDetailFormsVisible(false);
            if (gpAccessoryDetail != null) gpAccessoryDetail.setVisible(true);
        } else {
            if (tpDetailInfo != null) tpDetailInfo.setText("III. PLEASE CHOOSE PRODUCT TYPE AT SECTION I");
            setAllDetailFormsVisible(false);
        }
    }

    private void handleSubCategoryChange(String subCategory) {
        setAllDetailFormsVisible(false);
        String normalizedCategory = subCategory.trim().toLowerCase();
        
        if (normalizedCategory.contains("guitar") && gpGuitarDetail != null) gpGuitarDetail.setVisible(true);
        else if (normalizedCategory.contains("piano") && gpPianoDetail != null) gpPianoDetail.setVisible(true);
        else if (normalizedCategory.contains("key") && gpKeyboardDetail != null) gpKeyboardDetail.setVisible(true);
        else if (normalizedCategory.contains("drum") && gpDrumKitDetail != null) gpDrumKitDetail.setVisible(true);
    }
    
    private void setAllDetailFormsVisible(boolean visible) {
        if (gpGuitarDetail != null) gpGuitarDetail.setVisible(visible);
        if (gpPianoDetail != null) gpPianoDetail.setVisible(visible);
        if (gpKeyboardDetail != null) gpKeyboardDetail.setVisible(visible);
        if (gpDrumKitDetail != null) gpDrumKitDetail.setVisible(visible);
        if (gpAccessoryDetail != null) gpAccessoryDetail.setVisible(visible);
    }

    @FXML
    private void handleSaveProduct() {
        
        try {
            String name = txtNamePro.getText().trim();
            String brand = txtBrand.getText().trim();
            String origin = txtOrigin.getText().trim(); 
            String catePro = cbCatePro.getValue(); 
            
            if (name.isEmpty() || brand.isEmpty() || origin.isEmpty() || catePro == null) {
                showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in all general information.");
                return;
            }

            double price = Double.parseDouble(txtPrice.getText().trim());
            int quantity = Integer.parseInt(txtQuantity.getText().trim());
            LocalDate localDate = dpImportDate.getValue();
            Date importDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // --- B. SAVE PRODUCT BASED ON CATEGORY ---
            if ("Instrument".equals(catePro)) {
                String mateIns = txtMateIns.getText().trim();
                String colorIns = txtColorIns.getText().trim();
                boolean isElectric = chkIsElectric.isSelected();

                String subCategory = cbSubCategory.getValue(); 

                if (subCategory == null) {
                    showAlert(Alert.AlertType.WARNING, "Missing Info", "Please select an Instrument Type.");
                    return;
                }

                if (mateIns.isEmpty() || colorIns.isEmpty() || subCategory.isEmpty()) {
                     throw new IllegalArgumentException("Please fill in all general instrument details.");
                }

                String subCatLower = subCategory.toLowerCase();

                if (subCatLower.contains("guitar")) {
                    inventoryManager.addNewGuitar(name, catePro, origin, brand, quantity, importDate, price, mateIns, subCategory, colorIns, isElectric, 
                        txtCateGui.getText().trim(), Integer.parseInt(txtStrNumGui.getText().trim()), txtBodyShapeGui.getText().trim());
                
                } else if (subCatLower.contains("piano")) {
                    inventoryManager.addNewPiano(name, catePro, origin, brand, quantity, importDate, price, mateIns, subCategory, colorIns, isElectric, 
                        txtCatePi.getText().trim(), Integer.parseInt(txtKeyNumPi.getText().trim()), chkHasPedal.isSelected());
                
                } else if (subCatLower.contains("key")) { 
                    inventoryManager.addNewKeyboard(name, catePro, origin, brand, quantity, importDate, price, mateIns, subCategory, colorIns, isElectric, 
                        txtCateKey.getText().trim(), Integer.parseInt(txtKeyNumKey.getText().trim()), chkHasLCD.isSelected());
                
                } else if (subCatLower.contains("drum")) { 
                    inventoryManager.addNewDrum(name, catePro, origin, brand, quantity, importDate, price, mateIns, subCategory, colorIns, isElectric, 
                        Integer.parseInt(txtNumOfDrumPieces.getText().trim()), Integer.parseInt(txtNumOfCymbals.getText().trim()), txtHeadMaterial.getText().trim(), txtShellMaterial.getText().trim());
                
                } else {
                    showAlert(Alert.AlertType.WARNING, "Invalid Instrument Type", "Please enter 'Guitar', 'Piano', 'Keyboard', or 'Drumkit' in Sub Category.");
                    return;
                }

            } else if ("Accessory".equals(catePro)) {
                 
                 if (txtCateAcc.getText().trim().isEmpty() || txtCompatibleWith.getText().trim().isEmpty() || txtMateIns.getText().trim().isEmpty()) {
                      throw new IllegalArgumentException("Please fill in all accessory details (Category, Compatible With, Material).");
                 }

                 inventoryManager.addNewAccessory(name, catePro, origin, brand, quantity, importDate, price, 
                    txtCateAcc.getText().trim(), txtMateIns.getText().trim(), txtColorIns.getText().trim(), txtCompatibleWith.getText().trim());
                
            } else {
                 throw new IllegalArgumentException("Error: Major Category not found.");
            }

            // Khi lưu thành công thì nó sẽ làm những việc này
            System.out.println("✅ Saved to DB: " + name);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product '" + name + "' added successfully!");
            loadProductData(); // Cập nhật bảng Product
            handleCancelAdd();
            updateHomeStats();
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Price, Quantity, and numeric specs must be valid numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "System Error", e.getMessage());
        }
    }


    @FXML
    private void handleDebugAddProduct() {
        System.out.println("--- DEBUG: Injecting new product for testing live data ---");
        try {
            // Thêm một cây đàn guitar mới với giá 1 USD
            inventoryManager.addNewGuitar(
                "Debug Test Guitar - Live", "Instrument", "VN", "Debug Brand", 1, new Date(), 1.0,
                "Maple", "guitar", "Black", true, 
                "electric", 6, "Strat"
            );
            // Cập nhật tất cả các View
            loadProductData(); 
            updateHomeStats(); 
            System.out.println("--- DEBUG: Injection complete. ---");
            showAlert(Alert.AlertType.INFORMATION, "DEBUG", "Đã thêm 1 sản phẩm 1 USD. Home Stats đã được cập nhật!");

        } catch (Exception e) {
            System.err.println("DEBUG ERROR: " + e.getMessage());
        }
    }

    private void resetCardStyles() {
        String defaultStyle = "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);";
        if (cardProduct != null) cardProduct.setStyle(defaultStyle);
        if (cardCustomer != null) cardCustomer.setStyle(defaultStyle);
        if (cardRevenue != null) cardRevenue.setStyle(defaultStyle);
    }

    private void setupChart(String type) {
        if (revenueChart == null) return; // Dùng revenueChart (FX:ID GỐC)

        revenueChart.getData().clear();
        revenueChart.setAnimated(false);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(type);

        String[] allMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}; 
        
        // Map để lưu trữ dữ liệu nguồn: Key=Tháng, Value=Số liệu
        java.util.Map<String, Double> sourceData = new java.util.LinkedHashMap<>();
        
        // Dữ liệu mock data cần thiết (giữ lại cho chart)
        java.util.Map<String, Double> mockData = new java.util.LinkedHashMap<>();
        mockData.put("Jan", type.equals("Products") ? 50.0 : 10.0);
        mockData.put("Feb", type.equals("Products") ? 80.0 : 25.0);
        mockData.put("Mar", type.equals("Products") ? 120.0 : 40.0);
        mockData.put("Apr", type.equals("Products") ? 90.0 : 55.0);
        mockData.put("May", type.equals("Products") ? 150.0 : 70.0);
        mockData.put("Nov", type.equals("Products") ? 100.0 : 75.0);
        
        
        switch (type) {
            case "Revenue":
                revenueChart.setTitle("Monthly Inventory Value ($)");
                sourceData = inventoryManager.getMonthlyImportStats(); // <--- Dữ liệu thật từ DB
                break;

            case "Products":
                revenueChart.setTitle("Monthly Product Import (Mock)");
                sourceData = mockData; // <--- Dùng mock data đã nạp ở trên
                break;

            case "Customers":
                revenueChart.setTitle("New Customers Growth (Mock)");
                sourceData = mockData; // <--- Dùng mock data đã nạp ở trên
                break;
        }
        
        // --- VẼ CHART: LẤP ĐẦY 12 THÁNG BẰNG DỮ LIỆU CÓ SẴN (hoặc 0) ---
        for (String month : allMonths) {
            Double value = sourceData.getOrDefault(month, 0.0);
            series.getData().add(new XYChart.Data<>(month, value));
        }

        revenueChart.getData().add(series);
    }
    
    
    // --- LOGIC BEST SELLER & 3D ANIMATION (Giữ nguyên logic tạo dữ liệu giả) ---

    private void loadBestSellers() {
        if (pnBestSellers == null) return;
        
        pnBestSellers.getChildren().clear();
        
        // Thêm dữ liệu giả (Fake Data)
        pnBestSellers.getChildren().add(createBestSellerCard("Fender Stratocaster", "1,500", "guitar.png"));
        pnBestSellers.getChildren().add(createBestSellerCard("Yamaha Grand Piano", "12,000", "piano.png"));
        pnBestSellers.getChildren().add(createBestSellerCard("Pearl Export Drums", "950", "drum.png"));
        pnBestSellers.getChildren().add(createBestSellerCard("Roland XPS-10", "600", "keyboard.png"));
    }

    private HBox createBestSellerCard(String productName, String price, String imagePath) {
        HBox card = new HBox();
        card.setSpacing(20); 
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 3);");
        card.setPrefHeight(100); 
        card.setPadding(new javafx.geometry.Insets(10)); 
        
        // Ảnh
        ImageView imageView = new ImageView();
        try {
            String path = getClass().getResource("/images/" + imagePath).toExternalForm();
            imageView.setImage(new Image(path));
        } catch (Exception e) { 
            System.err.println("Không tìm thấy ảnh: " + imagePath);
        }
        
        imageView.setFitWidth(70); 
        imageView.setFitHeight(70);
        imageView.setPreserveRatio(true);

        // Hiệu ứng xoay 3D
        RotateTransition rotate = new RotateTransition(Duration.seconds(2), imageView);
        rotate.setAxis(Rotate.Y_AXIS); 
        rotate.setByAngle(360);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.setAutoReverse(false);

        card.setOnMouseEntered(e -> {
            rotate.play();
            card.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 10; -fx-padding: 10; -fx-border-color: #bdc3c7; -fx-border-radius: 10; -fx-cursor: hand;");
        });
    
        card.setOnMouseExited(e -> {
            rotate.stop();
            imageView.setRotate(0); 
            card.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 3); -fx-padding: 10;");
        });

        // Thông tin
        VBox info = new VBox(5);
        info.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLbl = new Label(productName);
        nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #2c3e50;");
        
        Label priceLbl = new Label("$" + price);
        priceLbl.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-font-size: 14px;");
        
        Label tag = new Label("🔥 Best Seller");
        tag.setStyle("-fx-background-color: #ff7675; -fx-text-fill: white; -fx-padding: 3 10; -fx-background-radius: 20; -fx-font-size: 11px; -fx-font-weight: bold;");

        info.getChildren().addAll(nameLbl, priceLbl, tag);
        card.getChildren().addAll(imageView, info);

        return card;
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // --- SIDEBAR ANIMATION ---
    private void openSidebar() { TranslateTransition t = new TranslateTransition(Duration.seconds(0.3), sidebar); t.setToX(0); sidebar.toFront(); t.play(); }
    private void closeSidebar() { TranslateTransition t = new TranslateTransition(Duration.seconds(0.3), sidebar); t.setToX(-250); t.play(); }

    private void setupSearchAndSort() {

        FilteredList<Product> filteredData = new FilteredList<>(productList, p -> true);

        if (txtSearchProduct != null){
            txtSearchProduct.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(product -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (product.getNamePro().toLowerCase().contains(lowerCaseFilter)) return true;
                    if (product.getBrand().toLowerCase().contains(lowerCaseFilter)) return true;
                    if (product.getId().toLowerCase().contains(lowerCaseFilter)) return true;
                    if (product.getCatePro().toLowerCase().contains(lowerCaseFilter)) return true;
                    if (product.getOrigin().toLowerCase().contains(lowerCaseFilter)) return true;

                    return false;
                });
            });
        }

        SortedList<Product> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(productTable.comparatorProperty());

        productTable.setItems(sortedData);

        // Logic search Customer
        if (customerList != null){
            FilteredList<Customer> filteredCus = new FilteredList<>(customerList, p -> true);
                
                if(txtSearchCustomer != null){
                    txtSearchCustomer.textProperty().addListener((observable, oldValue, newValue) -> {
                        filteredCus.setPredicate(cus -> {
                            if (newValue == null || newValue.isEmpty()) return true;
                            String lower = newValue.toLowerCase();

                            if (cus.getNameCus().toLowerCase().contains(lower)) return true;
                            if (cus.getCSN().toLowerCase().contains(lower)) return true;
                            if (cus.getPhoneNum().toLowerCase().contains(lower)) return true;
                            return false;
                        });
                    });
                }

                if (customerTable != null) {
                    SortedList<Customer> sortedCus = new SortedList<>(filteredCus);
                    sortedCus.comparatorProperty().bind(customerTable.comparatorProperty());
                    customerTable.setItems(sortedCus);
                }
            }

        // --- EMPLOYEE SEARCH ---
        if (employeeList != null) {
            FilteredList<Employee> filteredEmp = new FilteredList<>(employeeList, p -> true);
            
            // Listener cho ô search Employee
            if (txtSearchEmployee != null) {
                txtSearchEmployee.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredEmp.setPredicate(emp -> {
                        if (newValue == null || newValue.isEmpty()) return true;
                        String lower = newValue.toLowerCase();
                        
                        if (emp.getNameEmp().toLowerCase().contains(lower)) return true;
                        if (emp.getEID().toLowerCase().contains(lower)) return true;
                        if (emp.getPosEmp().toLowerCase().contains(lower)) return true;
                        return false;
                    });
                });
            }
            // Bind vào bảng
            if (employeeTable != null) {
                SortedList<Employee> sortedEmp = new SortedList<>(filteredEmp);
                sortedEmp.comparatorProperty().bind(employeeTable.comparatorProperty());
                employeeTable.setItems(sortedEmp);
            }
        }
    }

    // CUSTOMER, những hàm liên quan để add Customer mới
    @FXML 
    private void showAddCustomerForm() {
        
        if(customerTable != null) customerTable.setVisible(false);
        addCustomerView.setVisible(true); 
        addCustomerView.toFront();
        
        // Gọi InventoryManager tính ID tiếp theo
        String nextID = customerManager.generateNextCustomerId(); 
        
        if (txtCusCSNForm != null) {
            txtCusCSNForm.setText(nextID); 
            txtCusCSNForm.setEditable(false); 
            txtCusCSNForm.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #7f8c8d;"); // Làm mờ đi cho biết là Read-only
        }

        if (txtCusNameForm != null) txtCusNameForm.clear(); 
        if (txtCusPhoneForm != null) txtCusPhoneForm.clear(); 
        if (txtCusEmailForm != null) txtCusEmailForm.clear(); 
        if (txtCusAddressForm != null) txtCusAddressForm.clear();
    }

    @FXML void handleCancelAddCustomer() {
        addCustomerView.setVisible(false);
        if(customerTable != null) customerTable.setVisible(true);
    }

    @FXML void handleSaveCustomer(){
        
        String name = txtCusNameForm.getText().trim();
        String csn = txtCusCSNForm.getText().trim(); // CSN là ID tự sinh (ví dụ: C004)

        if (name.isEmpty() || csn.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Customer Name or CSN", "Try harder bro!");
            return; 
        }

        boolean success = customerManager.addNewCustomer(
            txtCusCSNForm.getText().trim(),
            txtCusNameForm.getText().trim(),
            txtCusPhoneForm.getText().trim(),
            txtCusEmailForm.getText().trim(),
            txtCusAddressForm.getText().trim()
        );

        if (success){
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer added successfully!");
            loadCustomerData(); 
            
            handleCancelAddCustomer(); // Đóng form
            updateHomeStats(); // Update số liệu Dashboard
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add customer. CSN might be duplicate.");
        }   
    }

    // EMPLOYEE, những hàm liên quan để add Employee mới
    @FXML 
    private void showAddEmployeeForm() {
        // 1. Ẩn bảng, hiện form
        if(employeeTable != null) employeeTable.setVisible(false);
        addEmployeeView.setVisible(true); 
        addEmployeeView.toFront();
        
        // Tự động sinh ID (E001, E002...)
        String nextID = employeeManager.generateNextEmployeeId();

        if (txtEmpIDForm != null) {
            txtEmpIDForm.setText(nextID);
            txtEmpIDForm.setEditable(false); // Không cho sửa
            txtEmpIDForm.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #7f8c8d;");
        }

        if (txtEmpNameForm != null) txtEmpNameForm.clear();
        if (txtEmpPosForm != null) txtEmpPosForm.clear();
        if (txtEmpSalForm != null) txtEmpSalForm.clear();
        
        // Mặc định ngày vào làm là Hôm nay
        if (dpEmpHireDateForm != null) dpEmpHireDateForm.setValue(LocalDate.now());
    }

    @FXML 
    private void handleCancelAddEmployee() {
        addEmployeeView.setVisible(false);
        if(employeeTable != null) employeeTable.setVisible(true);
    }

    @FXML 
    private void handleSaveEmployee() {

        if (txtEmpIDForm == null || txtEmpNameForm == null) {
             showAlert(Alert.AlertType.ERROR, "System Error", "Lỗi FXML: Chưa gán fx:id cho Employee Form!");
             return;
        }
        String eid = txtEmpIDForm.getText().trim(); // Lấy ID tự sinh
        String name = txtEmpNameForm.getText().trim();
        
        if (eid.isEmpty() || name.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Info", "What's your name bro?!");
            return;
        }
        
        try {
            // Validate lương phải là số
            int salary = Integer.parseInt(txtEmpSalForm.getText().trim());
            
            // Lấy ngày (hoặc mặc định là nay)
            LocalDate localDate = dpEmpHireDateForm.getValue();
            if (localDate == null) localDate = LocalDate.now();
            Date hireDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Gọi Manager để lưu
            boolean success = employeeManager.addNewEmployee(eid, name, txtEmpPosForm.getText().trim(), salary, hireDate);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee " + eid + " added successfully!");
                loadEmployeeData(); 
                handleCancelAddEmployee(); 
                updateHomeStats();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add employee (Check ID or Salary rule).");
            }
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Salary must be a valid number (e.g. 5000).");
        }
    }

    private void setupOrderTable() {
        // Cột 1: Tên sản phẩm
        TableColumn<Order.OrderDetail, String> colName = new TableColumn<>("Product");
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduct().getNamePro()));
        
        // Cột 2: Số lượng (Quantity)
        TableColumn<Order.OrderDetail, Integer> colQty = new TableColumn<>("Qty");
        colQty.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getQuantity()));
        
        // Cột 3: Giá bán
        TableColumn<Order.OrderDetail, Double> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPriceAtSale()));
        
        // Cột 4: Thành tiền (Qty * Price)
        TableColumn<Order.OrderDetail, Double> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getTotalPrice()));

        // Cột 5: Nút Xóa (Action)
        TableColumn<Order.OrderDetail, Void> colAction = new TableColumn<>("Action");
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnDel = new Button("X");
            {
                btnDel.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                btnDel.setOnAction(e -> {
                    // Logic xóa khỏi giỏ
                    Order.OrderDetail item = getTableView().getItems().get(getIndex());
                    cartItems.remove(item);
                    calculateOrderTotal(); // Tính lại tổng tiền
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnDel);
            }
        });

        orderTable.getColumns().setAll(colName, colQty, colPrice, colTotal, colAction);
        orderTable.setItems(cartItems); // Gắn list giỏ hàng vào bảng
    }

    private void setupOrderLogic() {
        // 1. Mặc định ngày bán là hôm nay
        if (dpSellDate != null) dpSellDate.setValue(LocalDate.now());
        if (dpDeliDate != null) dpDeliDate.setValue(LocalDate.now().plusDays(3)); // Giao sau 3 ngày

        // 2. Logic tìm Customer (Khi gõ vào ô search)
        if (txtCustomerSearch != null) {
            txtCustomerSearch.textProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null || newVal.trim().isEmpty()) {
                    selectedCustomer = null;
                    lblSelectedCustomerName.setText("No Customer Selected");
                    lblSelectedCustomerName.setStyle("-fx-text-fill: #e74c3c;"); // Màu đỏ
                    return;
                }
                
                // Tìm trong danh sách customerList (đã load từ DB)
                boolean found = false;
                for (Customer c : customerList) {
                    // Tìm theo ID hoặc Tên (không phân biệt hoa thường)
                    if (c.getCSN().equalsIgnoreCase(newVal.trim()) || c.getNameCus().toLowerCase().contains(newVal.toLowerCase())) {
                        selectedCustomer = c;
                        lblSelectedCustomerName.setText("✅ " + c.getNameCus() + " (" + c.getCSN() + ")");
                        lblSelectedCustomerName.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;"); // Màu xanh
                        
                        // Tự điền địa chỉ giao hàng
                        if (txtDeliAddress != null) txtDeliAddress.setText(c.getAddCus());
                        found = true;
                        break; // Tìm thấy 1 người là dừng
                    }
                }
                if (!found) {
                    selectedCustomer = null;
                    lblSelectedCustomerName.setText("❌ Not Found");
                    lblSelectedCustomerName.setStyle("-fx-text-fill: #e74c3c;");
                }
            });
        }

        // 3. Logic tìm Product 
        if (txtProductSearch != null) {
            txtProductSearch.setOnAction(e -> {
                String query = txtProductSearch.getText().trim();
                if (query.isEmpty()) return;

                // 1. Tìm tất cả sản phẩm khớp từ khóa
                List<Product> matches = new ArrayList<>();
                for (Product p : productList) {
                    if (p.getId().equalsIgnoreCase(query) || p.getNamePro().toLowerCase().contains(query.toLowerCase())) {
                        matches.add(p);
                    }
                }

                if (matches.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Not Found", "Không tìm thấy sản phẩm nào!");
                } 
                else if (matches.size() == 1) {
                    // Nếu chỉ có 1 kết quả -> Thêm ngay
                    addToCart(matches.get(0));
                    txtProductSearch.clear();
                } 
                else {
                    // Nếu có nhiều kết quả -> Hiện Dialog cho chọn
                    ChoiceDialog<Product> dialog = new ChoiceDialog<>(matches.get(0), matches);
                    dialog.setTitle("Select Product");
                    dialog.setHeaderText("Tìm thấy " + matches.size() + " sản phẩm khớp với '" + query + "'");
                    dialog.setContentText("Vui lòng chọn sản phẩm chính xác:");
                    
                    // Sửa hiển thị trong Dropdown (chỉ hiện Tên - Giá - ID)
                    // (Mặc định nó dùng hàm toString của Product, nếu bro chưa override toString thì nó hiện mã loằng ngoằng)
                    // Cách nhanh nhất là bro override toString() trong model Product, hoặc để nó hiện mặc định cũng được.
                    
                    Optional<Product> result = dialog.showAndWait();
                    result.ifPresent(selectedProduct -> {
                        addToCart(selectedProduct);
                        txtProductSearch.clear();
                    });
                }
            });
        }
    }

    // Helper: Thêm vào giỏ
    private void addToCart(Product p) {
        // Check tồn kho
        if (p.getQuantityInStock() <= 0) {
            showAlert(Alert.AlertType.ERROR, "Out of Stock", "Sản phẩm này đã hết hàng!");
            return;
        }

        // Check xem trong giỏ đã có món này chưa
        for (Order.OrderDetail item : cartItems) {
            if (item.getProduct().getId().equals(p.getId())) {
                // Nếu có rồi -> Tăng số lượng
                if (item.getQuantity() < p.getQuantityInStock()) {
                    item.setQuantity(item.getQuantity() + 1);
                    orderTable.refresh(); // Refresh bảng để hiện số mới
                    calculateOrderTotal();
                } else {
                    showAlert(Alert.AlertType.WARNING, "Limit Reached", "Trong kho không đủ hàng!");
                }
                return;
            }
        }

        // Nếu chưa có -> Tạo mới (Qty = 1)
        cartItems.add(new Order.OrderDetail(p, 1));
        calculateOrderTotal();
    }

    // Helper: Tính tổng tiền
    private void calculateOrderTotal() {
        double total = 0;
        for (Order.OrderDetail item : cartItems) {
            total += item.getTotalPrice();
        }
        if (lblOrderTotal != null) {
            lblOrderTotal.setText(String.format("$%,.2f", total));
        }
    }

    @FXML 
    private void showOrders() {
        // Ẩn mấy cái kia
        homeView.setVisible(false); productView.setVisible(false); 
        customerView.setVisible(false); employeeView.setVisible(false);
        addCustomerView.setVisible(false); addEmployeeView.setVisible(false);
        
        // Hiện Order
        orderView.setVisible(true); orderView.toFront();
        
        // Đổi màu nút Sidebar
        resetButtonStyles(); 
        if(btnOrders != null) btnOrders.getStyleClass().add("active");
    }

    @FXML
    private void handleCheckout() {
        // 1. Validate
        if (cartItems.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Cart", "Giỏ hàng đang trống!");
            return;
        }
        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Customer", "Vui lòng chọn khách hàng!");
            return;
        }

        // 2. Tạo Order Object
        String address = txtDeliAddress.getText();
        Date sDate = java.sql.Date.valueOf(dpSellDate.getValue());
        Date dDate = java.sql.Date.valueOf(dpDeliDate.getValue());

        Order newOrder = new Order("Processing", address, sDate, dDate, selectedCustomer);
        // Chuyển từ ObservableList sang ArrayList thường
        newOrder.setItems(new ArrayList<>(cartItems));

        // 3. Gọi Manager lưu xuống DB
        boolean success = inventoryManager.createOrder(newOrder);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order created successfully! ID: " + newOrder.getOrderId());
            
            // Reset giao diện để bán đơn tiếp theo
            cartItems.clear();
            txtProductSearch.clear();
            txtCustomerSearch.clear();
            lblOrderTotal.setText("$0.00");
            
            // Cập nhật lại kho (vì kho đã bị trừ)
            loadProductData(); 
            updateHomeStats();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed", "Lỗi khi tạo đơn hàng. Vui lòng thử lại.");
        }
    }

}