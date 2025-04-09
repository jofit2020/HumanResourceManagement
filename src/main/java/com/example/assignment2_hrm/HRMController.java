package com.example.assignment2_hrm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HRMController {
    private Db db = new Db();

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button employeesButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button departmentButton;

    @FXML
    private Button payrollButton;

    @FXML
    private Pane loginPane;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> login());
        employeesButton.setOnAction(event -> showEmployeesView());
        payrollButton.setOnAction(event -> showPayrollView());
        departmentButton.setOnAction(event -> showDepartmentView());
        exitButton.setOnAction(event -> exitApp());
    }

    @FXML
    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate if the username and password fields are not empty
        if (username.isEmpty() || password.isEmpty()) {
            App.showError("Username and Password are required.");
            return;
        }

        // Check credentials in the UserProfile table
        UserProfile userProfile = db.validateUserCredentials(username, password);
        if (userProfile != null) {
            // Set the flag to true
            App.isUserLoggedIn = true;
            // Set Current logged in user
            App.loggedInUserProfile = userProfile;
            App.showInfo("Login successful.");
            showEmployeesView();
        } else {
            App.showError("Invalid username or password.");
        }
    }

    /* MENU HANDLERS */
    public void showEmployeesView() {
        App.showEmployeesView(employeesButton);
    }

    public void showPayrollView() {
        App.showPayrollView(payrollButton);
    }

    public void showDepartmentView() {
        App.showDepartmentView(departmentButton);
    }

    public void exitApp() {
        App.exitApp();
    }



}