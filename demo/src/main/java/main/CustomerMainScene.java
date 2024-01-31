package main;

import DB.DatabaseConnector;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import products.CartItem;
import products.Product;
import products.ShoppingCart;
import users.Customer;
import validations.CustomerValidations;

import java.time.LocalDate;

import static products.ProductCRUD.fetchProducts;

public class CustomerMainScene {

    private TableView<Product> productTableView = new TableView<>();
    private boolean isCartVisible = false;
    private ShoppingCart shoppingCart = new ShoppingCart();


    public Scene createCustomerMainScene(Stage primaryStage, Customer customer) {

        // TOP - START

        BorderPane root = new BorderPane();
        VBox top = new VBox();

        Label titleLabel = new Label("RetailEase");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: #007BFF; -fx-font-weight: bold;");

        Label welcomeLabel = new Label("Welcome, " + customer.getFirstName());
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #28A745; -fx-font-weight: bold;");


        titleLabel.setAlignment(Pos.CENTER);
        StackPane titlePane = new StackPane(titleLabel);
        welcomeLabel.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(welcomeLabel, new Insets(0, 0, 0, 50));

        top.setSpacing(20);
        top.getChildren().addAll(titlePane, welcomeLabel);

        // TOP - END

        // CENTER - START

        VBox middle = new VBox();

        ObservableList<Product> productList = fetchProducts();

        Label searchLabel = new Label("Search: ");
        TextField searchTextField = new TextField();

        searchTextField.textProperty().addListener((observable, oldValue, newValue) ->
                filterProductList(productList, productTableView, newValue)
        );

        GridPane search = new GridPane();
        search.addRow(0, searchLabel, searchTextField);
        search.setAlignment(Pos.CENTER_RIGHT);

        BorderPane searchBorderPane = new BorderPane();
        searchBorderPane.setRight(search);
        searchBorderPane.setMaxWidth(800);

        productTableView = new TableView<>(productList);

        TableColumn<Product, String> productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productIdColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Product, Image> productImageColumn = new TableColumn<>("Image");
        productImageColumn.setCellFactory(param -> {
            //Set up the ImageView
            final ImageView imageview = new ImageView();
            imageview.setFitHeight(100);
            imageview.setFitWidth(100);

            //Set up the Table
            TableCell<Product, Image> cell = new TableCell<>() {
                public void updateItem(Image item, boolean empty) {
                    if (item != null) {
                        imageview.setImage(item);
                    }
                }
            };
            // Attach the imageview to the cell
            cell.setGraphic(imageview);
            return cell;
        });
        productImageColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getProductImage()));
        productImageColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Product, String> productNameColumn = new TableColumn<>("Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productNameColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Product, Double> productPriceColumn = new TableColumn<>("Price");
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productPriceColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Product, String> productTypeColumn = new TableColumn<>("Type");
        productTypeColumn.setCellValueFactory(new PropertyValueFactory<>("productType"));
        productTypeColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Product, String> productDescriptionColumn = new TableColumn<>("Description");
        productDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        productDescriptionColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Product, LocalDate> productExpirationDateColumn = new TableColumn<>("Expiration date");
        productExpirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        productExpirationDateColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Product, String> productSupplierColumn = new TableColumn<>("Supplier");
        productSupplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        productSupplierColumn.setStyle("-fx-alignment: CENTER;");

        productTableView.getColumns().addAll(productIdColumn, productImageColumn, productNameColumn, productPriceColumn, productTypeColumn, productDescriptionColumn, productExpirationDateColumn, productSupplierColumn);

        productTableView.setMaxSize(800, 500);

        middle.setSpacing(20);
        middle.setAlignment(Pos.CENTER);
        middle.getChildren().addAll(searchBorderPane, productTableView);

        // CENTER - END

        // RIGHT - START

        HBox right = new HBox();
        HBox.setHgrow(right, Priority.ALWAYS);

        VBox cartBox = new VBox();

        Label totalLabel = new Label("Total: " + calculateTotal() + " din");

        TableView<CartItem> cartTableView = new TableView<>(shoppingCart.getItems());

        TableColumn<CartItem, String> cartProductIdColumn = new TableColumn<>("Product ID");
        cartProductIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getProductId()));

        TableColumn<CartItem, String> cartProductNameColumn = new TableColumn<>("Name");
        cartProductNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getProductName()));

        TableColumn<CartItem, Integer> cartProductQuantityColumn = new TableColumn<>("Quantity");
        cartProductQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<CartItem, Double> cartProductPriceColumn = new TableColumn<>("Price");
        cartProductPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getProduct().getPrice() * cellData.getValue().getQuantity()).asObject());

        TableColumn<CartItem, Void> cartRemoveColumn = new TableColumn<>("Remove");
        cartRemoveColumn.setCellFactory(param -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");

            {
                removeButton.setOnAction(event -> {
                    CartItem cartItem = getTableView().getItems().get(getIndex());
                    shoppingCart.removeItem(cartItem);
                    cartTableView.setItems(shoppingCart.getItems());
                    cartTableView.refresh();
                    totalLabel.setText("Total: " + calculateTotal() + " din");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                }
            }
        });

        cartTableView.getColumns().addAll(cartProductIdColumn, cartProductNameColumn, cartProductQuantityColumn, cartProductPriceColumn, cartRemoveColumn);

        VBox cartContent = new VBox();
        cartContent.getChildren().addAll(new Label("Shopping Cart"), cartTableView);

        totalLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #FF4500; -fx-font-weight: bold;");
        Button checkoutButton = new Button("Checkout");
        cartBox.getChildren().addAll(new Label("Shopping Cart"), cartTableView, totalLabel, checkoutButton);

        // RIGHT - END

        // LEFT - START

        VBox left = new VBox();

        Button changeInfoButton = new Button("Change Information");
        Button logoutButton = new Button("Logout");

        left.setSpacing(10);
        left.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(changeInfoButton, new Insets(20, 0, 0, 20));
        VBox.setMargin(logoutButton, new Insets(0, 0, 0, 20));

        left.getChildren().addAll(changeInfoButton, logoutButton);

        root.setLeft(left);

        // LEFT - END

        // BOTTOM - START

        HBox bottom = new HBox();
        Button cartButton = new Button("Cart");

        HBox.setHgrow(cartButton, Priority.ALWAYS);
        bottom.setAlignment(Pos.BOTTOM_RIGHT);

        BorderPane.setAlignment(bottom, Pos.BOTTOM_RIGHT);
        bottom.getChildren().addAll(cartButton);

        // BOTTOM - END

        root.setTop(top);
        root.setCenter(middle);
        root.setRight(right);
        root.setBottom(bottom);

        // EVENTS

        productTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    // Check if product is in cart
                    boolean alreadyInCart = false;
                    for (CartItem cartItem : shoppingCart.getItems()) {
                        if (cartItem.getProduct().equals(selectedProduct)) {
                            cartItem.setQuantity(cartItem.getQuantity() + 1);
                            alreadyInCart = true;
                            break;
                        }
                    }

                    // Product not in cart
                    if (!alreadyInCart) {
                        if (selectedProduct.getQuantityInStock() > 0) {
                            shoppingCart.addItem(selectedProduct, 1);
                        } else {
                            System.out.println("Product is out of stock.");
                        }
                    }

                    // Update tableView
                    cartTableView.setItems(shoppingCart.getItems());
                    cartTableView.refresh();
                    totalLabel.setText("Total: " + calculateTotal() + " din");
                }
            }
        });
        cartButton.setOnAction(event -> {
            if (!isCartVisible) {
                showCart(right, cartBox);
            } else {
                hideCart(right);
            }
        });
        logoutButton.setOnAction(event -> {
            LoginScene loginScene = new LoginScene();
            primaryStage.setScene(loginScene.createLoginScene(primaryStage));
        });
        changeInfoButton.setOnAction(event -> {

            Stage changeinfoStage = new Stage();

            BorderPane changeInfoPane = new BorderPane();
            VBox center = new VBox();

            Label changeInfoLabel = new Label("Change Information");
            changeInfoLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            GridPane gridPane = new GridPane();
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            Label firstNameLabel = new Label("First Name:");
            TextField firstNameField = new TextField(customer.getFirstName());

            Label lastNameLabel = new Label("Last Name:");
            TextField lastNameField = new TextField(customer.getLastName());

            Label emailLabel = new Label("Email:");
            TextField emailField = new TextField(customer.getEmail());

            Label streetNameLabel = new Label("Street Name:");
            TextField streetNameField = new TextField(customer.getStreetName());

            Label streetNumberLabel = new Label("Street Number:");
            TextField streetNumberField = new TextField(customer.getStreetNumber());

            Label cityLabel = new Label("City:");
            ObservableList<String> cities = FXCollections.observableArrayList(
                    "Belgrade", "Novi Sad", "Nis", "Subotica", "Kragujevac", "Krusevac", "Cacak", "Vranje", "Zrenjanin"
            );
            ChoiceBox<String> cityChoiceBox = new ChoiceBox<>(cities);

            Label phoneNumberLabel = new Label("Phone Number:");
            TextField phoneNumberField = new TextField(customer.getPhoneNumber());

            gridPane.addColumn(0, firstNameLabel, lastNameLabel, emailLabel, streetNameLabel, streetNumberLabel, cityLabel, phoneNumberLabel);
            gridPane.addColumn(1, firstNameField, lastNameField, emailField, streetNameField, streetNumberField, cityChoiceBox, phoneNumberField);

            Button saveButton = new Button("Save Changes");
            saveButton.setDisable(true);

            cityChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                saveButton.setDisable(newValue == null);
            });

            saveButton.setDisable(cityChoiceBox.getValue() == null);

            center.getChildren().addAll(titleLabel, gridPane, saveButton);
            center.setAlignment(Pos.CENTER);
            center.setSpacing(10);

            changeInfoPane.setCenter(center);

            saveButton.setOnAction(actionEvent -> {
                if (CustomerValidations.validateAllInfo(firstNameField.getText(),
                        lastNameField.getText(),
                        emailField.getText(),
                        streetNameField.getText(),
                        streetNumberField.getText(),
                        phoneNumberField.getText())){

                    DatabaseConnector.updateCustomerInfo(customer.getUsername(),
                            firstNameField.getText(),
                            lastNameField.getText(),
                            emailField.getText(),
                            streetNameField.getText(),
                            streetNumberField.getText(),
                            cityChoiceBox.getValue(),
                            phoneNumberField.getText());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Information updated successfully!");
                    alert.showAndWait();
                    changeinfoStage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Invalid information.");
                    alert.showAndWait();
                }
            });

            Scene infoScene = new Scene(changeInfoPane, 400, 400);
            changeinfoStage.setScene(infoScene);
            changeinfoStage.show();
            changeinfoStage.setTitle("Change information");
            changeinfoStage.setResizable(false);
        });

        return new Scene(root, 1600, 700);
    }

    private double calculateTotal() {
        double total = 0;
        for (CartItem cartItem : shoppingCart.getItems()) {
            total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        return total;
    }
    private void showCart(HBox right, VBox cartBox) {
        right.getChildren().add(cartBox);
        isCartVisible = true;
    }
    private void hideCart(HBox right) {
        right.getChildren().remove(0);
        isCartVisible = false;
    }
    private void filterProductList(ObservableList<Product> originalList, TableView<Product> tableView, String keyword) {
        ObservableList<Product> filteredList = FXCollections.observableArrayList();
        for (Product product : originalList) {
            if (product.getProductId().toLowerCase().contains(keyword.toLowerCase()) ||
                    product.getProductName().toLowerCase().contains(keyword.toLowerCase()) ||
                    product.getProductType().toLowerCase().contains(keyword.toLowerCase()) ||
                    product.getSupplier().toLowerCase().contains(keyword.toLowerCase()) ||
                    (product.getPrice() == Double.parseDouble(keyword))) {
                filteredList.add(product);
            }
        }

        tableView.setItems(filteredList);
    }

}
