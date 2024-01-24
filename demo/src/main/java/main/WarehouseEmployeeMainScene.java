package main;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import products.Product;
import validations.ProductValidation;


import java.time.LocalDate;

import static products.ProductCRUD.*;

public class WarehouseEmployeeMainScene {
    public Scene createWarehouseEmployeeMainScene(Stage primaryStage) {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20, 20, 20, 20));

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

        productTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    openProductDetailsWindow(selectedProduct, productTableView);
                }
            }
        });

        pane.setCenter(productTableView);

        return new Scene(pane, 1000, 1000);
    }

    private void openProductDetailsWindow(Product product, TableView<Product> productTableView) {
        Stage detailsStage = new Stage();

        BorderPane detailsPane = new BorderPane();
        detailsPane.setPadding(new Insets(10, 20, 10, 20));

        TextField productNameField = new TextField(product.getProductName());
        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        TextField quantityField = new TextField(String.valueOf(product.getQuantityInStock()));
        TextField typeField = new TextField(product.getProductType());
        TextField descriptionField = new TextField(product.getDescription());
        DatePicker expirationDateField = new DatePicker(product.getExpirationDate());
        TextField supplierField = new TextField(product.getSupplier());

        Label productIdLabel = new Label("Product ID: ");
        Label productNameLabel = new Label("Name: ");
        Label priceLabel = new Label("Price: ");
        Label quantityLabel = new Label("Quantity: ");
        Label typeLabel = new Label("Type: ");
        Label descriptionLabel = new Label("Description: ");
        Label expirationDateLabel = new Label("Expiration date: ");
        Label supplierLabel = new Label("Supplier: ");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.addRow(0, productIdLabel, new Label(product.getProductId()));
        gridPane.addRow(1, productNameLabel, productNameField);
        gridPane.addRow(2, priceLabel, priceField);
        gridPane.addRow(3, quantityLabel, quantityField);
        gridPane.addRow(4, typeLabel, typeField);
        gridPane.addRow(5, descriptionLabel, descriptionField);
        gridPane.addRow(6, expirationDateLabel, expirationDateField);
        gridPane.addRow(7, supplierLabel, supplierField);

        Label title = new Label("PRODUCT INFORMATION");
        title.setPadding(new Insets(0, 0, 20, 0));

        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(gridPane, Pos.CENTER);

        detailsPane.setTop(title);
        detailsPane.setCenter(gridPane);

        Button productChangeButton = new Button("Change");
        Button productDeleteButton = new Button("Delete");

        HBox buttonBox = new HBox(productChangeButton, productDeleteButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);

        detailsPane.setBottom(buttonBox);

        productChangeButton.setOnAction(event -> {
            if (validateProductFields(productNameField.getText(), priceField.getText(), quantityField.getText(), typeField.getText(), descriptionField.getText(), expirationDateField.getValue(), supplierField.getText())) {
                updateProduct(product.getProductId(), productNameField.getText(), Double.parseDouble(priceField.getText()), Integer.parseInt(quantityField.getText()), typeField.getText(), descriptionField.getText(), expirationDateField.getValue(), supplierField.getText());
                showAlert("Success", "Product updated successfully.", Alert.AlertType.CONFIRMATION);

                int index = productTableView.getItems().indexOf(product);

                productTableView.getItems().set(index, new Product(product.getProductId(), productNameField.getText(), Double.parseDouble(priceField.getText()), Integer.parseInt(quantityField.getText()), typeField.getText(), descriptionField.getText(), expirationDateField.getValue(), supplierField.getText()));
                productTableView.refresh();

                detailsStage.close();
            } else {
                showAlert("Error", "Validation failed.", Alert.AlertType.ERROR);
            }
        });

        productDeleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Product");
            alert.setContentText("Are you sure you want to delete this product?");

            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(okButton, cancelButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == okButton) {
                    boolean deleted = deleteProduct(product.getProductId());

                    if (deleted) {
                        productTableView.getItems().remove(product);
                        showAlert("Success", "Product deleted successfully.", Alert.AlertType.CONFIRMATION);
                        detailsStage.close();
                    } else {
                        showAlert("Error", "Failed to delete product.", Alert.AlertType.ERROR);
                    }
                }
            });
        });

        Scene detailsScene = new Scene(detailsPane, 350, 400);
        detailsStage.setScene(detailsScene);

        detailsStage.show();
    }

    private boolean validateProductFields(String productName, String price, String quantity, String type, String description, LocalDate expirationDate, String supplier) {
        return ProductValidation.validateProductName(productName) &&
                ProductValidation.validatePrice(Double.parseDouble(price)) &&
                ProductValidation.validateQuantityInStock(Integer.parseInt(quantity)) &&
                ProductValidation.validateProductType(type) &&
                ProductValidation.validateDescription(description) &&
                ProductValidation.validateExpirationDate(expirationDate) &&
                ProductValidation.validateSupplier(supplier);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
