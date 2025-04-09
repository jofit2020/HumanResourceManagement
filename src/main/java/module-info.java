module com.example.assignment2_hrm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.assignment2_hrm to javafx.fxml;
    exports com.example.assignment2_hrm;
}