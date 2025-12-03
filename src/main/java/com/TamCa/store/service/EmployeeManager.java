package com.TamCa.store.service;

import com.TamCa.store.dao.EmployeeDAO;
// import com.TamCa.store.model.Customer;
import java.util.Date;
import com.TamCa.store.model.Employee;
import java.util.List;

// act as a service layer for employee
public class EmployeeManager {
    
    private final EmployeeDAO employeeDAO;

    public EmployeeManager() {
        this.employeeDAO = new EmployeeDAO();
    }
    
    // load the employees from DB
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }
    
    // add new Employee
    public boolean addNewEmployee(Employee employee) {
        if (employee.getSalEmp() < 1000) { // the salary cannot below 1000 ^_^
            System.err.println("Business Rule Violated: Salary cannot be less than 1000.");
            return false;
        }
        if (employeeDAO.getEmployeeByEID(employee.getEID()) != null) {
             System.err.println("Error: Employee ID " + employee.getEID() + " already exists.");
             return false;
        }
        return employeeDAO.addEmployee(employee);
    }

    public String generateNextEmployeeId() {
        List<Employee> list = getAllEmployees();
        if (list.isEmpty()) return "E001";

        int maxId = 0;
        for (Employee emp : list) {
            try {
                String numPart = emp.getEID().replaceAll("\\D+", "");
                if (!numPart.isEmpty()) {
                    int id = Integer.parseInt(numPart);
                    if (id > maxId) maxId = id;
                }
            } catch (Exception e) { continue; }
        }
        return String.format("E%03d", maxId + 1);
    }
    
    // update Employee's in4
    public boolean updateEmployee(Employee employee) {
        return employeeDAO.updateEmployee(employee);
    }
    
    // delete by EID
    public boolean deleteEmployee(String EID) {
        return employeeDAO.deleteEmployee(EID);
    }

    public boolean addNewEmployee(String eid, String name, String pos, int salary, Date hireDate) {
        Employee newEmp = new Employee(name, eid, pos, salary, hireDate);
        return this.addNewEmployee(newEmp);
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
