package com.example.assignment2_hrm;

import javafx.beans.property.*;

public class Payroll {

    private StringProperty employeeId;
    private StringProperty payPeriod;
    private DoubleProperty grossSalary;
    private DoubleProperty deductions;
    private DoubleProperty netSalary;
    private StringProperty paymentMethod;
    private StringProperty paymentStatus;

    // Constructor
    public Payroll(String employeeId, String payPeriod, double grossSalary, double deductions, String paymentMethod, String paymentStatus) {
        this.employeeId = new SimpleStringProperty(employeeId);
        this.payPeriod = new SimpleStringProperty(payPeriod);
        this.grossSalary = new SimpleDoubleProperty(grossSalary);
        this.deductions = new SimpleDoubleProperty(deductions);
        this.netSalary = new SimpleDoubleProperty(grossSalary - deductions);
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
        this.paymentStatus = new SimpleStringProperty(paymentStatus);
    }

    // Getters and Setters for each property
    public String getEmployeeId() {
        return employeeId.get();
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId.set(employeeId);
    }

    public StringProperty employeeIdProperty() {
        return employeeId;
    }

    public String getPayPeriod() {
        return payPeriod.get();
    }

    public void setPayPeriod(String payPeriod) {
        this.payPeriod.set(payPeriod);
    }

    public StringProperty payPeriodProperty() {
        return payPeriod;
    }

    public double getGrossSalary() {
        return grossSalary.get();
    }

    public void setGrossSalary(double grossSalary) {
        this.grossSalary.set(grossSalary);
        updateNetSalary();
    }

    public DoubleProperty grossSalaryProperty() {
        return grossSalary;
    }

    public double getDeductions() {
        return deductions.get();
    }

    public void setDeductions(double deductions) {
        this.deductions.set(deductions);
        updateNetSalary();
    }

    public DoubleProperty deductionsProperty() {
        return deductions;
    }

    public double getNetSalary() {
        return netSalary.get();
    }

    private void updateNetSalary() {
        this.netSalary.set(this.grossSalary.get() - this.deductions.get());
    }

    public DoubleProperty netSalaryProperty() {
        return netSalary;
    }

    public String getPaymentMethod() {
        return paymentMethod.get();
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod.set(paymentMethod);
    }

    public StringProperty paymentMethodProperty() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus.get();
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus.set(paymentStatus);
    }

    public StringProperty paymentStatusProperty() {
        return paymentStatus;
    }

    @Override
    public String toString() {
        return "Payroll{" +
                "employeeId=" + getEmployeeId() +
                ", payPeriod=" + getPayPeriod() +
                ", grossSalary=" + getGrossSalary() +
                ", deductions=" + getDeductions() +
                ", netSalary=" + getNetSalary() +
                ", paymentMethod=" + getPaymentMethod() +
                ", paymentStatus=" + getPaymentStatus() +
                '}';
    }
}
