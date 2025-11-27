package com.yourteamname.store.service;

import com.yourteamname.store.model.*; 
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicInteger; 

public class InventoryManager {

    // --- CẤU HÌNH CƠ SỞ DỮ LIỆU ---
    private static final String BASE_DB_URL = "jdbc:mysql://localhost:3306"; 
    private static final String CONNECTION_PARAMS = "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC"; 
    private static final String DB_USER = "root";       
    private static final String DB_PASSWORD = "Peter@18122005"; 
    private static final String DATABASE_NAME = "musical_store_db";
    
    // Tên các bảng
    private static final String PRODUCT_TABLE = "Product";
    private static final String INSTRUMENT_TABLE = "Instrument"; 
    private static final String GUITAR_DETAIL_TABLE = "GuitarDetail";
    private static final String PIANO_DETAIL_TABLE = "PianoDetail";
    private static final String KEYBOARD_DETAIL_TABLE = "KeyboardDetail";
    private static final String DRUMKIT_DETAIL_TABLE = "DrumKitDetail";
    private static final String ACCESSORY_DETAIL_TABLE = "AccessoryDetail";
    
    // Counter cho ID String (ĐÃ KHÔI PHỤC LOGIC PHÂN TÁCH CHO TỪNG LOẠI)
    private int guitarCounter = 1;
    private int pianoCounter = 1;
    private int keyboardCounter = 1;
    private int drumCounter = 1;
    private int accessoryCounter = 1;

    private static final String GUITAR_PREFIX = "111";
    private static final String PIANO_PREFIX = "112";
    private static final String KEYBOARD_PREFIX = "113";
    private static final String DRUM_PREFIX = "114";
    private static final String ACCESSORY_PREFIX = "115"; 

    public InventoryManager(){
        setupDatabase();
        // Cố gắng khởi tạo tất cả các bộ đếm từ DB
        initializeIdCounters();
    }
    
    // --- PHƯƠNG THỨC RESET DATABASE (GIỮ NGUYÊN) ---
    public void resetDatabase() {
        System.out.println("--- Starting RESET DATABASE: " + DATABASE_NAME + " ---");
        try (Connection conn = DriverManager.getConnection(BASE_DB_URL + CONNECTION_PARAMS, DB_USER, DB_PASSWORD)) {
            try (Statement stmt = conn.createStatement()) {
                String dropSql = "DROP DATABASE IF EXISTS " + DATABASE_NAME;
                stmt.executeUpdate(dropSql);
                System.out.println("Success: Old database dropped.");
            }
        } catch (SQLException e) {
            System.err.println("Error: Failed to drop database: " + e.getMessage());
            e.printStackTrace(); 
            return;
        }
        setupDatabase();
        System.out.println("Success: Database re-initialized with new structure.");
        // Reset counter sau khi reset DB
        guitarCounter = 1;
        pianoCounter = 1;
        keyboardCounter = 1;
        drumCounter = 1;
        accessoryCounter = 1;
    }

    // --- PHƯƠNG THỨC KẾT NỐI VÀ KHỞI TẠO DB (GIỮ NGUYÊN) ---
    
    private void setupDatabase() {
        // Kết nối để tạo database
        try (Connection conn = DriverManager.getConnection(BASE_DB_URL + CONNECTION_PARAMS, DB_USER, DB_PASSWORD)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
            }
        } catch (SQLException e) {
            System.err.println("Error: Failed to connect or create database: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Kết nối lại để tạo bảng
        try (Connection conn = getConnection(true)) { 
            if (conn != null) {
                createTables(conn);
            }
        } catch (SQLException e) {
            System.err.println("Error: Failed to create tables/initialize: " + e.getMessage());
            e.printStackTrace(); 
        }
    }

    public Connection getConnection() throws SQLException {
        return getConnection(false);
    }
    
    public Connection getConnection(boolean useDb) throws SQLException {
        String url = useDb 
            ? BASE_DB_URL + "/" + DATABASE_NAME + CONNECTION_PARAMS 
            : BASE_DB_URL + CONNECTION_PARAMS;
        
        try {
            return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database CONNECTION FAILED: " + e.getMessage());
            throw e; 
        }
    }
    
    // PHƯƠNG THỨC TẠO BẢNG (GIỮ NGUYÊN LOGIC CŨ)
    private void createTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // 1. Product (Không đổi)
            String createProductTable = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                "id VARCHAR(10) PRIMARY KEY," +
                "namePro VARCHAR(255) NOT NULL," +
                "catePro VARCHAR(50) NOT NULL," +
                "origin VARCHAR(100)," +
                "brand VARCHAR(100)," +
                "quantityInStock INT NOT NULL," +
                "importDate DATE," +
                "sellingPrice DOUBLE NOT NULL" +
                ")", PRODUCT_TABLE);
            stmt.executeUpdate(createProductTable);

