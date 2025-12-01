package com.TamCa.store.dao; 

import com.TamCa.store.model.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.*;

/**
 * ProductDAO: Class giao tiếp với Database cho sản phẩm.
 * Đã Refactor để dùng DBConnection.
 */
public class ProductDAO {

    // Table names
    private static final String PRODUCT_TABLE = "Product";
    private static final String INSTRUMENT_TABLE = "Instrument"; 
    private static final String GUITAR_DETAIL_TABLE = "GuitarDetail";
    private static final String PIANO_DETAIL_TABLE = "PianoDetail";
    private static final String KEYBOARD_DETAIL_TABLE = "KeyboardDetail";
    private static final String DRUMKIT_DETAIL_TABLE = "DrumKitDetail";
    private static final String ACCESSORY_DETAIL_TABLE = "AccessoryDetail";
    
    // Counter for String ID
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
    
    // Common SQL JOIN command
    private static final String BASE_JOIN_SQL = String.format(
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

    public ProductDAO(){
        initializeIdCounters();
    }
    
    // --- DATABASE CONNECTION METHOD ---
    public Connection getConnection(boolean useDb) throws SQLException {
        // Tham số useDb hiện tại không còn cần thiết vì ta luôn kết nối vào DB chính
        // Giữ lại tham số để tránh lỗi compile ở các đoạn code cũ gọi hàm này
        return DBConnection.getConnection();
    }
    
    // --- ID COUNTER INITIALIZATION ---
    private void initializeIdCounters() {
        String sql = "SELECT MAX(id) FROM " + PRODUCT_TABLE + " WHERE id LIKE ?";
        
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Helper interface for functional programming style in loop
            java.util.function.BiConsumer<String, java.util.function.IntConsumer> getMaxCounter = (prefix, setter) -> {
                try {
                    pstmt.setString(1, prefix + "%");
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next() && rs.getString(1) != null) {
                            String maxId = rs.getString(1);
                            try {
                                int maxCount = Integer.parseInt(maxId.substring(3)) + 1;
                                setter.accept(maxCount);
                            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                                // Ignore bad ID formats
                            }
                        }
                    }
                } catch (SQLException e) {
                     System.err.println("Error initializing counter for prefix " + prefix + ": " + e.getMessage());
                }
            };
            
            getMaxCounter.accept(GUITAR_PREFIX, count -> this.guitarCounter = count);
            getMaxCounter.accept(PIANO_PREFIX, count -> this.pianoCounter = count);
            getMaxCounter.accept(KEYBOARD_PREFIX, count -> this.keyboardCounter = count);
            getMaxCounter.accept(DRUM_PREFIX, count -> this.drumCounter = count);
            getMaxCounter.accept(ACCESSORY_PREFIX, count -> this.accessoryCounter = count);
            
