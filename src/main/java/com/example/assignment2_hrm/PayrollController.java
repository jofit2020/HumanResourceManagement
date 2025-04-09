package com.example.assignment2_hrm;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PayrollController {
    @FXML
    private Button employeesButton;
    @FXML
    private Button departmentButton;
    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        logoutButton.setOnAction(event -> logoutUser());
        departmentButton.setOnAction(event -> showDepartmentView());
        employeesButton.setOnAction(event -> showEmployeesView());
    }

    /* MENU HANDLERS */
    public void showDepartmentView() {
        App.showDepartmentView(departmentButton);
    }

    public void showEmployeesView() {
        App.showEmployeesView(employeesButton);
    }

    public void logoutUser() {
        App.logoutUser(logoutButton);
    }
}
