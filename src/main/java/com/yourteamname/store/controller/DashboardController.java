package com.yourteamname.store.controller;

import com.yourteamname.store.model.Customer;
import com.yourteamname.store.model.Employee;
import com.yourteamname.store.model.Product;
import com.yourteamname.store.service.InventoryManager;
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
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    
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
    @FXML private AreaChart<String, Number> revenueChart;
    @FXML private VBox pnBestSellers;
    // Label Count
    @FXML private Label lblTotalProductsCount;
    @FXML private Label lblTotalCustomersCount;
    @FXML private Label lblTotalRevenueValue;

    // --- PRODUCT VIEW ---
    @FXML private AnchorPane productView;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colBrand;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, String> colCategory;

    // --- CUSTOMER VIEW ---
    @FXML private AnchorPane customerView;
    @FXML private Label lblTotalCus;
    @FXML private Label lblVipCus;
    @FXML private Label lblNewestCus;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> colCusName;
    @FXML private TableColumn<Customer, String> colCusCSN;
    @FXML private TableColumn<Customer, String> colCusPhone;
    @FXML private TableColumn<Customer, String> colCusEmail;
    @FXML private TableColumn<Customer, String> colCusAddress;
    private ObservableList<Customer> customerList;

    // --- EMPLOYEE VIEW ---
    @FXML private AnchorPane employeeView;
    @FXML private Label lblTotalEmp;
    @FXML private Label lblTotalSalary;
    @FXML private Label lblManagerName;
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, String> colEmpID;
    @FXML private TableColumn<Employee, String> colEmpName;
    @FXML private TableColumn<Employee, String> colEmpPos;
    @FXML private TableColumn<Employee, Integer> colEmpSal;
    @FXML private TableColumn<Employee, Date> colEmpDate;
    private ObservableList<Employee> employeeList;

    // --- ADD PRODUCT FORM ---
    @FXML private AnchorPane addProductView; 
    
    // Form Sections
    @FXML private TitledPane tpInstrumentInfo;
    @FXML private TitledPane tpDetailInfo;
    @FXML private TextField txtNamePro, txtBrand, txtOrigin, txtPrice, txtQuantity;
    @FXML private ComboBox<String> cbCatePro; 
    @FXML private DatePicker dpImportDate; 
    
    // Instrument/Accessory General
    @FXML private Label lblCateIns; 
    @FXML private Label lblIsElectric; 
    @FXML private TextField txtMateIns, txtColorIns, txtCateIns; 
    @FXML private CheckBox chkIsElectric; 

    // Detail Containers 
    @FXML private AnchorPane apDetailContainer; 
    @FXML private GridPane gpGuitarDetail;
    @FXML private GridPane gpPianoDetail;
    @FXML private GridPane gpKeyboardDetail;
    @FXML private GridPane gpDrumKitDetail;
    @FXML private GridPane gpAccessoryDetail;

    // Detail Fields
    @FXML private TextField txtCateGui, txtStrNumGui, txtBodyShapeGui; // Guitar
    @FXML private TextField txtCatePi, txtKeyNumPi; @FXML private CheckBox chkHasPedal; // Piano
    @FXML private TextField txtCateKey, txtKeyNumKey; @FXML private CheckBox chkHasLCD; // Keyboard
    @FXML private TextField txtNumOfDrumPieces, txtNumOfCymbals, txtHeadMaterial, txtShellMaterial; // Drum
    @FXML private TextField txtCateAcc, txtCompatibleWith; // Accessory

    // Logic Data
    private InventoryManager inventoryManager;
    private ObservableList<Product> productList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Dashboard initialized (Merged Version)!");

        // 1. Setup DB (Logic của Bạn)
        inventoryManager = new InventoryManager();
        //inventoryManager.resetDatabase(); // Tạm tắt reset mỗi lần chạy để giữ dữ liệu cũ nếu muốn
        
        // 2. Setup Tables
        setupProductTable();   // DB thật
        setupCustomerTable();  // Fake
        setupEmployeeTable();  // Fake
        
        // 3. Load Data
        loadDataToTable();      // Load từ MySQL
        loadFakeCustomerData();
        loadFakeEmployeeData();

        // 4. Setup UI (Logic của Bro)
        setupChart("Revenue");
        loadBestSellers();
        
        sidebar.setTranslateX(-250);
        btnMenu.setOnMouseEntered(event -> openSidebar());
        sidebar.setOnMouseExited(event -> closeSidebar());

        // 5. Setup Form Logic (Logic của Bạn - Phức tạp hơn để khớp DB)
        setupFormLogic();
        updateHomeStats();

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

        showHome();


    }

    // ==================== LOGIC PRODUCT (DB THẬT) ====================

    private void setupProductTable() {
        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamePro()));
        colBrand.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBrand()));        
        colPrice.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSellingPrice()));
        colCategory.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCatePro()));
    }

    private void loadDataToTable(){
        // Lấy dữ liệu thật từ InventoryManager
        List<Product> items = inventoryManager.getAllItems();
        
        // Logic thêm dummy data của bạn nếu DB trống
        if (inventoryManager.getExistingProductCount() == 0) { 
             System.out.println("DB empty. Adding dummy data...");
             // (Giữ lại logic thêm dummy data của bạn ở đây nếu cần, hoặc bỏ qua)
        }

        productList = FXCollections.observableArrayList(items);
        productTable.setItems(productList);
    }

    // ==================== LOGIC FORM NHẬP LIỆU (CỦA BẠN) ====================

    private void setupFormLogic() {
        if (cbCatePro != null) {
            cbCatePro.setItems(FXCollections.observableArrayList("Instrument", "Accessory"));
            cbCatePro.valueProperty().addListener((obs, oldVal, newVal) -> handleMajorCategoryChange(newVal));
        }
        
        if (txtCateIns != null) {
            txtCateIns.setOnKeyReleased(event -> {
                if ("Instrument".equals(cbCatePro.getValue())) {
                    handleSubCategoryChange(txtCateIns.getText());
                }
            });
        }
    }

    private void handleMajorCategoryChange(String majorCategory) {
        boolean isInstrument = "Instrument".equals(majorCategory);
        boolean isAccessory = "Accessory".equals(majorCategory);
        
        if (tpInstrumentInfo != null) tpInstrumentInfo.setText(isInstrument ? "II. THÔNG TIN CHUNG NHẠC CỤ" : "II. THÔNG TIN CHUNG PHỤ KIỆN");
        if (lblCateIns != null) lblCateIns.setText(isInstrument ? "Instrument Type (Sub Category)" : "Material");
        if (lblIsElectric != null) lblIsElectric.setVisible(isInstrument);
        if (chkIsElectric != null) chkIsElectric.setVisible(isInstrument);

        if (isInstrument) {
            if (tpDetailInfo != null) tpDetailInfo.setText("III. THÔNG TIN CHI TIẾT NHẠC CỤ");
            handleSubCategoryChange(txtCateIns.getText()); 
        } else if (isAccessory) {
            if (tpDetailInfo != null) tpDetailInfo.setText("III. THÔNG TIN CHI TIẾT PHỤ KIỆN");
            setAllDetailFormsVisible(false);
            if (gpAccessoryDetail != null) gpAccessoryDetail.setVisible(true);
        } else {
            if (tpDetailInfo != null) tpDetailInfo.setText("III. CHỌN LOẠI SẢN PHẨM Ở MỤC I");
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
        // COPY NGUYÊN SI LOGIC CỦA BẠN ĐỂ ĐẢM BẢO KHỚP DB
        try {
            String name = txtNamePro.getText().trim();
            String brand = txtBrand.getText().trim();
            String origin = txtOrigin.getText().trim(); 
            String catePro = cbCatePro.getValue(); 
            
            if (name.isEmpty() || brand.isEmpty() || origin.isEmpty() || catePro == null) {
                showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng điền đầy đủ thông tin chung.");
                return;
            }

            double price = Double.parseDouble(txtPrice.getText().trim());
            int quantity = Integer.parseInt(txtQuantity.getText().trim());
            LocalDate localDate = dpImportDate.getValue();
            Date importDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            if ("Instrument".equals(catePro)) {
                String mateIns = txtMateIns.getText().trim();
                String colorIns = txtColorIns.getText().trim();
                boolean isElectric = chkIsElectric.isSelected();
                String subCategory = txtCateIns.getText().trim().toLowerCase(); 

                switch (subCategory) {
                    case "guitar":
                        inventoryManager.addNewGuitar(name, catePro, origin, brand, quantity, importDate, price, mateIns, subCategory, colorIns, isElectric, 
                            txtCateGui.getText().trim(), Integer.parseInt(txtStrNumGui.getText().trim()), txtBodyShapeGui.getText().trim());
                        break;
                    case "piano":
                        inventoryManager.addNewPiano(name, catePro, origin, brand, quantity, importDate, price, mateIns, subCategory, colorIns, isElectric, 
                            txtCatePi.getText().trim(), Integer.parseInt(txtKeyNumPi.getText().trim()), chkHasPedal.isSelected());
                        break;
                    case "keyboard":
                        inventoryManager.addNewKeyboard(name, catePro, origin, brand, quantity, importDate, price, mateIns, subCategory, colorIns, isElectric, 
                            txtCateKey.getText().trim(), Integer.parseInt(txtKeyNumKey.getText().trim()), chkHasLCD.isSelected());
                        break;
                    case "drumkit":
                        inventoryManager.addNewDrum(name, catePro, origin, brand, quantity, importDate, price, mateIns, subCategory, colorIns, isElectric, 
                            Integer.parseInt(txtNumOfDrumPieces.getText().trim()), Integer.parseInt(txtNumOfCymbals.getText().trim()), txtHeadMaterial.getText().trim(), txtShellMaterial.getText().trim());
                        break;
                    default:
                        showAlert(Alert.AlertType.WARNING, "Lỗi loại nhạc cụ", "Vui lòng nhập Guitar/Piano/Keyboard/Drumkit vào ô Sub Category.");
                        return;
                }
            } else if ("Accessory".equals(catePro)) {
                 inventoryManager.addNewAccessory(name, catePro, origin, brand, quantity, importDate, price, 
                    txtCateAcc.getText().trim(), txtMateIns.getText().trim(), txtColorIns.getText().trim(), txtCompatibleWith.getText().trim());
            }

            System.out.println("✅ Saved to DB: " + name);
            loadDataToTable(); // Reload bảng từ DB
            handleCancelAdd();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Giá trị số không hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi hệ thống", e.getMessage());
        }
    }

    // ==================== LOGIC UI (CỦA BRO) ====================

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

    private void updateHomeStats() {
        // 1. Lấy dữ liệu
        int totalProducts = inventoryManager.getExistingProductCount();
        double totalRevenue = inventoryManager.totalValue();
        int totalCustomers = customerList != null ? customerList.size() : 0; // Fake data tạm

        // 2. In log để kiểm tra
        System.out.println("--- UPDATE UI ---");
        System.out.println("Products: " + totalProducts);
        System.out.println("Revenue: " + totalRevenue);

        // 3. Cập nhật UI (Kèm ép màu chữ ĐEN ĐẬM để chống tàng hình)
        if (lblTotalProductsCount != null) {
            lblTotalProductsCount.setText(String.valueOf(totalProducts));
            lblTotalProductsCount.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 30px; -fx-font-weight: bold;"); 
        } else {
            System.err.println("LỖI: lblTotalProductsCount đang bị NULL (Chưa gán fx:id trong SceneBuilder)");
        }

        if (lblTotalCustomersCount != null) {
            lblTotalCustomersCount.setText(String.valueOf(totalCustomers));
            lblTotalCustomersCount.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 30px; -fx-font-weight: bold;");
        }

        if (lblTotalRevenueValue != null) {
            lblTotalRevenueValue.setText(String.format("$%,.2f", totalRevenue));
            lblTotalRevenueValue.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 30px; -fx-font-weight: bold;");
        }

        // 4. Update Chart
        setupChart("Revenue"); 
        
        // 5. Update Best Seller
        loadBestSellers();
    }

    @FXML private void showProducts() {
        loadDataToTable();
        homeView.setVisible(false); productView.setVisible(true); productView.toFront();
        customerView.setVisible(false); employeeView.setVisible(false); addProductView.setVisible(false);
        resetButtonStyles(); btnProducts.getStyleClass().add("active");
    }

    @FXML private void showCustomers() {
        homeView.setVisible(false); productView.setVisible(false); employeeView.setVisible(false); addProductView.setVisible(false);
        customerView.setVisible(true); customerView.toFront();
        resetButtonStyles(); if(btnCustomers != null) btnCustomers.getStyleClass().add("active");
    }

    @FXML private void showEmployees() {
        homeView.setVisible(false); productView.setVisible(false); customerView.setVisible(false); addProductView.setVisible(false);
        employeeView.setVisible(true); employeeView.toFront();
        resetButtonStyles(); if(btnEmployees != null) btnEmployees.getStyleClass().add("active");
    }

    @FXML private void showAddProductForm() {
        productTable.setVisible(false); homeView.setVisible(false); 
        addProductView.setVisible(true); addProductView.toFront();
        clearForm();
    }

    @FXML private void handleCancelAdd() {
        addProductView.setVisible(false);
        productTable.setVisible(true);
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
        if(txtCateIns != null) txtCateIns.clear();
        // (Thêm clear các trường detail khác nếu cần)
    }

    // --- FAKE DATA LOGIC (GIỮ LẠI CHO EMPLOYEE/CUSTOMER) ---
    private void setupEmployeeTable() {
        colEmpID.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEID()));
        colEmpName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNameEmp()));
        colEmpPos.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosEmp()));
        colEmpSal.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSalEmp()));
        colEmpDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getHireDate()));
    }

    private void loadFakeEmployeeData() {
        employeeList = FXCollections.observableArrayList();
        employeeList.add(new Employee("Phan Anh", "E001", "Manager", 2000, new Date()));
        employeeList.add(new Employee("Teamate", "E002", "Dev", 1500, new Date()));
        employeeTable.setItems(employeeList);
        if(lblTotalEmp != null) lblTotalEmp.setText(String.valueOf(employeeList.size()));
        if(lblTotalSalary != null) lblTotalSalary.setText("$3500");
        if(lblManagerName != null) lblManagerName.setText("Phan Anh");
    }

    private void setupCustomerTable() {
        colCusName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNameCus()));
        colCusCSN.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCSN()));
        colCusPhone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNum()));
        colCusEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmailCus()));
        colCusAddress.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddCus()));
    }

    private void loadFakeCustomerData() {
        customerList = FXCollections.observableArrayList();
        customerList.add(new Customer("Nguyen Van A", "079123456", "0909123456", "a@gmail.com", "HCMC"));
        customerTable.setItems(customerList);
        if(lblTotalCus != null) lblTotalCus.setText("1");
    }

    private void setupChart(String type){ 
        if (revenueChart == null) return;

        revenueChart.getData().clear();
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(type);

        switch (type){
            case "Products":
                revenueChart.setTitle("Monthly Product Import");
                series.getData().add(new XYChart.Data<>("Jan", 50));
                series.getData().add(new XYChart.Data<>("Feb", 80));
                series.getData().add(new XYChart.Data<>("Mar", 120));
                series.getData().add(new XYChart.Data<>("Apr", 90));
                series.getData().add(new XYChart.Data<>("May", inventoryManager.getExistingProductCount())); // Số thực tế
                break;

            case "Customers":
                revenueChart.setTitle("New Customers Growth");
                series.getData().add(new XYChart.Data<>("Jan", 10));
                series.getData().add(new XYChart.Data<>("Feb", 15));
                series.getData().add(new XYChart.Data<>("Mar", 25));
                series.getData().add(new XYChart.Data<>("Apr", 40));
                series.getData().add(new XYChart.Data<>("May", 55));
                break;

            case "Revenue":
                revenueChart.setTitle("Monthly Revenue ($)");
                series.getData().add(new XYChart.Data<>("Jan", 5000));
                series.getData().add(new XYChart.Data<>("Feb", 12000));
                series.getData().add(new XYChart.Data<>("Mar", 8000));
                series.getData().add(new XYChart.Data<>("Apr", 18500));
                // Lấy số liệu thực tế cho tháng hiện tại (cộng thêm 1 chút cho chart nó đẹp)
                series.getData().add(new XYChart.Data<>("May", inventoryManager.totalValue() > 0 ? inventoryManager.totalValue() : 20000)); 
                break;
        }

        revenueChart.getData().add(series);
    }
    
    
    // --- LOGIC BEST SELLER & 3D ANIMATION ---

    private void loadBestSellers() {
        if (pnBestSellers == null) return;
        
        pnBestSellers.getChildren().clear();
        
        // Thêm dữ liệu giả (Fake Data)
        // Lưu ý: File ảnh phải có trong thư mục src/main/resources/images/
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
        card.setPadding(new javafx.geometry.Insets(10)); // Thêm padding cho đẹp
        
        // Ảnh
        ImageView imageView = new ImageView();
        try {
            // Sửa đường dẫn để an toàn hơn
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
            loadDataToTable(); // Cập nhật bảng Product
            updateHomeStats(); // Cập nhật 3 cái thẻ Home
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
}