            // 2. Instrument (Bảng chung cho nhạc cụ)
            String createInstrumentTable = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                "product_id VARCHAR(10) PRIMARY KEY," +
                "cateIns VARCHAR(50) NOT NULL," + // Loại nhạc cụ (Guitar, Piano,...)
                "mateIns VARCHAR(100)," +
                "colorIns VARCHAR(50)," +
                "isElectric BOOLEAN," +
                "FOREIGN KEY (product_id) REFERENCES %s(id) ON DELETE CASCADE" +
                ")", INSTRUMENT_TABLE, PRODUCT_TABLE);
            stmt.executeUpdate(createInstrumentTable);

            // 3. GuitarDetail
            String createGuitarDetailTable = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                "product_id VARCHAR(10) PRIMARY KEY," +
                "cateGui VARCHAR(100)," +
                "strNumGui INT," +
                "bodyShapeGui VARCHAR(100)," +
                "FOREIGN KEY (product_id) REFERENCES %s(product_id) ON DELETE CASCADE" +
                ")", GUITAR_DETAIL_TABLE, INSTRUMENT_TABLE);
            stmt.executeUpdate(createGuitarDetailTable);
            
            // 4. PianoDetail
             String createPianoDetailTable = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                "product_id VARCHAR(10) PRIMARY KEY," +
                "catePi VARCHAR(100)," +
                "keyNumPi INT," +
                "hasPedal BOOLEAN," +
                "FOREIGN KEY (product_id) REFERENCES %s(product_id) ON DELETE CASCADE" +
                ")", PIANO_DETAIL_TABLE, INSTRUMENT_TABLE);
            stmt.executeUpdate(createPianoDetailTable);

            // 5. KeyboardDetail
            String createKeyboardDetailTable = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                "product_id VARCHAR(10) PRIMARY KEY," +
                "cateKey VARCHAR(100)," +
                "keyNumKey INT," +
                "hasLCD BOOLEAN," +
                "FOREIGN KEY (product_id) REFERENCES %s(product_id) ON DELETE CASCADE" +
                ")", KEYBOARD_DETAIL_TABLE, INSTRUMENT_TABLE);
            stmt.executeUpdate(createKeyboardDetailTable);

            // 6. DrumKitDetail
            String createDrumKitDetailTable = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                "product_id VARCHAR(10) PRIMARY KEY," +
                "numOfDrumPieces INT," +
                "numOfCymbals INT," +
                "headMaterial VARCHAR(100)," +
                "shellMaterial VARCHAR(100)," +
                "FOREIGN KEY (product_id) REFERENCES %s(product_id) ON DELETE CASCADE" +
                ")", DRUMKIT_DETAIL_TABLE, INSTRUMENT_TABLE);
            stmt.executeUpdate(createDrumKitDetailTable);

            // 7. AccessoryDetail
            String createAccessoryDetailTable = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                "product_id VARCHAR(10) PRIMARY KEY," +
                "cateAcc VARCHAR(100)," +
                "mateAcc VARCHAR(100)," + 
                "colorAcc VARCHAR(50)," +
                "compatibleWith VARCHAR(100)," + 
                "FOREIGN KEY (product_id) REFERENCES %s(id) ON DELETE CASCADE" +
                ")", ACCESSORY_DETAIL_TABLE, PRODUCT_TABLE);
            stmt.executeUpdate(createAccessoryDetailTable);

            System.out.println("Success: Created tables (Product, Instrument, Detail...)");
        }
    }
    
    // PHƯƠNG THỨC KHỞI TẠO TẤT CẢ CÁC BỘ ĐẾM RIÊNG BIỆT (ĐÃ KHÔI PHỤC)
    private void initializeIdCounters() {
        String sql = "SELECT MAX(id) FROM " + PRODUCT_TABLE + " WHERE id LIKE ?";
        
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            java.util.function.BiConsumer<String, java.util.function.IntConsumer> getMaxCounter = (prefix, setter) -> {
                try {
                    pstmt.setString(1, prefix + "%");
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next() && rs.getString(1) != null) {
                            String maxId = rs.getString(1);
                            // Lấy 3 chữ số cuối
                            int maxCount = Integer.parseInt(maxId.substring(3)) + 1;
                            setter.accept(maxCount);
                        }
                    }
                } catch (SQLException | NumberFormatException | StringIndexOutOfBoundsException e) {
                     System.err.println("Error initializing counter for prefix " + prefix + ": " + e.getMessage());
                }
            };
            
            getMaxCounter.accept(GUITAR_PREFIX, count -> this.guitarCounter = count);
            getMaxCounter.accept(PIANO_PREFIX, count -> this.pianoCounter = count);
            getMaxCounter.accept(KEYBOARD_PREFIX, count -> this.keyboardCounter = count);
            getMaxCounter.accept(DRUM_PREFIX, count -> this.drumCounter = count);
            getMaxCounter.accept(ACCESSORY_PREFIX, count -> this.accessoryCounter = count);
            
            System.out.println("Success: Initialized ID counters.");
            System.out.printf("Current Counters: Guitar=%d, Piano=%d, Keyboard=%d, Drum=%d, Accessory=%d\n", 
                                guitarCounter, pianoCounter, keyboardCounter, drumCounter, accessoryCounter);
        } catch (SQLException e) {
            System.err.println("Error initializing counters: " + e.getMessage());
        }
    }
    
    // --- PHƯƠNG THỨC INSERT CHUNG ---

    private java.sql.Date convertUtilToSqlDate(Date utilDate) {
        if (utilDate == null) return null;
        return new java.sql.Date(utilDate.getTime());
    }
    
    // Khôi phục logic tạo ID phân tách
    private String generateNextProductId(String prefix, String type) {
        int currentCounter;
        switch(type.toLowerCase()) {
            case "guitar":
                currentCounter = this.guitarCounter++;
                break;
            case "piano":
                currentCounter = this.pianoCounter++;
                break;
            case "keyboard":
                currentCounter = this.keyboardCounter++;
                break;
            case "drumkit":
                currentCounter = this.drumCounter++;
                break;
            case "accessory":
                currentCounter = this.accessoryCounter++;
                break;
            default:
                throw new IllegalArgumentException("Unknown product type for ID generation: " + type);
        }
        
        String formatCounter = String.format("%03d", currentCounter);
        return prefix + formatCounter;
    }

    private void insertProduct(Product product) throws SQLException {
        // Giữ lại INSERT Product (Bước 1)
        String sql = String.format(
            "INSERT INTO %s (id, namePro, catePro, origin, brand, quantityInStock, importDate, sellingPrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", PRODUCT_TABLE);

        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, product.getId());
            pstmt.setString(2, product.getNamePro());
            pstmt.setString(3, product.getCatePro());
            pstmt.setString(4, product.getOrigin());
            pstmt.setString(5, product.getBrand());
            pstmt.setInt(6, product.getQuantityInStock());
            pstmt.setDate(7, convertUtilToSqlDate(product.getImportDate()));
            pstmt.setDouble(8, product.getSellingPrice());

            pstmt.executeUpdate();
        }
    }
    
    private void insertInstrument(Instrument instrument) throws SQLException {
        // Giữ lại INSERT Instrument (Bước 2)
        String sql = String.format(
            "INSERT INTO %s (product_id, cateIns, mateIns, colorIns, isElectric) VALUES (?, ?, ?, ?, ?)", INSTRUMENT_TABLE);

        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, instrument.getId());
            String cleanCateIns = instrument.getCateIns().trim().toLowerCase(); 
            pstmt.setString(2, cleanCateIns); 
            pstmt.setString(3, instrument.getMateIns());
            pstmt.setString(4, instrument.getColorIns());
            pstmt.setBoolean(5, instrument.isElectric());
            
            pstmt.executeUpdate();
        }
    }

    // --- 1. ADD NEW GUITAR ---

    public void addNewGuitar(
        String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,
        String mateIns,
        String cateIns,
        String colorIns,
        boolean isElectric,
        String cateGui,
        int numOfString,
        String bodyShapeGui
    ) {
        try {
            // Sử dụng logic tạo ID phân tách
            String nextGuiId = generateNextProductId(GUITAR_PREFIX, "guitar");
            
            // 1. Tạo đối tượng Guitar
            Guitar newGuitar = new Guitar(
                       nextGuiId, namePro, catePro, origin, brand,
                       quantityInStock, importDate, sellingPrice,
                       cateIns, mateIns, colorIns, isElectric,
                       cateGui, numOfString, bodyShapeGui);

            // 2. Lưu vào bảng Product
            insertProduct(newGuitar);

            // 3. Lưu vào bảng Instrument
            insertInstrument(newGuitar);

            // 4. Lưu chi tiết vào bảng GuitarDetail 
            String sqlDetail = String.format("INSERT INTO %s (product_id, cateGui, strNumGui, bodyShapeGui) VALUES (?, ?, ?, ?)", GUITAR_DETAIL_TABLE);
            try (Connection conn = getConnection(true);
                 PreparedStatement pstmt = conn.prepareStatement(sqlDetail)) {
                pstmt.setString(1, nextGuiId);
                pstmt.setString(2, cateGui);
                pstmt.setInt(3, numOfString);
                pstmt.setString(4, bodyShapeGui);
                pstmt.executeUpdate();
            }

            System.out.println("Success: Added Guitar " + nextGuiId + " to DB.");
        } catch (SQLException e) {
            System.err.println("Error: Failed to add Guitar to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // --- 2. ADD NEW PIANO ---

    public void addNewPiano(
        String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,
        String mateIns,
        String cateIns,
        String colorIns,
        boolean isElectric,
        String catePi,
        int keyNumPi,
        boolean hasPedal
        ) {
        try {
            // Sử dụng logic tạo ID phân tách
            String nextPianoId = generateNextProductId(PIANO_PREFIX, "piano");
            
            // 1. Tạo đối tượng Piano
            Piano newPiano = new Piano(
                nextPianoId, namePro, catePro, origin, brand, 
                quantityInStock, importDate, sellingPrice,
                cateIns, mateIns, colorIns, isElectric,
                catePi, keyNumPi, hasPedal);

            // 2. Lưu vào bảng Product
            insertProduct(newPiano);
            
            // 3. Lưu vào bảng Instrument
            insertInstrument(newPiano);

            // 4. Lưu chi tiết vào bảng PianoDetail
            String sqlDetail = String.format("INSERT INTO %s (product_id, catePi, keyNumPi, hasPedal) VALUES (?, ?, ?, ?)", PIANO_DETAIL_TABLE);
            try (Connection conn = getConnection(true);
                 PreparedStatement pstmt = conn.prepareStatement(sqlDetail)) {
                pstmt.setString(1, nextPianoId);
                pstmt.setString(2, catePi);
                pstmt.setInt(3, keyNumPi);
                pstmt.setBoolean(4, hasPedal);
                pstmt.executeUpdate();
            }

            System.out.println("Success: Added Piano " + nextPianoId + " to DB.");
        } catch (SQLException e) {
            System.err.println("Error: Failed to add Piano to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // --- 3. ADD NEW KEYBOARD ---
    
    public void addNewKeyboard(
        String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,
        String mateIns,
        String cateIns,
        String colorIns,
        boolean isElectric,
        String cateKey,
        int keyNumKey,
        boolean hasLCD
    ) {
        try {
            // Sử dụng logic tạo ID phân tách
            String nextKeyboardId = generateNextProductId(KEYBOARD_PREFIX, "keyboard");
            
            // 1. Tạo đối tượng Keyboard
            Keyboard newKeyboard = new Keyboard(
                nextKeyboardId, namePro, catePro, origin, brand, 
                quantityInStock, importDate, sellingPrice,
                cateIns, mateIns, colorIns, isElectric,
                cateKey, keyNumKey, hasLCD
            );

            // 2. Lưu vào bảng Product
            insertProduct(newKeyboard);
            
            // 3. Lưu vào bảng Instrument 
            insertInstrument(newKeyboard);

            // 4. Lưu chi tiết vào bảng KeyboardDetail
            String sqlDetail = String.format("INSERT INTO %s (product_id, cateKey, keyNumKey, hasLCD) VALUES (?, ?, ?, ?)", KEYBOARD_DETAIL_TABLE);
            try (Connection conn = getConnection(true);
                 PreparedStatement pstmt = conn.prepareStatement(sqlDetail)) {
                pstmt.setString(1, nextKeyboardId);
                pstmt.setString(2, cateKey);
                pstmt.setInt(3, keyNumKey); 
                pstmt.setBoolean(4, hasLCD);
                pstmt.executeUpdate();
            }

            System.out.println("Success: Added Keyboard " + nextKeyboardId + " to DB.");
        } catch (SQLException e) {
            System.err.println("Error: Failed to add Keyboard to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- 4. ADD NEW DRUMKIT ---

    public void addNewDrum(
        String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,
        String mateIns,
        String cateIns,
        String colorIns,
        boolean isElectric,
        int numOfDrumPieces,
        int numOfCymbals,
        String headMaterial,
        String shellMaterial
    ) {
        try {
            // Sử dụng logic tạo ID phân tách
            String nextDrumId = generateNextProductId(DRUM_PREFIX, "drumkit");
            
            // 1. Tạo đối tượng DrumKit
            DrumKit newDrumKit = new DrumKit(
                nextDrumId, namePro, catePro, origin, brand, 
                quantityInStock, importDate, sellingPrice,
                cateIns, mateIns, colorIns, isElectric,
                numOfDrumPieces, numOfCymbals, headMaterial, shellMaterial
            );

            // 2. Lưu vào bảng Product
            insertProduct(newDrumKit);
            
            // 3. Lưu vào bảng Instrument
            insertInstrument(newDrumKit);

            // 4. Lưu chi tiết vào bảng DrumKitDetail
            String sqlDetail = String.format("INSERT INTO %s (product_id, numOfDrumPieces, numOfCymbals, headMaterial, shellMaterial) VALUES (?, ?, ?, ?, ?)", DRUMKIT_DETAIL_TABLE);
            try (Connection conn = getConnection(true);
                 PreparedStatement pstmt = conn.prepareStatement(sqlDetail)) {
                pstmt.setString(1, nextDrumId);
                pstmt.setInt(2, numOfDrumPieces);
                pstmt.setInt(3, numOfCymbals);
                pstmt.setString(4, headMaterial);
                pstmt.setString(5, shellMaterial);
                pstmt.executeUpdate();
            }

            System.out.println("Success: Added DrumKit " + nextDrumId + " to DB.");
        } catch (SQLException e) {
            System.err.println("Error: Failed to add DrumKit to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // --- 5. ADD NEW ACCESSORY ---
    
    public void addNewAccessory(
        String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,
        String cateAcc,
        String mateAcc, 
        String colorAcc,
        String compatibleWith
    ) {
        try {
            // Sử dụng logic tạo ID phân tách
            String nextAccId = generateNextProductId(ACCESSORY_PREFIX, "accessory");
            
            // 1. Tạo đối tượng Accessory
            Accessory newAccessory = new Accessory( 
                nextAccId, namePro, catePro, origin, brand, 
                quantityInStock, importDate, sellingPrice,
                cateAcc, mateAcc, colorAcc, compatibleWith
            );

            // 2. Lưu vào bảng Product
            insertProduct(newAccessory);

            // 3. Lưu chi tiết vào bảng AccessoryDetail 
            String sqlDetail = String.format("INSERT INTO %s (product_id, cateAcc, mateAcc, colorAcc, compatibleWith) VALUES (?, ?, ?, ?, ?)", ACCESSORY_DETAIL_TABLE);
            try (Connection conn = getConnection(true);
                 PreparedStatement pstmt = conn.prepareStatement(sqlDetail)) {
                pstmt.setString(1, nextAccId);
                pstmt.setString(2, cateAcc);
                pstmt.setString(3, mateAcc); 
                pstmt.setString(4, colorAcc);
                pstmt.setString(5, compatibleWith); 
                pstmt.executeUpdate();
            }

            System.out.println("Success: Added Accessory " + nextAccId + " to DB.");
        } catch (SQLException e) {
            System.err.println("Error: Failed to add Accessory to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- PHƯƠNG THỨC TẢI DỮ LIỆU TỪ DB (getAllItems) - GIỮ NGUYÊN LOGIC CŨ ---
    public List<Product> getAllItems() {
        List<Product> inventory = new ArrayList<>();
        
        String sql = String.format(
            "SELECT p.*, " +
            "i.mateIns AS i_mateIns, i.colorIns AS i_colorIns, i.isElectric AS i_isElectric, LOWER(TRIM(i.cateIns)) AS i_cateIns_clean, i.cateIns AS i_cateIns_raw, " + 
            "g.cateGui, g.strNumGui, g.bodyShapeGui, " +
            "pi.catePi, pi.keyNumPi, pi.hasPedal, " +
            "k.cateKey, k.keyNumKey, k.hasLCD, " +
            "d.numOfDrumPieces, d.numOfCymbals, d.headMaterial, d.shellMaterial, " +
            "a.cateAcc, a.mateAcc AS a_mateAcc, a.colorAcc AS a_colorAcc, a.compatibleWith AS a_compatibleWith " + 
            "FROM %s p " +
            "LEFT JOIN %s i ON p.id = i.product_id " +
            "LEFT JOIN %s g ON i.product_id = g.product_id " +
            "LEFT JOIN %s pi ON i.product_id = pi.product_id " + 
            "LEFT JOIN %s k ON i.product_id = k.product_id " + 
            "LEFT JOIN %s d ON i.product_id = d.product_id " + 
            "LEFT JOIN %s a ON p.id = a.product_id ", 
            PRODUCT_TABLE, INSTRUMENT_TABLE, GUITAR_DETAIL_TABLE, PIANO_DETAIL_TABLE, 
            KEYBOARD_DETAIL_TABLE, DRUMKIT_DETAIL_TABLE, ACCESSORY_DETAIL_TABLE);
        
        try (Connection conn = getConnection(true);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Đọc thông tin chung Product
                String id = rs.getString("id");
                String namePro = rs.getString("namePro");
                String catePro = rs.getString("catePro");
                String origin = rs.getString("origin");
                String brand = rs.getString("brand");
                int quantityInStock = rs.getInt("quantityInStock");
                Date importDate = rs.getDate("importDate");
                double sellingPrice = rs.getDouble("sellingPrice");
                java.util.Date utilImportDate = (importDate != null) ? new java.util.Date(importDate.getTime()) : null;

                if ("Instrument".equals(catePro)) {
                    // Đọc thông tin chung Instrument
                    String mateIns = rs.getString("i_mateIns");
                    String colorIns = rs.getString("i_colorIns");
                    boolean isElectric = rs.getBoolean("i_isElectric");
                    String cateInsRaw = rs.getString("i_cateIns_raw"); 
                    String normalizedCateIns = rs.getString("i_cateIns_clean"); 
                    
                    if (mateIns == null) continue; 
                    
                    // Tái tạo đối tượng Instrument con
                    switch (normalizedCateIns) {
                        case "guitar":
                            inventory.add(new Guitar(
                                id, namePro, catePro, origin, brand, quantityInStock, utilImportDate, sellingPrice,
                                cateInsRaw, mateIns, colorIns, isElectric,
                                rs.getString("cateGui"), rs.getInt("strNumGui"), rs.getString("bodyShapeGui")
                            ));
                            break;
                        case "piano":
                            inventory.add(new Piano(
                                id, namePro, catePro, origin, brand, quantityInStock, utilImportDate, sellingPrice,
                                cateInsRaw, mateIns, colorIns, isElectric,
                                rs.getString("catePi"), rs.getInt("keyNumPi"), rs.getBoolean("hasPedal")
                            ));
                            break;
                        case "keyboard":
                            inventory.add(new Keyboard(
                                id, namePro, catePro, origin, brand, quantityInStock, utilImportDate, sellingPrice,
                                cateInsRaw, mateIns, colorIns, isElectric,
                                rs.getString("cateKey"), rs.getInt("keyNumKey"), rs.getBoolean("hasLCD")
                            ));
                            break;
                        case "drumkit":
                            inventory.add(new DrumKit(
                                id, namePro, catePro, origin, brand, quantityInStock, utilImportDate, sellingPrice,
                                cateInsRaw, mateIns, colorIns, isElectric,
                                rs.getInt("numOfDrumPieces"), rs.getInt("numOfCymbals"), rs.getString("headMaterial"), rs.getString("shellMaterial")
                            ));
                            break;
                    }
                } else if ("Accessory".equals(catePro)) {
                    // Tái tạo đối tượng Accessory
                    String cateAcc = rs.getString("cateAcc");
                    if (cateAcc == null) continue;
                    
                    inventory.add(new Accessory(
                        id, namePro, catePro, origin, brand, quantityInStock, utilImportDate, sellingPrice,
                        cateAcc, rs.getString("a_mateAcc"), rs.getString("a_colorAcc"), rs.getString("a_compatibleWith")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: Lỗi khi tải dữ liệu từ DB. Chi tiết SQL:");
            e.printStackTrace(); 
        } 
        return inventory;
    }
    
    // --- CÁC PHƯƠNG THỨC CRUD CÒN LẠI (GIỮ NGUYÊN) ---
    
    public Product findItem(String itemId) {
        // Tải lại toàn bộ để đảm bảo dữ liệu mới nhất
        List<Product> allItems = getAllItems();
        for (Product item : allItems) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }

    public boolean deleteItem(String itemId) {
        String sql = String.format("DELETE FROM %s WHERE id = ?", PRODUCT_TABLE);
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, itemId);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Thành công: Đã xóa sản phẩm " + itemId + ".");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: Lỗi khi xóa sản phẩm khỏi DB: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void updateStock(String itemId, int newStock) {
        String sql = String.format("UPDATE %s SET quantityInStock = quantityInStock + ? WHERE id = ?", PRODUCT_TABLE);
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newStock);
            pstmt.setString(2, itemId);
            
            pstmt.executeUpdate();
            System.out.println("Thành công: Đã cập nhật tồn kho cho " + itemId + ".");
        } catch (SQLException e) {
            System.err.println("Lỗi: Lỗi khi cập nhật tồn kho: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updatePrice(String itemId, double newPrice) {
        String sql = String.format("UPDATE %s SET sellingPrice = ? WHERE id = ?", PRODUCT_TABLE);
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, newPrice);
            pstmt.setString(2, itemId);
            
            pstmt.executeUpdate();
            System.out.println("Thành công: Đã cập nhật giá cho " + itemId + ".");
        } catch (SQLException e) {
            System.err.println("Lỗi: Lỗi khi cập nhật giá: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public double totalValue(){
        String sql = String.format("SELECT SUM(sellingPrice * quantityInStock) AS total FROM %s", PRODUCT_TABLE);
        try (Connection conn = getConnection(true);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: Lỗi khi tính tổng giá trị: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;        
    }
    
    public int getExistingProductCount() {
        String sql = String.format("SELECT COUNT(*) FROM %s", PRODUCT_TABLE);
        try (Connection conn = getConnection(true);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: Lỗi khi đếm sản phẩm trong DB: " + e.getMessage());
        }
        return 0;
    }
}