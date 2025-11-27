package com.yourteamname.store.controller;

import com.yourteamname.store.model.Product;
import com.yourteamname.store.service.InventoryManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane; 
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart; 
import javafx.scene.input.KeyEvent; // Cần thiết cho sự kiện KeyReleased

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.List;
import java.time.ZoneId;

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

    // --- KHAI BÁO FORM NHẬP LIỆU ---
    @FXML private AnchorPane addProductView; 
    
    // SECTION 1: Product Info 
    @FXML private TitledPane tpInstrumentInfo;
    @FXML private TitledPane tpDetailInfo;
    @FXML private TextField txtNamePro;
    @FXML private TextField txtBrand;   
    @FXML private TextField txtOrigin;  
    @FXML private TextField txtPrice;   
    @FXML private TextField txtQuantity;
    @FXML private ComboBox<String> cbCatePro; 
    @FXML private DatePicker dpImportDate; 
    
    // SECTION 2: Instrument/Accessory General Info
    @FXML private Label lblCateIns; // Label cho trường Sub Category
    @FXML private Label lblIsElectric; // Label cho Checkbox Electric
    @FXML private TextField txtMateIns; 
    @FXML private TextField txtColorIns; 
    @FXML private CheckBox chkIsElectric; 
    @FXML private TextField txtCateIns; // Dùng cho Sub Category (Instrument) hoặc Material (Accessory)

    // CONTAINER CHO CÁC FORM CON
    @FXML private AnchorPane apDetailContainer; 

    // SECTION 3: Guitar Detail Fields
    @FXML private GridPane gpGuitarDetail;
    @FXML private TextField txtCateGui;
    @FXML private TextField txtStrNumGui;
    @FXML private TextField txtBodyShapeGui;

    // SECTION 3: Piano Detail Fields
    @FXML private GridPane gpPianoDetail;
    @FXML private TextField txtCatePi;
    @FXML private TextField txtKeyNumPi;
    @FXML private CheckBox chkHasPedal;

    // SECTION 3: Keyboard Detail Fields
    @FXML private GridPane gpKeyboardDetail;
    @FXML private TextField txtCateKey;
    @FXML private TextField txtKeyNumKey;
    @FXML private CheckBox chkHasLCD;

    // SECTION 3: DrumKit Detail Fields
    @FXML private GridPane gpDrumKitDetail;
    @FXML private TextField txtNumOfDrumPieces;
    @FXML private TextField txtNumOfCymbals;
    @FXML private TextField txtHeadMaterial;
    @FXML private TextField txtShellMaterial;

    // SECTION 3: Accessory Detail Fields
    @FXML private GridPane gpAccessoryDetail;
    @FXML private TextField txtCateAcc;
    @FXML private TextField txtCompatibleWith;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Dashboard is loading...");

        inventoryManager = new InventoryManager(); 
        
        // --- THÊM LOGIC RESET DATABASE BẮT BUỘC ĐỂ TEST CASE ĐÚNG ---
        // Chúng ta reset DB để đảm bảo getExistingProductCount() == 0 
        // và logic thêm dữ liệu mẫu được kích hoạt một cách đáng tin cậy.
        inventoryManager.resetDatabase();
        // -----------------------------------------------------------
        
        System.out.println("Number of items in inventory (pre-load check): " + inventoryManager.getAllItems().size());

        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamePro()));
        colBrand.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBrand()));        
        colPrice.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSellingPrice()));
        colCategory.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCatePro()));

        loadDataToTable();

        setupChart("Revenue");
        cardProduct.setOnMouseClicked(event -> {setupChart("Products");});
        cardCustomer.setOnMouseClicked(event -> {setupChart("Customers");});
        cardRevenue.setOnMouseClicked(event -> {setupChart("Revenue");});
        showHome();
        
        // Thiết lập ComboBox Category chính và lắng nghe sự kiện
        if (cbCatePro != null) {
            cbCatePro.setItems(FXCollections.observableArrayList("Instrument", "Accessory"));
            cbCatePro.getSelectionModel().selectFirst();
            // Lắng nghe sự kiện thay đổi Category chính (Major Category)
            cbCatePro.valueProperty().addListener((obs, oldVal, newVal) -> handleMajorCategoryChange(newVal));
        }
        
        // Lắng nghe sự kiện gõ phím trên trường Sub Category để cập nhật Form Detail (Chỉ cho Instrument)
        if (txtCateIns != null) {
            txtCateIns.setOnKeyReleased(event -> {
                if ("Instrument".equals(cbCatePro.getValue())) {
                    handleSubCategoryChange(txtCateIns.getText());
                }
            });
        }
        
        // Gọi hàm setup ban đầu (mặc định là Instrument)
        handleMajorCategoryChange(cbCatePro.getValue()); 
    }
    
    // --- HÀM ẨN/HIỆN FORM CHI TIẾT DỰA TRÊN CATEGORY CHÍNH (Instrument/Accessory) ---
    private void handleMajorCategoryChange(String majorCategory) {
        boolean isInstrument = "Instrument".equals(majorCategory);
        boolean isAccessory = "Accessory".equals(majorCategory);
        
        // --- Cập nhật UI chung (SECTION II) ---
        tpInstrumentInfo.setText(isInstrument ? "II. THÔNG TIN CHUNG NHẠC CỤ" : "II. THÔNG TIN CHUNG PHỤ KIỆN");
        lblCateIns.setText(isInstrument ? "Instrument Type (Sub Category)" : "Material");
        lblIsElectric.setVisible(isInstrument);
        chkIsElectric.setVisible(isInstrument);

        // --- Cập nhật UI chi tiết (SECTION III) ---
        if (isInstrument) {
            tpDetailInfo.setText("III. THÔNG TIN CHI TIẾT NHẠC CỤ");
            // Khi chuyển sang Instrument, kích hoạt logic Sub Category
            handleSubCategoryChange(txtCateIns.getText()); 
        } else if (isAccessory) {
            tpDetailInfo.setText("III. THÔNG TIN CHI TIẾT PHỤ KIỆN");
            // Hiển thị Accessory Form và ẩn tất cả form nhạc cụ
            setAllDetailFormsVisible(false);
            gpAccessoryDetail.setVisible(true);
        } else {
            tpDetailInfo.setText("III. CHỌN LOẠI SẢN PHẨM Ở MỤC I");
            setAllDetailFormsVisible(false);
        }
    }

    // --- HÀM ẨN/HIỆN FORM CHI TIẾT DỰA TRÊN SUB CATEGORY (Instrument only) ---
    private void handleSubCategoryChange(String subCategory) {
        
        // Luôn ẩn Accessory khi gọi hàm này (vì chỉ dùng cho Instrument)
        setAllDetailFormsVisible(false);
        
        String normalizedCategory = subCategory.trim().toLowerCase();
        
        // Hiện form chi tiết tương ứng
        if (normalizedCategory.contains("guitar")) {
            gpGuitarDetail.setVisible(true);
        } else if (normalizedCategory.contains("piano")) {
            gpPianoDetail.setVisible(true);
        } else if (normalizedCategory.contains("key")) {
            gpKeyboardDetail.setVisible(true);
        } else if (normalizedCategory.contains("drum")) {
            gpDrumKitDetail.setVisible(true);
        }
    }
    
    // Hàm tiện ích để ẩn tất cả các form chi tiết
    private void setAllDetailFormsVisible(boolean visible) {
        if (gpGuitarDetail != null) gpGuitarDetail.setVisible(visible);
        if (gpPianoDetail != null) gpPianoDetail.setVisible(visible);
        if (gpKeyboardDetail != null) gpKeyboardDetail.setVisible(visible);
        if (gpDrumKitDetail != null) gpDrumKitDetail.setVisible(visible);
        if (gpAccessoryDetail != null) gpAccessoryDetail.setVisible(visible);
    }


    private void loadDataToTable(){
        List<Product> items = inventoryManager.getAllItems();
        
        // --- LOGIC THÊM DỮ LIỆU MẪU ĐƯỢC KÍCH HOẠT KHI DB TRỐNG ---
        if (inventoryManager.getExistingProductCount() == 0) { 
            System.out.println("Attempting to add initial dummy data...");
            try {
                // Thêm dữ liệu mẫu (đã đảm bảo chữ thường cho cateIns)
                inventoryManager.addNewGuitar(
                    "Fender Stratocaster", "Instrument", "USA", "Fender", 10, new Date(), 1500.0,
                    "Alder",      // mateIns
                    "guitar",     // cateIns
                    "Sunburst",   // colorIns
                    true,         // isElectric

                    "electric",   // cateGui (loại guitar)
                    6,
                    "Double Cutaway"
                );
                inventoryManager.addNewPiano(
                    "Yamaha U1", "Instrument", "Japan", "Yamaha", 5, new Date(), 5000.0,
                    "Spruce", "piano", "Black", false, "Upright", 88, true
                );
                inventoryManager.addNewAccessory(
                    "Ernie Ball Slinky red", "Accessory", "USA", "Ernie Ball", 50, new Date(), 10.0,
                    "Strings", "Steel", "Silver", "Guitar"
                );
                
                // Tải lại dữ liệu sau khi thêm
                items = inventoryManager.getAllItems();
                
            } catch (Exception ex) {
                System.err.println("❌ Error adding dummy data: " + ex.getMessage());
                showAlert(Alert.AlertType.ERROR, "Lỗi Database", "Không thể thêm dữ liệu mẫu. Vui lòng kiểm tra MySQL logs.");
            }
        }
        
        if (items.isEmpty()) {
             System.out.println("Warning: List is empty or retrieval failed!");
             showAlert(Alert.AlertType.WARNING, "Lỗi tải dữ liệu", "Không có sản phẩm nào được tải. Vui lòng kiểm tra lại kết nối DB và dữ liệu.");
        }

        productList = FXCollections.observableArrayList(items);
        productTable.setItems(productList);
        System.out.println("Successfully loaded data into table! Total items: " + productList.size());
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
        loadDataToTable(); 
        homeView.setVisible(false);
        productTable.setVisible(true);
        productTable.toFront();
        
        resetButtonStyles();

        btnProducts.getStyleClass().add("active");
    }

    @FXML
    private void showAddProductForm() {
        productTable.setVisible(false);
        homeView.setVisible(false);
        
        addProductView.setVisible(true);
        addProductView.toFront();
        
        clearForm();
    }

    @FXML
    private void handleCancelAdd() {
        addProductView.setVisible(false);
        productTable.setVisible(true);
    }

    private void clearForm() {
        // Clear Product General fields
        if(txtNamePro != null) txtNamePro.clear();
        if(txtBrand != null) txtBrand.clear();
        if(txtOrigin != null) txtOrigin.clear();
        if(txtPrice != null) txtPrice.clear();
        if(txtQuantity != null) txtQuantity.clear();
        if(dpImportDate != null) dpImportDate.setValue(LocalDate.now());
        if(cbCatePro != null) cbCatePro.getSelectionModel().selectFirst();
        
        // Clear Instrument General fields
        if(txtMateIns != null) txtMateIns.clear();
        if(txtColorIns != null) txtColorIns.clear();
        if(chkIsElectric != null) chkIsElectric.setSelected(false);
        if(txtCateIns != null) txtCateIns.clear(); 
        
        // Clear các trường chi tiết
        if(txtCateGui != null) txtCateGui.clear();
        if(txtStrNumGui != null) txtStrNumGui.clear();
        if(txtBodyShapeGui != null) txtBodyShapeGui.clear();
        if(txtCatePi != null) txtCatePi.clear();
        if(txtKeyNumPi != null) txtKeyNumPi.clear();
        if(chkHasPedal != null) chkHasPedal.setSelected(false);
        if(txtCateKey != null) txtCateKey.clear();
        if(txtKeyNumKey != null) txtKeyNumKey.clear();
        if(chkHasLCD != null) chkHasLCD.setSelected(false);
        if(txtNumOfDrumPieces != null) txtNumOfDrumPieces.clear();
        if(txtNumOfCymbals != null) txtNumOfCymbals.clear();
        if(txtHeadMaterial != null) txtHeadMaterial.clear();
        if(txtShellMaterial != null) txtShellMaterial.clear();
        if(txtCateAcc != null) txtCateAcc.clear();
        if(txtCompatibleWith != null) txtCompatibleWith.clear();
        
        // Thiết lập lại trạng thái ẩn/hiện ban đầu
        handleMajorCategoryChange(cbCatePro.getValue()); 
    }

    @FXML
    private void handleSaveProduct() {
        try {
            // --- A. LẤY DỮ LIỆU CHUNG (Product) ---
            String name = txtNamePro.getText().trim();
            String brand = txtBrand.getText().trim();
            String origin = txtOrigin.getText().trim(); 
            String catePro = cbCatePro.getValue(); 
            
            // Validation cơ bản
            if (name.isEmpty() || brand.isEmpty() || origin.isEmpty() || catePro == null) {
                throw new IllegalArgumentException("Vui lòng điền đầy đủ thông tin chung của sản phẩm.");
            }

            double price = Double.parseDouble(txtPrice.getText().trim());
            int quantity = Integer.parseInt(txtQuantity.getText().trim());
            
            LocalDate localDate = dpImportDate.getValue();
            java.util.Date importDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // --- B. LƯU SẢN PHẨM DỰA TRÊN CATEGORY CHÍNH ---
            if ("Instrument".equals(catePro)) {
                
                // Lấy dữ liệu Instrument chung
                String mateIns = txtMateIns.getText().trim();
                String colorIns = txtColorIns.getText().trim();
                boolean isElectric = chkIsElectric.isSelected();
                String subCategory = txtCateIns.getText().trim().toLowerCase(); 
                
                if (mateIns.isEmpty() || colorIns.isEmpty() || subCategory.isEmpty()) {
                     throw new IllegalArgumentException("Vui lòng điền đầy đủ thông tin chung của nhạc cụ.");
                }

                // --- SỬ DỤNG SWITCH-CASE ĐỂ CHỌN ĐÚNG PHƯƠNG THỨC LƯU ---
                switch (subCategory) {
                    case "guitar":
                        inventoryManager.addNewGuitar(
                            name, catePro, origin, brand, quantity, importDate, price, 
                            mateIns, subCategory, colorIns, isElectric, 
                            txtCateGui.getText().trim(), 
                            Integer.parseInt(txtStrNumGui.getText().trim()), 
                            txtBodyShapeGui.getText().trim()
                        );
                        break;
                    case "piano":
                         inventoryManager.addNewPiano(
                            name, catePro, origin, brand, quantity, importDate, price, 
                            mateIns, subCategory, colorIns, isElectric, 
                            txtCatePi.getText().trim(), 
                            Integer.parseInt(txtKeyNumPi.getText().trim()), 
                            chkHasPedal.isSelected()
                        );
                        break;
                    case "keyboard":
                        inventoryManager.addNewKeyboard(
                            name, catePro, origin, brand, quantity, importDate, price, 
                            mateIns, subCategory, colorIns, isElectric, 
                            txtCateKey.getText().trim(), 
                            Integer.parseInt(txtKeyNumKey.getText().trim()), 
                            chkHasLCD.isSelected()
                        );
                        break;
                    case "drumkit":
                        inventoryManager.addNewDrum(
                            name, catePro, origin, brand, quantity, importDate, price, 
                            mateIns, subCategory, colorIns, isElectric, 
                            Integer.parseInt(txtNumOfDrumPieces.getText().trim()),
                            Integer.parseInt(txtNumOfCymbals.getText().trim()),
                            txtHeadMaterial.getText().trim(), 
                            txtShellMaterial.getText().trim()
                        );
                        break;
                    default:
                        throw new IllegalArgumentException("Vui lòng nhập loại nhạc cụ con hợp lệ (Guitar/Piano/Keyboard/Drumkit).");
                }
            } else if ("Accessory".equals(catePro)) {
                 // Lấy dữ liệu Accessory (Sử dụng trường MateIns/ColorIns cho Material/Color của Accessory)
                 if (txtCateAcc.getText().trim().isEmpty() || txtCompatibleWith.getText().trim().isEmpty() || txtMateIns.getText().trim().isEmpty()) {
                      throw new IllegalArgumentException("Vui lòng điền đầy đủ thông tin chi tiết của phụ kiện (Category, Compatible With, Material).");
                 }
                 inventoryManager.addNewAccessory(
                    name, catePro, origin, brand, quantity, importDate, price, 
                    txtCateAcc.getText().trim(), 
                    txtMateIns.getText().trim(), 
                    txtColorIns.getText().trim(), 
                    txtCompatibleWith.getText().trim()
                 );
            } else {
                 throw new IllegalArgumentException("Lỗi: Không tìm thấy Category chính.");
            }

            // --- C. THÀNH CÔNG ---
            System.out.println("✅ Saved: " + name);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm sản phẩm " + name + " vào kho!");
            loadDataToTable();
            handleCancelAdd();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Giá, Số lượng, Số dây/phím phải là số hợp lệ.");
            System.err.println("❌ Lỗi nhập liệu: " + e.getMessage());
        } catch (IllegalArgumentException e) {
             showAlert(Alert.AlertType.WARNING, "Lỗi nghiệp vụ", e.getMessage());
             System.err.println("❌ Lỗi nghiệp vụ: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi không xác định", "Không thể lưu sản phẩm. Vui lòng kiểm tra console log.");
            System.err.println("❌ Lỗi không xác định khi lưu sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Hàm tiện ích để hiển thị Alert
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void setupChart(String type){
        // Xoá và setup lại series
        revenueChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(type + " Overview");

        switch (type){
            case "Products":
                // Sửa lỗi: Chỉ cần sử dụng XYChart.Data
                series.getData().add(new XYChart.Data<>("Jan", 50));
                series.getData().add(new XYChart.Data<>("Feb", 80));
                series.getData().add(new XYChart.Data<>("Mar", 150));
                revenueChart.setTitle("Monthly Product Import");
                break;
            case "Customers":
                // Sửa lỗi: Chỉ cần sử dụng XYChart.Data
                series.getData().add(new XYChart.Data<>("Jan", 20));
                series.getData().add(new XYChart.Data<>("Feb", 45));
                series.getData().add(new XYChart.Data<>("Mar", 90));
                revenueChart.setTitle("Customers Growth");
                break;
            case "Revenue":
                // Sửa lỗi: Chỉ cần sử dụng XYChart.Data
                series.getData().add(new XYChart.Data<>("Jan", 5000));
                series.getData().add(new XYChart.Data<>("Feb", 12000));
                series.getData().add(new XYChart.Data<>("Mar", 25000));
                revenueChart.setTitle("Monthly Revenue ($)");
                break;
        }
        revenueChart.getData().add(series);
    }
}