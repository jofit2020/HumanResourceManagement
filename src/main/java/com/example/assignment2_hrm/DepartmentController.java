package com.example.assignment2_hrm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.util.List;

public class DepartmentController {
    @FXML
    private Button employeesButton;
    @FXML
    private Button payrollButton;
    @FXML
    private Button logoutButton;

    @FXML
    private Pane mainPane;  // The main pane with the table view

    @FXML
    private Pane formPane;  // The pane with the add/update form

    @FXML
    private TextField formDepartmentName;  // TextField in the form

    @FXML
    private TextField formDepartmentCode;  // TextField in the form

    @FXML
    private ComboBox<String> formManagerId;  // ComboBox in the form

    @FXML
    private Button btnSave;  // Save button in the form

    @FXML
    private Button btnCancel;  // Cancel button in the form

    @FXML
    private TableView<Department> tblDepartments;

    @FXML
    private TableColumn<Department, Integer> colId;

    @FXML
    private TableColumn<Department, String> colName;

    @FXML
    private TableColumn<Department, String> colCode;

    @FXML
    private TableColumn<Department, Integer> colEmployees;

    @FXML
    private TableColumn<Department, String> colManager;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField txtDepartmentName;  // Search field for department name

    @FXML
    private TextField txtDepartmentCode;  // Search field for department code

    private ObservableList<Department> departmentList = FXCollections.observableArrayList();
    private FilteredList<Department> filteredDepartments;

    private final Db db = new Db();

    private boolean isUpdateMode = false;
    private Department currentDepartment;

