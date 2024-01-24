package main;

import DB.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import products.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WarehouseEmployeeMainScene {
    public Scene createWarehouseEmployeeMainScene(Stage primaryStage){
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20,20,20,20));

        //center

        ObservableList<Product> productList = fetchProducts();

        TableView<Product> productTableView = new TableView<>(productList);

        TableColumn<Product, String> productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));

        TableColumn<Product, String> productNameColumn = new TableColumn<>("Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<Product, Double> productPriceColumn = new TableColumn<>("Price");
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> productQuantityColumn = new TableColumn<>("Quantity");
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));

        TableColumn<Product, String> productTypeColumn = new TableColumn<>("Type");
        productTypeColumn.setCellValueFactory(new PropertyValueFactory<>("productType"));

        TableColumn<Product, String> productDescriptionColumn = new TableColumn<>("Description");
        productDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Product, LocalDate> productExpirationDateColumn = new TableColumn<>("Expiration date");
        productExpirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));

        TableColumn<Product, String> productSupplierColumn = new TableColumn<>("Supplier");
        productSupplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));

        productTableView.getColumns().addAll(productIdColumn, productNameColumn, productPriceColumn, productQuantityColumn, productTypeColumn, productDescriptionColumn, productExpirationDateColumn, productSupplierColumn);

        pane.setCenter(productTableView);

        return new Scene(pane, 1000, 1000);
    }

    private ObservableList<Product> fetchProducts(){
        List<Product> productList = new ArrayList<>();

        try(Connection connection = DatabaseConnector.connect();
            Statement statement = connection.createStatement()){

            String query = "SELECT * FROM product";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                String productId = resultSet.getString("PRODUCT_ID");
                String productName = resultSet.getString("NAME");
                double price = resultSet.getDouble("PRICE");
                int quantity = resultSet.getInt("QUANTITY");
                String type = resultSet.getString("TYPE");
                String description = resultSet.getString("DESCRIPTION");
                LocalDate expirationDate = resultSet.getDate("EXPIRATION_DATE").toLocalDate();
                String supplier = resultSet.getString("SUPPLIER");

                Product product = new Product(productId, productName, price, quantity, type, description, expirationDate, supplier);
                productList.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(productList);
    }
}
