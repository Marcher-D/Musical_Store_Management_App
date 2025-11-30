package com.TamCa.store.service;

// Import the DAOs from the new package
import com.TamCa.store.dao.*;

import com.TamCa.store.model.*; 
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.sql.SQLException;

/**
 * InventoryManager: Service/Business Logic Layer.
 * Responsible for business logic and coordinating between DAOs.
 */
public class InventoryManager {

    // Declare DAO objects
    private final ProductDAO productDAO;
    private final AccountDAO accountDAO;

    private final CustomerManager customerManager; // MỚI
    private final EmployeeManager employeeManager; // MỚI

    public InventoryManager(){
        // Initialize DAOs
        this.productDAO = new ProductDAO();
        this.accountDAO = new AccountDAO();

        this.customerManager = new CustomerManager(); // KHỞI TẠO MỚI
        this.employeeManager = new EmployeeManager(); // KHỞI TẠO MỚI
    }

    /**
     * Checks login credentials by calling AccountDAO.
     */
    public String checkLogin(String username, String password) {
        return accountDAO.checkLogin(username, password);
    }
    
    // --- ADD PRODUCT FUNCTIONALITY (Uses ID generation logic and calls ProductDAO) ---

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
            // 1. Generate ID (using logic from ProductDAO)
            String nextGuiId = productDAO.generateNextProductId("111", "guitar");
            
            // 2. Create Guitar object
            Guitar newGuitar = new Guitar(
                       nextGuiId, namePro, catePro, origin, brand,
                       quantityInStock, importDate, sellingPrice,
                       cateIns, mateIns, colorIns, isElectric,
                       cateGui, numOfString, bodyShapeGui);

            // 3. Save to tables (call ProductDAO)
            productDAO.insertProduct(newGuitar);
            productDAO.insertInstrument(newGuitar);
            productDAO.insertGuitarDetail(nextGuiId, cateGui, numOfString, bodyShapeGui);

            System.out.println("Success: Added Guitar " + nextGuiId + " to DB.");
        } catch (SQLException e) {
            System.err.println("Error: Failed to add Guitar to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
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
            String nextPianoId = productDAO.generateNextProductId("112", "piano");
            
            Piano newPiano = new Piano(
                nextPianoId, namePro, catePro, origin, brand, 
                quantityInStock, importDate, sellingPrice,
                cateIns, mateIns, colorIns, isElectric,
                catePi, keyNumPi, hasPedal);

            productDAO.insertProduct(newPiano);
            productDAO.insertInstrument(newPiano);
            productDAO.insertPianoDetail(nextPianoId, catePi, keyNumPi, hasPedal);

            System.out.println("Success: Added Piano " + nextPianoId + " to DB.");
        } catch (SQLException e) {
            System.err.println("Error: Failed to add Piano to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
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
            String nextKeyboardId = productDAO.generateNextProductId("113", "keyboard");
            
            Keyboard newKeyboard = new Keyboard(
                nextKeyboardId, namePro, catePro, origin, brand, 
                quantityInStock, importDate, sellingPrice,
                cateIns, mateIns, colorIns, isElectric,
                cateKey, keyNumKey, hasLCD
            );

            productDAO.insertProduct(newKeyboard);
            productDAO.insertInstrument(newKeyboard);
            productDAO.insertKeyboardDetail(nextKeyboardId, cateKey, keyNumKey, hasLCD);

            System.out.println("Success: Added Keyboard " + nextKeyboardId + " to DB.");
        } catch (SQLException e) {
            System.err.println("Error: Failed to add Keyboard to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
            String nextDrumId = productDAO.generateNextProductId("114", "drumkit");
            
            DrumKit newDrumKit = new DrumKit(
                nextDrumId, namePro, catePro, origin, brand, 
                quantityInStock, importDate, sellingPrice,
                cateIns, mateIns, colorIns, isElectric,
                numOfDrumPieces, numOfCymbals, headMaterial, shellMaterial
            );

            productDAO.insertProduct(newDrumKit);
            productDAO.insertInstrument(newDrumKit);
            productDAO.insertDrumDetail(nextDrumId, numOfDrumPieces, numOfCymbals, headMaterial, shellMaterial);

            System.out.println("Success: Added DrumKit " + nextDrumId + " to DB.");
        } catch (SQLException e) {
            System.err.println("Error: Failed to add DrumKit to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void addNewAccessory(
        String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,
        String cateAcc,
        String mateAcc, 
        String colorAcc,
        String compatibleWith
    ) {
        try {
            String nextAccId = productDAO.generateNextProductId("115", "accessory");
            
            Accessory newAccessory = new Accessory( 
                nextAccId, namePro, catePro, origin, brand, 
                quantityInStock, importDate, sellingPrice,
                cateAcc, mateAcc, colorAcc, compatibleWith
            );

            productDAO.insertProduct(newAccessory);
            productDAO.insertAccessoryDetail(nextAccId, cateAcc, mateAcc, colorAcc, compatibleWith);

            System.out.println("Success: Added Accessory " + nextAccId + " to DB.");
        } catch (SQLException e) {
            System.err.println("Error: Failed to add Accessory to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- QUERY AND UPDATE FUNCTIONALITY (Delegated to ProductDAO) ---

    public List<Product> getAllItems() {
        return productDAO.getAllItems();
    }
    
    public Product findItem(String itemId) {
        return productDAO.findItem(itemId);
    }

    public boolean deleteItem(String itemId) {
        return productDAO.deleteItem(itemId);
    }

    public void updateStock(String itemId, int newStock) {
        // Business logic can be here (e.g., permission checks, quantity validation)
        productDAO.updateStock(itemId, newStock);
    }

    public void updatePrice(String itemId, double newPrice) {
        productDAO.updatePrice(itemId, newPrice);
    }

    public void updatePrice(String itemId, String newPriceStr){
        try {
            double parsedPrice = Double.parseDouble(newPriceStr);

            this.updatePrice(itemId, parsedPrice);
        } catch (NumberFormatException e){
            System.err.println("Error: Value entered is not a valid number");

            throw new IllegalArgumentException("Price must be a number");
        }
    }
    
    public double totalValue(){
        return productDAO.totalValue();
    }
    
    public int getExistingProductCount() {
        return productDAO.getExistingProductCount();
    }
    
    public Map<String, Double> getMonthlyImportStats() {
        return productDAO.getMonthlyImportStats();
    }

    // --- CUSTOMER DELEGATION ---
    public List<Customer> getAllCustomers() {
        return customerManager.getAllCustomers();
    }

    // Hàm này được Controller gọi để lấy ID tự động điền vào Form
    public String generateNextCustomerId() {
        return customerManager.generateNextCustomerId();
    }

    public boolean addNewCustomer(String csn, String name, String phone, String email, String address) {
        Customer newCus = new Customer(name, csn, phone, email, address);
        return customerManager.addNewCustomer(newCus);
    }

    // --- EMPLOYEE DELEGATION ---
    public List<Employee> getAllEmployees() {
        return employeeManager.getAllEmployees();
    }

    // Hàm này được Controller gọi
    public String generateNextEmployeeId() {
        return employeeManager.generateNextEmployeeId();
    }

    public boolean addNewEmployee(String eid, String name, String pos, int salary, Date hireDate) {
        Employee newEmp = new Employee(name, eid, pos, salary, hireDate);
        return employeeManager.addNewEmployee(newEmp);
    }
    
    public int getTotalSalary(){
        List<Employee> list = getAllEmployees();
        int total = 0;
        for (Employee e : list){
            total += e.getSalEmp();
        }
        return total;
    }
}