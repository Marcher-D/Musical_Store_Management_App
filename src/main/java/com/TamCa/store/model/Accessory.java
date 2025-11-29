package com.TamCa.store.model;

import java.util.Date;

public class Accessory extends Product {
    // ĐÃ SỬA LỖI CHÍNH TẢ ĐỂ KHỚP VỚI SCHEMA SQL MỚI
    private String cateAcc, mateAcc, colorAcc, compatibleWith;

    public Accessory(
        // 8 arguments for Product to go into super()
        String id, String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,
        
        String cateAcc,
        String mateAcc,
        String colorAcc,
        String compatibleWith // ĐÃ SỬA TÊN BIẾN
    ) {
        super(id, namePro, catePro, origin, brand, quantityInStock, importDate, sellingPrice);

        this.cateAcc = cateAcc;
        this.mateAcc = mateAcc;
        this.colorAcc = colorAcc;
        this.compatibleWith = compatibleWith;
    }

    // --- GETTERS & SETTERS (Các phương thức đã thêm trước đó) ---
    
    final public String getCateAcc() { return this.cateAcc; }
    final public String getMateAcc() { return this.mateAcc; }
    final public String getColorAcc() { return this.colorAcc; }
    final public String getCompatibleWith() { return this.compatibleWith; } // ĐÃ SỬA getter
    
    public void setCateAcc(String cateAcc) { this.cateAcc = cateAcc; }
    public void setMateAcc(String mateAcc) { this.mateAcc = mateAcc; }
    public void setColorAcc(String colorAcc) { this.colorAcc = colorAcc; }
    public void setCompatibleWith(String compatibleWith) { this.compatibleWith = compatibleWith; } // ĐÃ SỬA setter
    
    // --- GHI ĐÈ PHƯƠNG THỨC getDescription() ---
    
    @Override
    public String getDescription(){
        // Gọi getDescription() của lớp cha (Product) để lấy thông tin chung (Tùy chọn)
        // String productInfo = super.getDescription(); 
        
        // Hoặc chỉ hiển thị thông tin chính như yêu cầu của bạn (dựa trên Piano format)
        return "Accessory ID: " + getId() +
               "\n Product Name: " + getNamePro() +
               "\n Brand: " + getBrand() +
               "\n Category: " + getCateAcc() + // Sử dụng cateAcc thay vì catePi
               "\n Material: " + getMateAcc() +
               "\n Color: " + getColorAcc() +
               "\n Compatible With: " + getCompatibleWith() + // Sử dụng getter đã sửa
               "\n Selling Price: " + getSellingPrice() +
               "\n Quantity in Stock: " + getQuantityInStock();
    }
}