package com.example.assignment2_hrm;

public class Department {
    private int departmentId;
    private String departmentName;
    private int numberOfEmployees;
    private String managerId;
    private String departmentCode;

    // Default constructor
    public Department() {
        // Initialize with default values
        this.departmentId = 0;
        this.departmentName = "";
        this.departmentCode = "";
        this.numberOfEmployees = 0;
        this.managerId = "";
    }

    // Constructor with parameters
    public Department(int departmentId, String departmentName, String departmentCode, int numberOfEmployees, String managerId) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.numberOfEmployees = numberOfEmployees;
        this.managerId = managerId;
    }

    // Getters and Setters
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    @Override
    public String toString() {
        return departmentName;
    }
}
