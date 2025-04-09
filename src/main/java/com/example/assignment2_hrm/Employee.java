package com.example.assignment2_hrm;

import javafx.beans.property.*;
import java.io.*;
import java.util.Date;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    // Transient JavaFX Properties
    private transient StringProperty employeeId;
    private transient StringProperty firstName;
    private transient StringProperty lastName;
    private transient StringProperty address;
    private transient StringProperty city;
    private transient StringProperty postcode;
    private transient StringProperty province;
    private transient StringProperty country;
    private transient StringProperty password;
    private transient StringProperty phone;
    private transient StringProperty email;
    private transient BooleanProperty isActive;
    private transient DoubleProperty hourlyRate;
    private transient ObjectProperty<Date> startDate;
    private transient ObjectProperty<Date> endDate;
    private transient IntegerProperty departmentId;
    private transient StringProperty role; // New Role property

    // Default constructor
    public Employee() {
        this.employeeId = new SimpleStringProperty("");
        this.firstName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.city = new SimpleStringProperty("");
        this.province = new SimpleStringProperty("");
        this.postcode = new SimpleStringProperty("");
        this.country = new SimpleStringProperty("");
        this.password = new SimpleStringProperty("");
        this.phone = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.isActive = new SimpleBooleanProperty(false);
        this.hourlyRate = new SimpleDoubleProperty(0.0);
        this.startDate = new SimpleObjectProperty<>(null);
        this.endDate = new SimpleObjectProperty<>(null);
        this.departmentId = new SimpleIntegerProperty(0);
        this.role = new SimpleStringProperty("");
    }

    // Constructor
    public Employee(String employeeId, String firstName, String lastName, String address, String city,
                    String province, String postcode, String country, String password, String phone, String email,
                    Boolean isActive, Double hourlyRate, Date startDate, Date endDate, Integer departmentId, String role) {
        this.employeeId = new SimpleStringProperty(employeeId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.address = new SimpleStringProperty(address);
        this.city = new SimpleStringProperty(city);
        this.province = new SimpleStringProperty(province);
        this.postcode = new SimpleStringProperty(postcode);
        this.country = new SimpleStringProperty(country);
        this.password = new SimpleStringProperty(password);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.isActive = new SimpleBooleanProperty(isActive);
        this.hourlyRate = new SimpleDoubleProperty(hourlyRate);
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty<>(endDate);
        this.departmentId = new SimpleIntegerProperty(departmentId);
        this.role = new SimpleStringProperty(role);
    }

    // Getters for JavaFX properties
    public StringProperty employeeIdProperty() {
        return employeeId;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty cityProperty() {
        return city;
    }

    public StringProperty provinceProperty() {
        return province;
    }

    public StringProperty postcodeProperty() {
        return postcode;
    }

    public StringProperty countryProperty() {
        return country;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public BooleanProperty isActiveProperty() {
        return isActive;
    }

    public DoubleProperty hourlyRateProperty() {
        return hourlyRate;
    }

    public ObjectProperty<Date> startDateProperty() {
        return startDate;
    }

    public ObjectProperty<Date> endDateProperty() {
        return endDate;
    }

    public IntegerProperty departmentIdProperty() {
        return departmentId;
    }

    public StringProperty roleProperty() {
        return role;
    }

    // GETTERS
    public String getEmployeeId() {
        return employeeId.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getCity() {
        return city.get();
    }

    public String getProvince() {
        return province.get();
    }

    public String getPostcode() {
        return postcode.get();
    }

    public String getCountry() {
        return country.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public String getEmail() {
        return email.get();
    }

    public boolean getIsActive() {
        return isActive.get();
    }

    public double getHourlyRate() {
        return hourlyRate.get();
    }

    public Date getStartDate() {
        return startDate.get();
    }

    public Date getEndDate() {
        return endDate.get();
    }

    public int getDepartmentId() {
        return departmentId.get();
    }

    public String getRole() {
        return role.get();
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId.set(employeeId);
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setProvince(String province) {
        this.province.set(province);
    }

    public void setPostcode(String postcode) {
        this.postcode.set(postcode);
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate.set(hourlyRate);
    }

    public void setStartDate(Date startDate) {
        this.startDate.set(startDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate.set(endDate);
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId.set(departmentId);
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    // Serialization and Deserialization methods
    private void writeObject(ObjectOutputStream out) throws IOException {
        // Serialize the underlying values
        out.defaultWriteObject();
        out.writeObject(employeeId.get());
        out.writeObject(firstName.get());
        out.writeObject(lastName.get());
        out.writeObject(address.get());
        out.writeObject(city.get());
        out.writeObject(province.get());
        out.writeObject(postcode.get());
        out.writeObject(country.get());
        out.writeObject(password.get());
        out.writeObject(phone.get());
        out.writeObject(email.get());
        out.writeObject(isActive.get());
        out.writeObject(hourlyRate.get());
        out.writeObject(startDate.get());
        out.writeObject(endDate.get());
        out.writeObject(departmentId.get());
        out.writeObject(role.get());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Deserialize the underlying values and reconstruct JavaFX properties
        in.defaultReadObject();
        this.employeeId = new SimpleStringProperty((String) in.readObject());
        this.firstName = new SimpleStringProperty((String) in.readObject());
        this.lastName = new SimpleStringProperty((String) in.readObject());
        this.address = new SimpleStringProperty((String) in.readObject());
        this.city = new SimpleStringProperty((String) in.readObject());
        this.province = new SimpleStringProperty((String) in.readObject());
        this.postcode = new SimpleStringProperty((String) in.readObject());
        this.country = new SimpleStringProperty((String) in.readObject());
        this.password = new SimpleStringProperty((String) in.readObject());
        this.phone = new SimpleStringProperty((String) in.readObject());
        this.email = new SimpleStringProperty((String) in.readObject());
        this.isActive = new SimpleBooleanProperty((Boolean) in.readObject());
        this.hourlyRate = new SimpleDoubleProperty((Double) in.readObject());
        this.startDate = new SimpleObjectProperty<>((Date) in.readObject());
        this.endDate = new SimpleObjectProperty<>((Date) in.readObject());
        this.departmentId = new SimpleIntegerProperty((Integer) in.readObject());
        this.role = new SimpleStringProperty((String) in.readObject());
    }
}
