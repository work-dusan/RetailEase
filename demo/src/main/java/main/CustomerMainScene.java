package main;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import products.CartItem;
import products.ImageTableCell;
import products.Product;
import users.Customer;

import java.time.LocalDate;
import java.util.Optional;

import static products.ProductCRUD.fetchProducts;

public class CustomerMainScene {

    private static ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private TableView<Product> productTableView = new TableView<>();
    private boolean isCartOpen = false;
    private static ListView<CartItem> cartListView;
    private static Label totalLabel;
    private VBox cartContents;
    private boolean isCartShowed = false;

    private static Spinner<Integer> currentSpinner;

    public Scene createCustomerMainScene(Stage primaryStage, Customer customer){

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
        VBox.setMargin(welcomeLabel, new Insets(0,0,0,50));

        top.setSpacing(20);
        top.getChildren().addAll(titlePane, welcomeLabel);

        // TOP - END

        // CENTER - START

        VBox middle = new VBox();

        Label searchLabel = new Label("Search: ");
        TextField searchTextField = new TextField();

        GridPane search = new GridPane();
        search.addRow(0, searchLabel, searchTextField);
        search.setAlignment(Pos.CENTER_RIGHT);

        BorderPane searchBorderPane = new BorderPane();
        searchBorderPane.setRight(search);
        searchBorderPane.setMaxWidth(800);

        ObservableList<Product> productList = fetchProducts();

        productTableView = new TableView<>(productList);

        TableColumn<Product, String> productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));

        TableColumn<Product, Image> productImageColumn = new TableColumn<>("Image");
        productImageColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProductImage()));
        productImageColumn.setCellFactory(col -> new ImageTableCell());

        TableColumn<Product, String> productNameColumn = new TableColumn<>("Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<Product, Double> productPriceColumn = new TableColumn<>("Price");
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, String> productTypeColumn = new TableColumn<>("Type");
        productTypeColumn.setCellValueFactory(new PropertyValueFactory<>("productType"));

        TableColumn<Product, String> productDescriptionColumn = new TableColumn<>("Description");
        productDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Product, LocalDate> productExpirationDateColumn = new TableColumn<>("Expiration date");
        productExpirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));

        TableColumn<Product, String> productSupplierColumn = new TableColumn<>("Supplier");
        productSupplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));

        productTableView.getColumns().addAll(productIdColumn, productImageColumn, productNameColumn, productPriceColumn, productTypeColumn, productDescriptionColumn, productExpirationDateColumn, productSupplierColumn);

        productTableView.setMaxSize(800, 500);

        productTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                // Dodavanje proizvoda u korpu prilikom dvostrukog klika
                Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    addToCart(selectedProduct);
                }
            }
        });

        middle.setSpacing(20);
        middle.setAlignment(Pos.CENTER);
        middle.getChildren().addAll(searchBorderPane, productTableView);

        // CENTER - END

        // RIGHT - START

        VBox placeholder = new VBox();
        placeholder.setVisible(false);

        // RIGHT - END

        // BOTTOM - START

        Button cartButton = new Button("Cart");
        cartButton.setOnAction(e -> toggleCart(placeholder));
        BorderPane.setAlignment(cartButton, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(cartButton, new Insets(0, 10, 10, 0));

        // BOTTOM - END

        root.setTop(top);
        root.setCenter(middle);
        root.setBottom(cartButton);
        root.setRight(placeholder);

        return new Scene(root, 1600, 700);
    }

    private void openCart(VBox placeholder) {
        System.out.println("Opening cart...");
        cartContents = new VBox();

        // Prikazi listu proizvoda u korpi
        cartListView = new ListView<>(cartItems);
        cartListView.setCellFactory(param -> new CartItemCell());

        if(!isCartShowed) {
            cartContents.getChildren().add(cartListView);
            isCartShowed = true;
        }

        // Inicijalizujte label za ukupnu cenu
        totalLabel = new Label("Total: " + calculateTotal());
        cartContents.getChildren().add(totalLabel);

        // Postavite TranslateTransition
        TranslateTransition showCart = new TranslateTransition(Duration.millis(500), cartContents);
        showCart.setToY(-cartContents.getHeight());
        showCart.setInterpolator(Interpolator.EASE_OUT);

        // Prikazivanje korpe sa animacijom

        isCartOpen = true;

        // Dodajte cartContents na scenu ili na određeni kontejner
        placeholder.getChildren().add(cartContents);
        placeholder.setVisible(true);
        showCart.play();
        System.out.println("Cart opened successfully.");
    }

    // Kreirajte prilagođeni prikaz za svaki element u ListView
    private static class CartItemCell extends ListCell<CartItem> {
        @Override
        protected void updateItem(CartItem cartItem, boolean empty) {
            super.updateItem(cartItem, empty);

            if (empty || cartItem == null) {
                setText(null);
            } else {
                // Prikazivanje podataka o proizvodu u korpi
                Label label = new Label(cartItem.getProduct().getProductName() + " - Total: " + cartItem.getTotalPrice());

                // Koristite TextField umesto Spinnnera
                TextField quantityTextField = new TextField(String.valueOf(cartItem.getQuantity()));
                quantityTextField.setMaxWidth(50);
                quantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        int newQuantity = Integer.parseInt(newValue);
                        updateQuantity(cartItem, newQuantity);
                    } catch (NumberFormatException e) {
                        // Ignorišite neispravan unos
                    }
                });

                // Dodajte dugme Remove
                Button removeButton = new Button("Remove");
                removeButton.setOnAction(event -> removeFromCart(cartItem));

                HBox hbox = new HBox(label, quantityTextField, removeButton);
                setGraphic(hbox);
            }
        }
    }

    private void closeCart(VBox placeholder){
        placeholder.getChildren().remove(0);
        placeholder.setVisible(false);
    }

    private static double calculateTotal() {
        return cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    private void addToCart(Product product) {
        System.out.println("Adding product to cart: " + product);

        // Proverite da li je proizvod već dodat u korpu
        Optional<CartItem> existingCartItem = cartItems.stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // Ako je proizvod već u korpi, povećajte količinu za 1
            existingCartItem.get().increaseQuantity();
        } else {
            // Inače, dodajte novu stavku u korpu
            cartItems.add(new CartItem(product, 1));
        }

        // Ažurirajte prikaz korpe
        updateCartView();
        System.out.println("Cart items after adding: " + cartItems);
    }

    private static void removeFromCart(CartItem cartItem) {
        cartItems.remove(cartItem);

        // Ažurirajte prikaz korpe
        updateCartView();
    }

    private static void updateQuantity(CartItem cartItem, Integer newQuantity) {
        cartItem.setQuantity(newQuantity);

        // Ažurirajte prikaz korpe
        updateCartView();

        // Proverite da li postoji trenutni Spinner i postavite njegovu vrednost
        if (currentSpinner != null) {
            currentSpinner.getValueFactory().setValue(newQuantity);
        }
    }

    private void toggleCart(VBox placeholder) {
        if (isCartOpen) {
            closeCart(placeholder);
            isCartOpen = false;
        } else {
            openCart(placeholder);
            isCartOpen = true;
        }
    }

    // Metoda za ažuriranje prikaza korpe
    private static void updateCartView() {
        updateTotalLabel();
        cartListView.refresh();
    }

    // Metoda za ažuriranje ListView u korpi
    private static void updateCartListView(ListView<CartItem> cartListView) {
        // Koristite getItems().setAll() umesto setItems()
        cartListView.getItems().setAll(cartItems);
        cartListView.refresh();
    }


    // Metoda za ažuriranje ukupne cene
    private static void updateTotalLabel() {
        totalLabel.setText("Total: " + calculateTotal());
    }



}
