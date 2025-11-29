package com.TamCa.store.model;

import java.util.Date;

public class Employee {
    private String nameEmp, EID, posEmp;
    private int salEmp;
    private Date hireDate; 

    public Employee(String nameEmp, String EID, String posEmp, int salEmp, Date hireDate){
        this.nameEmp = nameEmp;
        this.EID = EID;
        this.posEmp = posEmp;
        this.salEmp = salEmp;
        this.hireDate = hireDate;
    }

    final public String getNameEmp(){
        return this.nameEmp;
    }

    final public String getEID(){
        return this.EID;
    }

    final public String getPosEmp(){
        return this.posEmp;
    }

    final public int getSalEmp(){
        return this.salEmp;
    }

    final public Date getHireDate(){
        return this.hireDate;
    }

    public void setNameEmp(String nameEmp){
        this.nameEmp = nameEmp;
    }

    public void setEID(String EID){
        this.EID = EID;
    }

    public void setPosEmp(String posEmp){
        this.posEmp = posEmp; 
    }

    public void setSalEmp(int salEmp){
        this.salEmp = salEmp;
    }

    public void setHireDate(Date hireDate){
        this.hireDate = hireDate;
    }

    public String getDescription(){
    return "Employee ID: " + getEID() +
    "\nName: " + getNameEmp() + 
    "\n Position: " + getPosEmp() + 
    "\n Salary: " + getSalEmp() + 
    "\n Hire Date: " + getHireDate();
    }
}