            System.out.println("Success: Initialized ID counters.");
        } catch (SQLException e) {
            System.err.println("Error initializing counters: " + e.getMessage());
        }
    }
    
    // --- COMMON METHODS: ID GENERATION AND DATE CONVERSION ---
    
    private java.sql.Date convertUtilToSqlDate(Date utilDate) {
        if (utilDate == null) return null;
        return new java.sql.Date(utilDate.getTime());
    }
    
    public String generateNextProductId(String prefix, String type) {
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
    
    // --- BASIC INSERT METHODS ---
    
    public void insertProduct(Product product) throws SQLException {
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
    
    public void insertInstrument(Instrument instrument) throws SQLException {
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
    
    public void insertGuitarDetail(String productId, String cateGui, int numOfString, String bodyShapeGui) throws SQLException {
        String sqlDetail = String.format("INSERT INTO %s (product_id, cateGui, strNumGui, bodyShapeGui) VALUES (?, ?, ?, ?)", GUITAR_DETAIL_TABLE);
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sqlDetail)) {
            pstmt.setString(1, productId);
            pstmt.setString(2, cateGui);
            pstmt.setInt(3, numOfString);
            pstmt.setString(4, bodyShapeGui);
            pstmt.executeUpdate();
        }
    }
    
    public void insertPianoDetail(String productId, String catePi, int keyNumPi, boolean hasPedal) throws SQLException {
        String sqlDetail = String.format("INSERT INTO %s (product_id, catePi, keyNumPi, hasPedal) VALUES (?, ?, ?, ?)", PIANO_DETAIL_TABLE);
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sqlDetail)) {
            pstmt.setString(1, productId);
            pstmt.setString(2, catePi);
            pstmt.setInt(3, keyNumPi);
            pstmt.setBoolean(4, hasPedal);
            pstmt.executeUpdate();
        }
    }
    
    public void insertKeyboardDetail(String productId, String cateKey, int keyNumKey, boolean hasLCD) throws SQLException {
        String sqlDetail = String.format("INSERT INTO %s (product_id, cateKey, keyNumKey, hasLCD) VALUES (?, ?, ?, ?)", KEYBOARD_DETAIL_TABLE);
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sqlDetail)) {
            pstmt.setString(1, productId);
            pstmt.setString(2, cateKey);
            pstmt.setInt(3, keyNumKey); 
            pstmt.setBoolean(4, hasLCD);
            pstmt.executeUpdate();
        }
    }
    
    public void insertDrumDetail(String productId, int numOfDrumPieces, int numOfCymbals, String headMaterial, String shellMaterial) throws SQLException {
        String sqlDetail = String.format("INSERT INTO %s (product_id, numOfDrumPieces, numOfCymbals, headMaterial, shellMaterial) VALUES (?, ?, ?, ?, ?)", DRUMKIT_DETAIL_TABLE);
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sqlDetail)) {
            pstmt.setString(1, productId);
            pstmt.setInt(2, numOfDrumPieces);
            pstmt.setInt(3, numOfCymbals);
            pstmt.setString(4, headMaterial);
            pstmt.setString(5, shellMaterial);
            pstmt.executeUpdate();
        }
    }
    
    public void insertAccessoryDetail(String productId, String cateAcc, String mateAcc, String colorAcc, String compatibleWith) throws SQLException {
        String sqlDetail = String.format("INSERT INTO %s (product_id, cateAcc, mateAcc, colorAcc, compatibleWith) VALUES (?, ?, ?, ?, ?)", ACCESSORY_DETAIL_TABLE);
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sqlDetail)) {
            pstmt.setString(1, productId);
            pstmt.setString(2, cateAcc);
            pstmt.setString(3, mateAcc); 
            pstmt.setString(4, colorAcc);
            pstmt.setString(5, compatibleWith); 
            pstmt.executeUpdate();
        }
    }

    // --- HELPER METHOD: BUILD PRODUCT OBJECT FROM RESULTSET ---
    private Product buildProductFromResultSet(ResultSet rs) throws SQLException {
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
            String mateIns = rs.getString("i_mateIns");
            String colorIns = rs.getString("i_colorIns");
            boolean isElectric = rs.getBoolean("i_isElectric");
            String cateInsRaw = rs.getString("i_cateIns_raw"); 
            String normalizedCateIns = rs.getString("i_cateIns_clean"); 
            
            if (mateIns == null) return null; 
            
            switch (normalizedCateIns) {
                case "guitar":
                    return new Guitar(
                        id, namePro, catePro, origin, brand, quantityInStock, utilImportDate, sellingPrice,
                        cateInsRaw, mateIns, colorIns, isElectric,
                        rs.getString("cateGui"), rs.getInt("strNumGui"), rs.getString("bodyShapeGui")
                    );
                case "piano":
                    return new Piano(
                        id, namePro, catePro, origin, brand, quantityInStock, utilImportDate, sellingPrice,
                        cateInsRaw, mateIns, colorIns, isElectric,
                        rs.getString("catePi"), rs.getInt("keyNumPi"), rs.getBoolean("hasPedal")
                    );
                case "keyboard":
                    return new Keyboard(
                        id, namePro, catePro, origin, brand, quantityInStock, utilImportDate, sellingPrice,
                        cateInsRaw, mateIns, colorIns, isElectric,
                        rs.getString("cateKey"), rs.getInt("keyNumKey"), rs.getBoolean("hasLCD")
                    );
                case "drumkit":
                    return new DrumKit(
                        id, namePro, catePro, origin, brand, quantityInStock, utilImportDate, sellingPrice,
                        cateInsRaw, mateIns, colorIns, isElectric,
                        rs.getInt("numOfDrumPieces"), rs.getInt("numOfCymbals"), rs.getString("headMaterial"), rs.getString("shellMaterial")
                    );
                default:
                    return null; 
            }
        } else if ("Accessory".equals(catePro)) {
            String cateAcc = rs.getString("cateAcc");
            if (cateAcc == null) return null;
            
            return new Accessory(
                id, namePro, catePro, origin, brand, quantityInStock, utilImportDate, sellingPrice,
                cateAcc, rs.getString("a_mateAcc"), rs.getString("a_colorAcc"), rs.getString("a_compatibleWith")
            );
        }
        return null; 
    }

    // --- DATA QUERY METHODS ---

    public List<Product> getAllItems() {
        List<Product> inventory = new ArrayList<>();
        
        try (Connection conn = getConnection(true);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(BASE_JOIN_SQL)) {

            while (rs.next()) {
                Product product = buildProductFromResultSet(rs);
                if (product != null) {
                    inventory.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: Failed to load data from DB. SQL details:");
            e.printStackTrace(); 
        } 
        return inventory;
    }
    
    public Product findItem(String itemId) {
        String sql = BASE_JOIN_SQL + " WHERE p.id = ?";
        
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, itemId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return buildProductFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: Failed to find product " + itemId + " from DB. SQL details:");
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteItem(String itemId) {
        String sql = String.format("DELETE FROM %s WHERE id = ?", PRODUCT_TABLE);
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, itemId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error: Failed to delete product from DB: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void updateStock(String itemId, int stockChange) {
        String sql = String.format("UPDATE %s SET quantityInStock = quantityInStock + ? WHERE id = ?", PRODUCT_TABLE);
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, stockChange);
            pstmt.setString(2, itemId);
            pstmt.executeUpdate();
            System.out.println("Success: Stock updated for " + itemId + ".");
        } catch (SQLException e) {
            System.err.println("Error: Failed to update stock: " + e.getMessage());
        }
    }

    public void updatePrice(String itemId, double newPrice) {
        String sql = String.format("UPDATE %s SET sellingPrice = ? WHERE id = ?", PRODUCT_TABLE);
        try (Connection conn = getConnection(true);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, newPrice);
            pstmt.setString(2, itemId);
            pstmt.executeUpdate();
            System.out.println("Success: Price updated for " + itemId + ".");
        } catch (SQLException e) {
            System.err.println("Error: Failed to update price: " + e.getMessage());
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
            System.err.println("Error: Failed to calculate total value: " + e.getMessage());
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
            System.err.println("Error: Failed to count products in DB: " + e.getMessage());
        }
        return 0;
    }

    public java.util.Map<String, Double> getMonthlyImportStats() {
        java.util.Map<String, Double> stats = new java.util.LinkedHashMap<>();
        
        String sql = "SELECT MONTHNAME(importDate) as month, SUM(sellingPrice * quantityInStock) as total " +
                     "FROM Product " +
                     "GROUP BY MONTH(importDate), MONTHNAME(importDate) " +
                     "ORDER BY MONTH(importDate)";
                     
        try (Connection conn = getConnection(true);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while(rs.next()) {
                String month = rs.getString("month"); 
                Double total = rs.getDouble("total"); 
                if (month != null && month.length() > 3) month = month.substring(0, 3);
                stats.put(month, total);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error while loading monthly statistics: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }
}