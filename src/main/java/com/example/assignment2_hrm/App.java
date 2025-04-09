package com.example.assignment2_hrm;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

/* ALL COMMON METHODS ARE ADDED TO THIS STATIC CLASS TO REDUCE CODE REDUNDANCY */

public class App {
    /* Flags to check if user is logged in and who is logged in */
    public static boolean isUserLoggedIn = false;
    public static UserProfile loggedInUserProfile = null;

    /* This method loads the employee view */
    public static void showEmployeesView(Button btnEmployees) {
        if (isUserLoggedIn) {
            try {
                // Load the employee view from the FXML file
                FXMLLoader loader = new FXMLLoader(App.class.getResource("employee-view.fxml"));
                Parent employeeView = loader.load();

                // Get the current stage (window) from the button
                Stage stage = (Stage) btnEmployees.getScene().getWindow();

                // Set the new scene
                stage.setScene(new Scene(employeeView));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            showError("Please login first to access Employees page.");
        }

    }

    /* This method loads the department view */
    public static void showDepartmentView(Button departmentButton) {
        if (isUserLoggedIn) {
            try {
                // Load the department view from the FXML file
                FXMLLoader loader = new FXMLLoader(App.class.getResource("department-view.fxml"));
                Parent departmentView = loader.load();

                // Get the current stage (window) from the button
                Stage stage = (Stage) departmentButton.getScene().getWindow();

                // Set the new scene
                stage.setScene(new Scene(departmentView));
            } catch (Exception e) {
                e.printStackTrace();
                showError("Failed to load the Department view.");
            }
        }
        else {
            showError("Please login first to access Department page.");
        }

    }

    /* This method loads the payroll view */
    public static void showPayrollView(Button payrollButton) {
        if (isUserLoggedIn) {
            try {
                // Load the Payroll view from the FXML file
                FXMLLoader loader = new FXMLLoader(App.class.getResource("payroll-view.fxml"));
                Parent employeeView = loader.load();

                // Get the current stage (window) from the button
                Stage stage = (Stage) payrollButton.getScene().getWindow();

                // Set the new scene
                stage.setScene(new Scene(employeeView));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            showError("Please login first to access Payroll page.");
        }

    }

    /* This method logs out user and sets UserProfile to null */
    public static void logoutUser(Button logoutButton) {
        // Clear the login state
        isUserLoggedIn = false;
        loggedInUserProfile = null;

        // Load the main HRM view)
        try {
            // Load the new scene from the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(HRMApplication.class.getResource("hrm-view.fxml"));
            Parent root = fxmlLoader.load();

            // Get the current stage
            Stage stage = (Stage) logoutButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Handle the exception if FXML loading fails
            e.printStackTrace();
            showError("Error loading the HRM view.");
        }
    }

    /* This method shows error popup with message */
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /* This method shows information popup with message */
    public static void showInfo(String message) {
        // Creating an alert with the INFORMATION type
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

        // Set the title of the alert
        infoAlert.setTitle("Information");

        // Set the header text
        infoAlert.setHeaderText(null);

        // Set the content text
        infoAlert.setContentText(message);

        // Show the alert and wait for the user to close it
        infoAlert.showAndWait();
    }

    /* This method exists the app */
    public static void exitApp() {
        Platform.exit();
        System.exit(0);
    }

}
