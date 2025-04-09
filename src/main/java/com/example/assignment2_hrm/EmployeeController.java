package com.example.assignment2_hrm;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller class for Employee Management
 */
public class EmployeeController {
    private Db db = new Db();
    private Employee selectedEmployee;
    private String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> idColumn;
    @FXML
    private TableColumn<Employee, String> fullNameColumn;
    @FXML
    private TableColumn<Employee, String> emailColumn;
    @FXML
    private TableColumn<Employee, String> phoneColumn;
    @FXML
    private TableColumn<Employee, String> cityColumn;

    @FXML
    private Pane employeeTablePane;
    @FXML
    private Pane employeeUpdateFormPane;
    @FXML
    private Pane employeeAddFormPane;

   // ADD PANE FIELDS
    @FXML
    private TextField add_emailField;
    @FXML
    private TextField add_phoneField;
    @FXML
    private TextField add_addressField;
    @FXML
    private TextField add_hourlyRateField;
    @FXML
    private TextField add_cityField;
    @FXML
    private Label add_employeeIdField;
    @FXML
    private ComboBox<String> add_roleField;
    @FXML
    private TextField add_countryField;
    @FXML
    private CheckBox add_userActiveField;
    @FXML
    private DatePicker add_startDateField;
    @FXML
    private DatePicker add_endDateField;
    @FXML
    private ComboBox<Department> add_departmentField;
    @FXML
    private TextField add_firstNameField;
    @FXML
    private TextField add_lastNameField;
    @FXML
    private TextField add_passwordField;
    @FXML
    private TextField add_postalField;
    @FXML
    private ComboBox<String>  add_provinceField;

    // UPDATE PANE FIELDS
    @FXML
    private TextField update_emailField;
    @FXML
    private TextField update_phoneField;
    @FXML
    private TextField update_addressField;
    @FXML
    private TextField update_hourlyRateField;
    @FXML
    private TextField update_cityField;
    @FXML
    private Label update_employeeIdField;
    @FXML
    private ComboBox<String> update_roleField;
    @FXML
    private TextField update_countryField;
    @FXML
    private CheckBox update_userActiveField;
    @FXML
    private DatePicker update_startDateField;
    @FXML
    private DatePicker update_endDateField;
    @FXML
    private ComboBox<Department> update_departmentField;
    @FXML
    private TextField update_firstNameField;
    @FXML
    private TextField update_lastNameField;
    @FXML
    private TextField update_passwordField;
    @FXML
    private TextField update_postalField;
    @FXML
    private ComboBox<String>  update_provinceField;

    @FXML
    private Button addEmployeeButton;
    @FXML
    private Button updateEmployeeButton;
    @FXML
    private Button deleteEmployeeButton;
    @FXML
    private Button showAddEmployeePaneButton;
    @FXML
    private Button showUpdateEmployeePaneButton;
    @FXML
    private Button departmentButton;
    @FXML
    private Button payrollButton;
    @FXML
    private Button logoutButton;

    @FXML
    private TextField searchField;

    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    private ComboBox<Department> cbDepartment;

