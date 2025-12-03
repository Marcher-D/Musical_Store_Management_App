package com.TamCa.store.model;

public class Customer {
    private String nameCus, CSN, phoneNum, emailCus, addCus;

    public Customer(String nameCus, String CSN, String phoneNum, String emailCus, String addCus){
        this.nameCus = nameCus;
        this.CSN = CSN;
        this.phoneNum = phoneNum;
        this.emailCus = emailCus;
        this.addCus = addCus;
    }

    // getter and setter
    final public String getNameCus(){
        return this.nameCus;
    }

    final public String getCSN(){
        return this.CSN;
    }

    final public String getPhoneNum(){
        return this.phoneNum;
    }

    final public String getEmailCus(){
        return this.emailCus;
    }

    final public String getAddCus(){
        return this.addCus;
    }

    public void setNameCus(String nameCus){
        this.nameCus = nameCus;
    }

    public void setCSN(String CSN){
        this.CSN = CSN;
    }

    public void setPhoneNum(String phoneNum){
        this.phoneNum = phoneNum;
    }

    public void setEmailCus(String emailCus){
        this.emailCus = emailCus;
    }

    public void setAddCus(String addCus){
        this.addCus = addCus;
    }
    
    public String getDescription(){
    return "Name of Customer: " + getNameCus() + 
        "\nCSN: " + getCSN() +
        "\nPhone number: " + getPhoneNum() +
        "\nEmail: " + getEmailCus() +
        "\nAddress: " + getAddCus();
    }
}
