module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens main to javafx.fxml;
    opens products to javafx.base;
    exports main;
}