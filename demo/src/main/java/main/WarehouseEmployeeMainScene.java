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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import products.Product;
import users.WarehouseEmployee;
import validations.ProductValidation;


import java.time.LocalDate;

import static products.ProductCRUD.*;

public class WarehouseEmployeeMainScene {
    public Scene createWarehouseEmployeeMainScene(Stage primaryStage, WarehouseEmployee loggedInEmployee) {

        // TOP - START

        Label welcomeLabel = new Label("Welcome, " + loggedInEmployee.getFirstName() + " " + loggedInEmployee.getLastName());
        welcomeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // TOP - END
        // CENTER - START

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20, 20, 20, 20));

        Button addProductButton = new Button("Add product");

        Label searchLabel = new Label("Search: ");
        TextField searchTextField = new TextField();

        GridPane search = new GridPane();
        search.addRow(0, searchLabel, searchTextField);

        BorderPane addAndSearch = new BorderPane();
        addAndSearch.setLeft(addProductButton);
        addAndSearch.setRight(search);
        addAndSearch.setMaxWidth(700);

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

        productTableView.setMaxSize(700, 500);

        VBox center = new VBox(addAndSearch, productTableView);
        center.setAlignment(Pos.CENTER);

        addProductButton.setOnAction(event -> addProductWindow(productTableView));

        productTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    openProductDetailsWindow(selectedProduct, productTableView);
                }
            }
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) ->
                filterProductList(productList, productTableView, newValue)
        );

        // CENTER - END

        // BOTTOM - START

        Button checkExpirationButton = new Button("Check Expiration");
        checkExpirationButton.setOnAction(event -> checkExpiration(productList));

        HBox bottom = new HBox(checkExpirationButton);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(20);

        // BOTTOM - END

        pane.setTop(welcomeLabel);
        pane.setCenter(center);
        pane.setBottom(bottom);
        BorderPane.setAlignment(center, Pos.CENTER);


        return new Scene(pane, 1000, 1000);
    }

    private void addProductWindow(TableView<Product> productTableView){
        Stage addProductStage = new Stage();

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        TextField productIdField = new TextField();
        TextField productNameField = new TextField();
        TextField priceField = new TextField();
        TextField quantityField = new TextField();
        TextField typeField = new TextField();
        TextField descriptionField = new TextField();
        DatePicker expirationDateField = new DatePicker();
        TextField supplierField = new TextField();

        Label productIdLabel = new Label("Product ID: ");
        Label productNameLabel = new Label("Name: ");
        Label priceLabel = new Label("Price: ");
        Label quantityLabel = new Label("Quantity: ");
        Label typeLabel = new Label("Type: ");
        Label descriptionLabel = new Label("Description: ");
        Label expirationDateLabel = new Label("Expiration date: ");
        Label supplierLabel = new Label("Supplier: ");

        Button addProductButton = new Button("Add product");

        gridPane.addRow(0, productIdLabel, productIdField);
        gridPane.addRow(1, productNameLabel, productNameField);
        gridPane.addRow(2, priceLabel, priceField);
        gridPane.addRow(3, quantityLabel, quantityField);
        gridPane.addRow(4, typeLabel, typeField);
        gridPane.addRow(5, descriptionLabel, descriptionField);
        gridPane.addRow(6, expirationDateLabel, expirationDateField);
        gridPane.addRow(7, supplierLabel, supplierField);
        gridPane.addRow(8, addProductButton);

        addProductButton.setOnAction(event -> {
            if(validateProductFields(productIdField.getText(), productNameField.getText(), priceField.getText(), quantityField.getText(), typeField.getText(), descriptionField.getText(), expirationDateField.getValue(), supplierField.getText())){
                addProduct(productIdField.getText(), productNameField.getText(), Double.parseDouble(priceField.getText()), Integer.parseInt(quantityField.getText()), typeField.getText(), descriptionField.getText(), expirationDateField.getValue(), supplierField.getText());
                showAlert("Success", "Product added successfully.", Alert.AlertType.CONFIRMATION);

                Product newProduct = new Product(productIdField.getText(), productNameField.getText(), Double.parseDouble(priceField.getText()), Integer.parseInt(quantityField.getText()), typeField.getText(), descriptionField.getText(), expirationDateField.getValue(), supplierField.getText());
                productTableView.getItems().add(newProduct);

                addProductStage.close();
            } else {
                showAlert("Error", "Product was not added.", Alert.AlertType.ERROR);
            }
        });

        Scene addProductScene = new Scene(gridPane, 350, 400);
        addProductStage.setScene(addProductScene);

        addProductStage.show();
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
            if (validateProductFields(product.getProductId(), productNameField.getText(), priceField.getText(), quantityField.getText(), typeField.getText(), descriptionField.getText(), expirationDateField.getValue(), supplierField.getText())) {
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

    private boolean validateProductFields(String productId, String productName, String price, String quantity, String type, String description, LocalDate expirationDate, String supplier) {
        return ProductValidation.validateProductId(productId) &&
                ProductValidation.validateProductName(productName) &&
                ProductValidation.validatePrice(Double.parseDouble(price)) &&
                ProductValidation.validateQuantityInStock(Integer.parseInt(quantity)) &&
                ProductValidation.validateProductType(type) &&
                ProductValidation.validateDescription(description) &&
                ProductValidation.validateExpirationDate(expirationDate) &&
                ProductValidation.validateSupplier(supplier);
    }

    private void filterProductList(ObservableList<Product> originalList, TableView<Product> tableView, String keyword) {
        ObservableList<Product> filteredList = originalList.filtered(product ->
                product.getProductId().toLowerCase().contains(keyword.toLowerCase()) ||
                        product.getProductName().toLowerCase().contains(keyword.toLowerCase()) ||
                        product.getProductType().toLowerCase().contains(keyword.toLowerCase()) ||
                        product.getSupplier().toLowerCase().contains(keyword.toLowerCase())
        );

        tableView.setItems(filteredList);
    }

    private void checkExpiration(ObservableList<Product> productList) {
        LocalDate currentDate = LocalDate.now();
        StringBuilder expiredProducts = new StringBuilder("Expired Products:\n");

        for (Product product : productList) {
            if (product.getExpirationDate() != null && product.getExpirationDate().isBefore(currentDate)) {
                expiredProducts.append(product.getProductId() + " " + product.getProductName()).append("\n");
            }
        }

        if (expiredProducts.length() > 18) {
            showAlert("Expired Products", expiredProducts.toString(), Alert.AlertType.INFORMATION);
        } else {
            showAlert("Expired Products", "No products have expired.", Alert.AlertType.INFORMATION);
        }
    }


    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
