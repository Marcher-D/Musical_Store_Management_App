package com.TamCa.store.model;
import java.util.Date;

public class Piano extends Instrument {
    private String catePi;
    private int keyNumPi;
    private boolean hasPedal;

    public Piano(
        // 8 parameters of Product
        String id, String namePro, String catePro, String origin, 
        String brand, int quantityInStock, Date importDate, double sellingPrice,

        // 4 parameters of Instrument
        String cateIns,
        String mateIns,
        String colorIns,
        boolean isElectric,

        // 3 parameters of Piano
        String catePi,
        int keyNumPi,
        boolean hasPedal
        ) {
            super(id, namePro, catePro, origin, brand, quantityInStock, importDate, sellingPrice,
                  cateIns, mateIns, colorIns, isElectric);

            this.catePi = catePi;
            this.keyNumPi = keyNumPi;
            this.hasPedal = hasPedal;
        }

        public String getCatePi(){
            return this.catePi;
        }

        public int getNumOfKey(){
            return this.keyNumPi;
        }

        public boolean hasPedal(){
            return this.hasPedal;
        }

        public void setCatePi(String catePi){
            this.catePi = catePi;
        }

        public void setNumOfKey(int keyNumPi){
            this.keyNumPi = keyNumPi;
        }

        public void setHasPedal(boolean hasPedal){
            this.hasPedal = hasPedal;
        }

        @Override
        public String getDescription(){
            return "Piano ID: " + getId() +
                "\n Model: " + getNamePro() +
                "\n Brand: " + getBrand() +
                "\n Category: " + getCatePi() +
                "\n Price: " + getSellingPrice() +
                "\n Number of keys: " + getNumOfKey();
        }
    
}