    @FXML
    public void initialize() {
        // Initialize navigation buttons
        logoutButton.setOnAction(event -> logoutUser());
        payrollButton.setOnAction(event -> showPayrollView());
        employeesButton.setOnAction(event -> showEmployeesView());

        // Check user role and restrict access accordingly
        boolean isAdmin = App.loggedInUserProfile != null && 
                         App.loggedInUserProfile.getRole().equals(UserProfile.ROLE_ADMIN);
        
        // Hide admin-only buttons for non-admin users
        btnAdd.setVisible(isAdmin);
        btnAdd.setManaged(isAdmin);
        btnUpdate.setVisible(isAdmin);
        btnUpdate.setManaged(isAdmin);
        btnDelete.setVisible(isAdmin);
        btnDelete.setManaged(isAdmin);

        // Recreate department table with test data
        db.createDepartmentTable();

        // Initialize manager ID combo box
        List<String> managerIds = db.getAllEmployeeIds();
        formManagerId.setItems(FXCollections.observableArrayList(managerIds));

        // Initialize department table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("departmentCode"));  
        colEmployees.setCellValueFactory(new PropertyValueFactory<>("numberOfEmployees"));
        colManager.setCellValueFactory(new PropertyValueFactory<>("managerId"));

        // Initialize filtered list
        filteredDepartments = new FilteredList<>(departmentList, p -> true);

        // Set table to use the filtered list
        tblDepartments.setItems(filteredDepartments);

        // Load data from database
        loadDepartmentsFromDatabase();

        // Add table selection listener - only for admin users
        if (isAdmin) {
            tblDepartments.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    currentDepartment = newSelection;
                }
            });
        }

        // Add listeners to search fields
        txtDepartmentName.textProperty().addListener((observable, oldValue, newValue) -> {
            updateFilter();
        });

        txtDepartmentCode.textProperty().addListener((observable, oldValue, newValue) -> {
            updateFilter();
        });
    }

    private void updateFilter() {
        filteredDepartments.setPredicate(department -> {
            // If no search terms, show all departments
            if (txtDepartmentName.getText().isEmpty() && txtDepartmentCode.getText().isEmpty()) {
                return true;
            }

            String nameSearch = txtDepartmentName.getText().toLowerCase();
            String codeSearch = txtDepartmentCode.getText().toLowerCase();

            boolean nameMatch = department.getDepartmentName().toLowerCase().contains(nameSearch);
            boolean codeMatch = department.getDepartmentCode().toLowerCase().contains(codeSearch);

            // If both fields have search terms, both must match
            if (!txtDepartmentName.getText().isEmpty() && !txtDepartmentCode.getText().isEmpty()) {
                return nameMatch && codeMatch;
            }

            // If only name field has search term
            if (!txtDepartmentName.getText().isEmpty()) {
                return nameMatch;
            }

            // If only code field has search term
            if (!txtDepartmentCode.getText().isEmpty()) {
                return codeMatch;
            }

            return false;
        });
    }

    @FXML
    public void showMainPane() {
        mainPane.setVisible(true);
        formPane.setVisible(false);
        clearFormFields();
        isUpdateMode = false;
        currentDepartment = null;
    }

    private void showAddForm() {
        mainPane.setVisible(false);
        formPane.setVisible(true);
        clearFormFields();
        isUpdateMode = false;
    }

    private void showUpdateForm() {
        if (currentDepartment == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a department to update");
            return;
        }

        mainPane.setVisible(false);
        formPane.setVisible(true);
        populateFormFields(currentDepartment);
        isUpdateMode = true;
    }

    private void clearFormFields() {
        formDepartmentName.clear();
        formDepartmentCode.clear();
        formManagerId.setValue(null);
    }

    private void populateFormFields(Department department) {
        formDepartmentName.setText(department.getDepartmentName());
        formDepartmentCode.setText(department.getDepartmentCode());
        formManagerId.setValue(department.getManagerId());
    }

    private boolean validateFormInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (formDepartmentName.getText().trim().isEmpty()) {
            errorMessage.append("Department name is required\n");
        }
        if (formDepartmentCode.getText().trim().isEmpty()) {
            errorMessage.append("Department code is required\n");
        }
        if (formManagerId.getValue() == null || formManagerId.getValue().trim().isEmpty()) {
            errorMessage.append("Manager ID is required\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", errorMessage.toString());
            return false;
        }
        return true;
    }

    private void checkAdminAccess() {
        if (App.loggedInUserProfile == null || !App.loggedInUserProfile.getRole().equals(UserProfile.ROLE_ADMIN)) {
            showAlert(Alert.AlertType.ERROR, "Access Denied", "You do not have permission to perform this operation.");
            throw new SecurityException("Operation requires admin privileges");
        }
    }

    @FXML
    private void handleSave() {
        try {
            checkAdminAccess();
            
            if (!validateFormInput()) {
                return;
            }

            try {
                if (isUpdateMode && currentDepartment != null) {
                    // Update existing department
                    currentDepartment.setDepartmentName(formDepartmentName.getText().trim());
                    currentDepartment.setDepartmentCode(formDepartmentCode.getText().trim());
                    currentDepartment.setManagerId(formManagerId.getValue().trim());
                    db.updateDepartment(currentDepartment);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Department updated successfully!");
                } else {
                    // Add new department
                    Department department = new Department();
                    department.setDepartmentName(formDepartmentName.getText().trim());
                    department.setDepartmentCode(formDepartmentCode.getText().trim());
                    department.setManagerId(formManagerId.getValue().trim());
                    department.setNumberOfEmployees(0); // Set default number of employees to 0
                    db.insertDepartment(department);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Department added successfully!");
                }

                loadDepartmentsFromDatabase();
                showMainPane();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", 
                         isUpdateMode ? "Failed to update department: " : "Failed to add department: " + e.getMessage());
            }
        } catch (SecurityException e) {

            showMainPane();
        }
    }

    @FXML
    private void handleDeleteDepartment() {
        try {
            checkAdminAccess();
            
            Department selectedDepartment = tblDepartments.getSelectionModel().getSelectedItem();
            if (selectedDepartment == null) {
                showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a department to delete");
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Delete");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Are you sure you want to delete this department? This will affect all employees in this department.");

            if (confirmation.showAndWait().get() == ButtonType.OK) {
                try {
                    db.deleteDepartment(selectedDepartment.getDepartmentId());
                    loadDepartmentsFromDatabase();
                    showMainPane();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Department deleted successfully!");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete department: " + e.getMessage());
                }
            }
        } catch (SecurityException e) {
        }
    }

    @FXML
    private void handleAddDepartment() {
        try {
            checkAdminAccess();
            showAddForm();
        } catch (SecurityException e) {

        }
    }

    @FXML
    private void handleUpdateDepartment() {
        try {
            checkAdminAccess();
            showUpdateForm();
        } catch (SecurityException e) {

        }
    }

    private void loadDepartmentsFromDatabase() {
        try {
            List<Department> departments = db.getAllDepartments();
            departmentList.clear();
            departmentList.addAll(departments);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load departments. Error: " + e.getMessage());
        }
    }

    public void showPayrollView() {
        App.showPayrollView(payrollButton);
    }

    public void showEmployeesView() {
        App.showEmployeesView(employeesButton);
    }

    public void logoutUser() {
        App.logoutUser(logoutButton);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
