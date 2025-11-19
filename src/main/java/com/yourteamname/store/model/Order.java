package com.yourteamname.store.model;

import java.util.Date;

public class Order {
    private String status, deliAdd;
    private Date sellDate, deliDate;

    public Order(String status, String deliAdd, Date sellDate, Date deliDate){
        this.status = status;
        this.deliAdd = deliAdd;
        this.sellDate = sellDate;
        this.deliDate = deliDate;
    }

    final public String getStatus(){
        return this.status;
    }

    final public String getDeliAdd(){
        return this.deliAdd;
    }

    final public Date getSellDate(){
        return this.sellDate;
    }

    final public Date getDeliDate(){
        return this.deliDate;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setDeliAdd(String deliAdd){
        this.deliAdd = deliAdd;
    }

    public void setSellDate(Date sellDate){
        this.sellDate = sellDate;
    }

    public void setDeliDate(Date deliDate){
        this.deliDate = deliDate;
    }

    public String getDescription(){
    return "Status: " + getStatus() + 
        "\nSelling Date: " + getSellDate() +
        "\nDelivering Date: " + getDeliDate() +
        "\nDelivering Address: " + getDeliAdd();
    }
}
