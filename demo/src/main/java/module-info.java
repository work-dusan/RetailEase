module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires org.json;
    requires java.desktop;
    requires javafx.web;
    requires json.simple;
    requires org.jsoup;
    requires selenium.java;
    requires jcommander;
    requires async.http.client;
    requires selenium.api;
    requires selenium.chrome.driver;

    opens main to javafx.fxml;
    opens products to javafx.base;
    opens delivery to javafx.base;

    exports main;
    exports testing;
}