    @FXML
    public void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        fullNameColumn.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue();
            String fullName = employee.getFirstName() + " " + employee.getLastName();
            return new SimpleStringProperty(fullName);
        });
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));

        // Initialize role comboboxes
        List<String> roles = Arrays.asList("Manager", "Developer", "HR", "Accountant", "Sales");
        add_roleField.setItems(FXCollections.observableArrayList(roles));
        update_roleField.setItems(FXCollections.observableArrayList(roles));

        // Initialize province comboboxes
        List<String> provinces = Arrays.asList("AB", "BC", "MB", "NB", "NL", "NS", "ON", "PE", "QC", "SK");
        add_provinceField.setItems(FXCollections.observableArrayList(provinces));
        update_provinceField.setItems(FXCollections.observableArrayList(provinces));

        // Load departments into comboboxes
        refreshDepartmentComboBoxes();

        // Load initial data
        refreshEmployeeTable();

        // Add listener for table selection
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedEmployee = newSelection;
                populateUpdateForm(newSelection);
            }
        });

        // Get existing employees
        loadEmployeesFromDB();
        employeeTable.setItems(employeeList);
        initializeAddEmployeeFields();
        loadDepartmentsForComboBox();

        // Bind TableColumn to Employee properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        fullNameColumn.setCellValueFactory(cellData -> {
            String fullName = cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName();
            return new SimpleStringProperty(fullName);
        });
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));

        showTableEmployeePane();
        employeeTablePane.setVisible(true);
        employeeAddFormPane.setVisible(false);
        employeeUpdateFormPane.setVisible(false);
        showAddEmployeePaneButton.setOnAction(event -> showAddEmployeePane());
        addEmployeeButton.setOnAction(event -> handleAddEmployee());
        showUpdateEmployeePaneButton.setOnAction(event -> showUpdateEmployeePane());
        updateEmployeeButton.setOnAction(event -> updateEmployee());
        deleteEmployeeButton.setOnAction(event -> deleteEmployee());
        logoutButton.setOnAction(event -> logoutUser());
        payrollButton.setOnAction(event -> showPayrollView());
        departmentButton.setOnAction(event -> showDepartmentView());

        // Add a listener to the search field to trigger filtering
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterEmployeeData(newValue);  // Filter employees based on the search query
        });
    }

    private void refreshDepartmentComboBoxes() {
        List<Department> departments = db.getAllDepartments();
        add_departmentField.setItems(FXCollections.observableArrayList(departments));
        update_departmentField.setItems(FXCollections.observableArrayList(departments));
    }

    private void refreshEmployeeTable() {
        List<Employee> employees = db.getAlleEmployees();
        employeeTable.setItems(FXCollections.observableArrayList(employees));
    }

    private boolean validateEmployeeInput(TextField firstName, TextField lastName, TextField email, 
                                       TextField phone, TextField hourlyRate, ComboBox<Department> department) {
        StringBuilder errorMessage = new StringBuilder();
        
        if (firstName.getText().trim().isEmpty()) errorMessage.append("First name is required\n");
        if (lastName.getText().trim().isEmpty()) errorMessage.append("Last name is required\n");
        if (email.getText().trim().isEmpty() || !email.getText().contains("@")) 
            errorMessage.append("Valid email is required\n");
        if (phone.getText().trim().isEmpty()) errorMessage.append("Phone number is required\n");
        
        try {
            if (!hourlyRate.getText().trim().isEmpty()) {
                double rate = Double.parseDouble(hourlyRate.getText());
                if (rate < 0) errorMessage.append("Hourly rate must be positive\n");
            } else {
                errorMessage.append("Hourly rate is required\n");
            }
        } catch (NumberFormatException e) {
            errorMessage.append("Hourly rate must be a valid number\n");
        }
        
        if (department.getValue() == null) errorMessage.append("Department must be selected\n");

        if (errorMessage.length() > 0) {
            showAlert(AlertType.ERROR, "Validation Error", errorMessage.toString());
            return false;
        }
        return true;
    }

    @FXML
    private void handleAddEmployee() {
        if (!validateEmployeeInput(add_firstNameField, add_lastNameField, add_emailField, 
                                 add_phoneField, add_hourlyRateField, add_departmentField)) {
            return;
        }

        try {
            Employee newEmployee = new Employee();
            newEmployee.setEmployeeId(generateEmployeeId());
            newEmployee.setFirstName(add_firstNameField.getText());
            newEmployee.setLastName(add_lastNameField.getText());
            newEmployee.setEmail(add_emailField.getText());
            newEmployee.setPhone(add_phoneField.getText());
            newEmployee.setAddress(add_addressField.getText());
            newEmployee.setCity(add_cityField.getText());
            newEmployee.setProvince(add_provinceField.getValue());
            newEmployee.setPostcode(add_postalField.getText());
            newEmployee.setCountry(add_countryField.getText());
            newEmployee.setPassword(add_passwordField.getText());
            newEmployee.setIsActive(add_userActiveField.isSelected());
            newEmployee.setHourlyRate(Double.parseDouble(add_hourlyRateField.getText()));
            newEmployee.setRole(add_roleField.getValue());
            newEmployee.setDepartmentId(add_departmentField.getValue().getDepartmentId());

            // Convert LocalDate to java.util.Date for database storage
            if (add_startDateField.getValue() != null) {
                newEmployee.setStartDate(java.util.Date.from(
                    add_startDateField.getValue().atStartOfDay()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toInstant()));
            }
            if (add_endDateField.getValue() != null) {
                newEmployee.setEndDate(java.util.Date.from(
                    add_endDateField.getValue().atStartOfDay()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toInstant()));
            }

            db.insertEmployee(newEmployee);
            refreshEmployeeTable();
            clearAddForm();
            showAlert(AlertType.INFORMATION, "Success", "Employee added successfully!");
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Failed to add employee: " + e.getMessage());
        }
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearAddForm() {
        add_firstNameField.clear();
        add_lastNameField.clear();
        add_emailField.clear();
        add_phoneField.clear();
        add_addressField.clear();
        add_cityField.clear();
        add_provinceField.setValue(null);
        add_postalField.clear();
        add_countryField.clear();
        add_passwordField.clear();
        add_hourlyRateField.clear();
        add_roleField.setValue(null);
        add_departmentField.setValue(null);
        add_startDateField.setValue(null);
        add_endDateField.setValue(null);
        add_userActiveField.setSelected(false);
    }

    @FXML
    private void generateRandomPassword() {
        // Use StringBuilder for building the password
        StringBuilder password = new StringBuilder();

        // Generate a 6-character password
        for (int i = 0; i < 6; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            password.append(characters.charAt(randomIndex));
        }

        // Set the generated password to the password field
        add_passwordField.setText(password.toString());
    }

    @FXML
    private void resetRandomPassword() {
        // Use StringBuilder for building the password
        StringBuilder password = new StringBuilder();

        // Generate a 6-character password
        for (int i = 0; i < 6; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            password.append(characters.charAt(randomIndex));
        }

        // Set the generated password to the password field
        update_passwordField.setText(password.toString());
    }

    @FXML
    private void backToTableFromUpdate() {
        showTableEmployeePane();
    }

    @FXML
    private void backToTableFromAdd() {
        showTableEmployeePane();
    }

    @FXML
    private void  addEmployee() {
        Employee newEmployee = new Employee(
                db.generateEmployeeId(),
                add_firstNameField.getText(),
                add_lastNameField.getText(),
                add_addressField.getText(),
                add_cityField.getText(),
                add_provinceField.getValue(),
                add_postalField.getText(),
                add_countryField.getText(),
                add_passwordField.getText(),
                add_phoneField.getText(),
                add_emailField.getText(),
                add_userActiveField.isSelected(),
                parseDoubleOrDefault(add_hourlyRateField.getText(), 0.0),
                add_startDateField.getValue() != null ? java.sql.Date.valueOf(add_startDateField.getValue()) : null,
                add_endDateField.getValue() != null ? java.sql.Date.valueOf(add_endDateField.getValue()) : null,
                add_departmentField.getValue().getDepartmentId(),
                add_roleField.getValue()
        );

        if (validateEmployeeData(newEmployee)) {
            db.insertEmployee(newEmployee);
            loadEmployeesFromDB();
            employeeTable.setItems(employeeList);
            showTableEmployeePane();
        }
    }

    @FXML
    private void updateEmployee() {
        if (selectedEmployee == null) {
            App.showError("No employee selected for update.");
            return;
        }
        // Update the selectedEmployee object with values from the UI
        selectedEmployee.setFirstName(update_firstNameField.getText());
        selectedEmployee.setLastName(update_lastNameField.getText());
        selectedEmployee.setAddress(update_addressField.getText());
        selectedEmployee.setCity(update_cityField.getText());
        selectedEmployee.setProvince(update_provinceField.getValue());
        selectedEmployee.setPostcode(update_postalField.getText());
        selectedEmployee.setCountry(update_countryField.getText());
        selectedEmployee.setPassword(update_passwordField.getText());
        selectedEmployee.setPhone(update_phoneField.getText());
        selectedEmployee.setIsActive(update_userActiveField.isSelected());
        selectedEmployee.setHourlyRate(parseDoubleOrDefault(update_hourlyRateField.getText(), 0.0));
        selectedEmployee.setEndDate(update_endDateField.getValue() != null ? java.sql.Date.valueOf(update_endDateField.getValue()) : null);
        selectedEmployee.setDepartmentId(update_departmentField.getValue().getDepartmentId());
        selectedEmployee.setRole(update_roleField.getValue());

        if (validateEmployeeData(selectedEmployee)) {
            db.updateEmployee(selectedEmployee);
            loadEmployeesFromDB();
            employeeTable.setItems(employeeList);
            showTableEmployeePane();
        }
    }

    private void deleteEmployee() {
        selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            App.showError("No employee selected for deletion.");
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this employee?");
        confirmationAlert.setContentText("Employee: " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName());

        // Show the alert and wait for user's response
        ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

        // Check if the user clicked the "OK" button
        if (result == ButtonType.OK) {
            // Proceed with deletion if confirmed
            db.deleteEmployee(selectedEmployee.getEmployeeId());
            loadEmployeesFromDB();
            employeeTable.setItems(employeeList);
            App.showInfo("Employee deleted successfully.");
        } else {
            // If canceled, show cancellation message
            App.showInfo("Employee deletion canceled.");
        }
    }

    private boolean validateEmployeeData(Employee employee) {
        // Validate First Name
        if (employee.getFirstName() == null || employee.getFirstName().isEmpty() ||
                !employee.getFirstName().matches("[A-Za-z]+")) {
            App.showError("First Name must contain only letters.");
            return false;
        }

        // Validate Last Name
        if (employee.getLastName() == null || employee.getLastName().isEmpty() ||
                !employee.getLastName().matches("[A-Za-z]+")) {
            App.showError("Last Name must contain only letters.");
            return false;
        }

        // Validate Email
        if (employee.getEmail() == null || !employee.getEmail().matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
            App.showError("Please enter a valid email address.");
            return false;
        }

        // Validate Phone (Canadian format)
        if (employee.getPhone() == null || !employee.getPhone().matches("^\\d{3}-\\d{3}-\\d{4}$")) {
            App.showError("Phone number must be in the format XXX-XXX-XXXX.");
            return false;
        }

        // Validate Address
        if (employee.getAddress() == null || employee.getAddress().isEmpty()) {
            App.showError("Please provide your address");
            return false;
        }

        // Validate Postal Code (Canadian format)
        if (employee.getPostcode() == null || !employee.getPostcode().matches("^[A-Za-z]\\d[A-Za-z]\\d[A-Za-z]\\d$")) {
            App.showError("Postal Code must be in the format M4R1R4.");
            return false;
        }

        // Validate Password
        if (employee.getPassword() == null || employee.getPassword().isEmpty()) {
            App.showError("Please generate a password for this employee");
            return false;
        }

        // Validate Hourly Rate
        if (employee.getHourlyRate() <= 0) {
            App.showError("Hourly Rate must be a greater than 0.");
            return false;
        }

        // Validate Date Fields
        if (employee.getStartDate() == null) {
            App.showError("Start Date and End Date must be provided.");
            return false;
        }

        if (employee.getEndDate() != null) {
            if (employee.getEndDate().before(employee.getStartDate())) {
                App.showError("End Date cannot be before Start Date.");
                return false;
            }
        }

        // All validations passed
        return true;
    }

    private List<Department> loadDepartmentsFromDB() {
        List<Department> departments = db.getAllDepartments();
        return departments;
    }

    private void loadDepartmentsForComboBox() {
        List<Department> departments = db.getAllDepartments();
        if (departments == null || departments.isEmpty()) {
            System.out.println("No departments found.");
        } else {
            System.out.println("Departments loaded: " + departments.size());
        }
        add_departmentField.getItems().clear();
        add_departmentField.getItems().addAll(departments);
    }

    private void showAddEmployeePane() {
        employeeTablePane.setVisible(false);
        employeeAddFormPane.setVisible(true);
        employeeUpdateFormPane.setVisible(false);
        add_firstNameField.requestFocus();
    }

    private void showUpdateEmployeePane() {
        selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            initializeUpdateEmployeeFields(selectedEmployee);
            employeeTablePane.setVisible(false);
            employeeAddFormPane.setVisible(false);
            employeeUpdateFormPane.setVisible(true);
            update_firstNameField.requestFocus();
        } else {
            // Show a dialog with a message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Employee Selected");
            alert.setContentText("Please select an employee to update.");
            alert.showAndWait();
        }
    }

    private void showTableEmployeePane() {
        employeeTablePane.setVisible(true);
        employeeAddFormPane.setVisible(false);
        employeeUpdateFormPane.setVisible(false);
        updateButtonVisibility();
    }

    private void loadEmployeesFromDB() {
        List<Employee> employeesFromDB = db.getAlleEmployees();
        employeeList.clear();
        employeeList.addAll(employeesFromDB);
    }

    private void initializeAddEmployeeFields() {
        add_employeeIdField.setText(generateEmployeeId());
        // Get all departments and set default value to departments
        List<Department> departments = loadDepartmentsFromDB();
        add_departmentField.getItems().addAll(departments);
        add_departmentField.getSelectionModel().selectFirst();

        // Predefined roles for the application
        List<String> roles = Arrays.asList(
                "Software Engineer",
                "Project Manager",
                "Business Analyst",
                "UI/UX Designer",
                "Software Tester",
                "Product Manager",
                "Customer Support",
                "HR Specialist",
                "Data Scientist",
                "Marketing Specialist"
        );

        // Set the roles into the ComboBox
        add_roleField.getItems().addAll(roles);
        add_roleField.getSelectionModel().selectFirst();
        // Set the default country to Canada
        add_countryField.setText("CANADA");
        add_countryField.isDisabled();
        add_hourlyRateField.setText("17.30");

        // List of Canadian provinces
        List<String> provinces = Arrays.asList(
                "Alberta",
                "British Columbia",
                "Manitoba",
                "New Brunswick",
                "Newfoundland and Labrador",
                "Nova Scotia",
                "Ontario",
                "Prince Edward Island",
                "Quebec",
                "Saskatchewan"
        );

        // Add provinces to the ComboBox
        add_provinceField.getItems().addAll(provinces);

        // Set a default values
        add_cityField.setText("Toronto");
        add_provinceField.setValue("Ontario");

        // Set placeholder (prompt text) for the fields
        add_firstNameField.setPromptText("eg: John");
        add_lastNameField.setPromptText("eg: Smith");
        add_emailField.setPromptText("eg: johnsmith@georgebrown.ca");
        add_phoneField.setPromptText("eg: 6478889999");
        add_addressField.setPromptText("eg: 101 Queen St East");
        add_postalField.setPromptText("eg: M4J 1R4");
        add_passwordField.setPromptText("*********");
        add_firstNameField.requestFocus();

    }

    private void initializeUpdateEmployeeFields(Employee employee) {
        // List of Canadian provinces
        List<String> provinces = Arrays.asList(
                "Alberta", "British Columbia", "Manitoba", "New Brunswick",
                "Newfoundland and Labrador", "Nova Scotia", "Ontario",
                "Prince Edward Island", "Quebec", "Saskatchewan"
        );
        List<Department> departments = loadDepartmentsFromDB();
        // Predefined roles for the application
        List<String> roles = Arrays.asList(
                "Software Engineer",
                "Project Manager",
                "Business Analyst",
                "UI/UX Designer",
                "Software Tester",
                "Product Manager",
                "Customer Support",
                "HR Specialist",
                "Data Scientist",
                "Marketing Specialist"
        );

        // Update the values based on the selected employee
        update_roleField.getItems().addAll(roles);
        update_roleField.getSelectionModel().select(employee.getRole());
        update_countryField.setText("CANADA");
        update_countryField.setDisable(true);
        update_hourlyRateField.setText(String.valueOf(employee.getHourlyRate()));
        update_departmentField.getItems().addAll(departments);
        update_departmentField.getSelectionModel().select(departments.stream()
                .filter(department -> department.getDepartmentId() == employee.getDepartmentId())
                .findFirst().orElse(null));
        update_employeeIdField.setText(employee.getEmployeeId());
        update_provinceField.getItems().addAll(provinces);
        update_provinceField.setValue(employee.getProvince());
        update_cityField.setText(employee.getCity());
        update_firstNameField.setText(employee.getFirstName());
        update_lastNameField.setText(employee.getLastName());
        update_emailField.setText(employee.getEmail());
        update_phoneField.setText(employee.getPhone());
        update_addressField.setText(employee.getAddress());
        update_postalField.setText(employee.getPostcode());
        update_passwordField.setText(employee.getPassword());
        update_userActiveField.setSelected(employee.getIsActive());

        // Handle dates - convert java.util.Date to LocalDate
        if (employee.getStartDate() != null) {
            update_startDateField.setValue(employee.getStartDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate());
        }
        if (employee.getEndDate() != null) {
            update_endDateField.setValue(employee.getEndDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate());
        }

        // Set placeholders (prompt text) for the fields, if needed
        update_firstNameField.setPromptText("eg: John");
        update_lastNameField.setPromptText("eg: Smith");
        update_emailField.setPromptText("eg: johnsmith@georgebrown.ca");
        update_phoneField.setPromptText("eg: 6478889999");
        update_addressField.setPromptText("eg: 101 Queen St East");
        update_postalField.setPromptText("eg: M4J 1R4");
        update_passwordField.setPromptText("*********");
    }

    private void populateUpdateForm(Employee employee) {
        if (employee != null) {
            update_employeeIdField.setText(employee.getEmployeeId());
            update_firstNameField.setText(employee.getFirstName());
            update_lastNameField.setText(employee.getLastName());
            update_emailField.setText(employee.getEmail());
            update_phoneField.setText(employee.getPhone());
            update_addressField.setText(employee.getAddress());
            update_cityField.setText(employee.getCity());
            update_provinceField.setValue(employee.getProvince());
            update_postalField.setText(employee.getPostcode());
            update_countryField.setText(employee.getCountry());
            update_passwordField.setText(employee.getPassword());
            update_hourlyRateField.setText(String.valueOf(employee.getHourlyRate()));
            update_roleField.setValue(employee.getRole());
            update_userActiveField.setSelected(employee.getIsActive());

            // Handle dates
            if (employee.getStartDate() != null) {
                update_startDateField.setValue(employee.getStartDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate());
            }
            if (employee.getEndDate() != null) {
                update_endDateField.setValue(employee.getEndDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate());
            }

            // Set department
            List<Department> departments = db.getAllDepartments();
            for (Department dept : departments) {
                if (dept.getDepartmentId() == employee.getDepartmentId()) {
                    update_departmentField.setValue(dept);
                    break;
                }
            }
        }
    }

    private double parseDoubleOrDefault(String text, double defaultValue) {
        if (text == null || text.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void filterEmployeeData(String query) {
        // Normalize the query to lowercase to make the search case-insensitive
        String normalizedQuery = query.toLowerCase();

        // Filter the employeeList based on whether any of the employee properties match the query
        ObservableList<Employee> filteredList = FXCollections.observableArrayList(
                employeeList.stream()
                        .filter(employee -> employee.getFirstName().toLowerCase().contains(normalizedQuery) ||
                                employee.getLastName().toLowerCase().contains(normalizedQuery) ||
                                employee.getEmail().toLowerCase().contains(normalizedQuery) ||
                                employee.getPhone().toLowerCase().contains(normalizedQuery) ||
                                employee.getCity().toLowerCase().contains(normalizedQuery))
                        .collect(Collectors.toList())
        );

        // Update the TableView with the filtered list
        employeeTable.setItems(filteredList);
    }

    // Helper method to convert java.util.Date to LocalDate to display back in date picker
    private LocalDate convertToLocalDate(Object date) {
        if (date != null) {
            // Convert java.sql.Date directly to LocalDate as error is thrown when we set date to date picker
            if (date instanceof java.sql.Date) {
                return ((java.sql.Date) date).toLocalDate();
            } else if (date instanceof java.util.Date) {
                return ((java.util.Date) date).toInstant()
                        .atZone(ZoneId.systemDefault())  // Convert to ZonedDateTime
                        .toLocalDate();  // Extract LocalDate
            }
        }
        // Return null if date is null
        return null;
    }

    public void updateButtonVisibility() {
        // Check if user is logged in and if the role is APP_ADMIN
        if (App.isUserLoggedIn && App.loggedInUserProfile != null) {
            if ("APP_ADMIN".equals(App.loggedInUserProfile.getRole())) {
                // Make buttons visible if the role is APP_ADMIN
                showAddEmployeePaneButton.setVisible(true);
                deleteEmployeeButton.setVisible(true);
            } else {
                // Hide buttons if the role is not APP_ADMIN
                showAddEmployeePaneButton.setVisible(false);
                deleteEmployeeButton.setVisible(false);
            }
        } else {
            // If no user is logged in, hide the buttons
            showAddEmployeePaneButton.setVisible(false);
            deleteEmployeeButton.setVisible(false);
        }
    }

    /* MENU HANDLERS */
    public void showPayrollView() {
        App.showPayrollView(payrollButton);
    }

    public void showDepartmentView() {
        App.showDepartmentView(departmentButton);
    }

    public void logoutUser() {
        App.logoutUser(logoutButton);
    }

    private String generateEmployeeId() {
        // Get the current list of employees
        List<Employee> employees = db.getAlleEmployees();
        
        // Find the highest employee ID number
        int maxNumber = 0;
        String prefix = "EMP";
        
        for (Employee emp : employees) {
            String empId = emp.getEmployeeId();
            if (empId != null && empId.startsWith(prefix)) {
                try {
                    int number = Integer.parseInt(empId.substring(prefix.length()));
                    maxNumber = Math.max(maxNumber, number);
                } catch (NumberFormatException e) {
                    // Skip if the ID doesn't follow the expected format
                    continue;
                }
            }
        }
        
        // Generate new ID by incrementing the highest number
        int newNumber = maxNumber + 1;
        
        // Format the ID with leading zeros (e.g., EMP001, EMP002, etc.)
        return String.format("%s%03d", prefix, newNumber);
    }
}
