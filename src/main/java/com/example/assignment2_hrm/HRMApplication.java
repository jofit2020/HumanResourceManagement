package com.example.assignment2_hrm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HRMApplication extends Application {
   private Db db = new Db();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HRMApplication.class.getResource("hrm-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        // Add stylesheet
        String stylesheet = getClass().getResource("/css/fullpackstyling.css") != null
                ? getClass().getResource("/css/fullpackstyling.css").toExternalForm()
                : null;
        if (stylesheet != null) {
            scene.getStylesheets().add(stylesheet);
        } else {
            System.err.println("Stylesheet not found!");
        }

        stage.setTitle("Human Resource Management System");
        stage.setScene(scene);
        stage.show();

        db.getConnection();
        db.createUserProfileTable();
        db.createDepartmentTable();
        db.createEmployeeTable();
    }


    public static void main(String[] args) {
        launch();
    }
}