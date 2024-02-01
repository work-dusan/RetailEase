package main;

import DB.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import products.CartItem;
import products.Product;
import products.ProductCRUD;
import products.ShoppingCart;
import transactions.TransactionDAO;
import transactions.TransactionItemDAO;
import users.Customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CheckoutScene {

    public static void createCheckoutStage(ShoppingCart shoppingCart, Customer customer, Double totalPrice) {
        Stage checkoutStage = new Stage();

        Label addressLabel = new Label("Delivery address:");
        TextField addressTextField = new TextField();
        addressTextField.setMaxWidth(300);
        addressTextField.setPromptText("Type an address...");

        ContextMenu contextMenu = new ContextMenu();

        Label paymentInfoLabel = new Label("Payment info:");
        RadioButton cashRadioButton = new RadioButton("Cash on delivery");
        RadioButton cardRadioButton = new RadioButton("Credit card");

        ToggleGroup paymentToggleGroup = new ToggleGroup();
        cashRadioButton.setToggleGroup(paymentToggleGroup);
        cardRadioButton.setToggleGroup(paymentToggleGroup);
        
        TextField cardNumberTextField = new TextField();
        cardNumberTextField.setPromptText("Card Number");

        TextField expirationDateTextField = new TextField();
        expirationDateTextField.setPromptText("Expiration Date (MM/YY)");

        TextField cvvTextField = new TextField();
        cvvTextField.setPromptText("CVV");

        HBox cardInfoBox = new HBox(cardNumberTextField, expirationDateTextField, cvvTextField);
        cardInfoBox.setSpacing(10);
        cardInfoBox.setAlignment(Pos.CENTER);
        cardInfoBox.setVisible(false);

        Label deliveryTimeLabel = new Label("Delivery Time:");
        ComboBox<String> deliveryTimeComboBox = new ComboBox<>();
        deliveryTimeComboBox.getItems().addAll("10:00 AM", "12:00 PM", "2:00 PM", "4:00 PM", "6:00 PM");
        deliveryTimeComboBox.setPromptText("Select delivery time");

        Label requestLabel = new Label("Special request");
        TextArea specialOrderTextArea = new TextArea();
        specialOrderTextArea.maxWidth(300);
        specialOrderTextArea.setPromptText("Type your request here...");

        Button confirmPaymentButton = new Button("Confirm Payment");

        VBox root = new VBox(addressLabel, addressTextField, paymentInfoLabel, cashRadioButton, cardRadioButton, cardInfoBox, deliveryTimeLabel, deliveryTimeComboBox, requestLabel, specialOrderTextArea, confirmPaymentButton);
        root.setAlignment(Pos.CENTER);
        
        cashRadioButton.setOnAction(event -> {
            cardInfoBox.setVisible(false);
        });
        cardRadioButton.setOnAction(event -> {
            cardInfoBox.setVisible(cardRadioButton.isSelected());
        });

        // Show address into context menu
        addressTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                List<String> autoCompleteResults = getAutoCompleteResults(newValue);
                showAutoCompleteResults(contextMenu, autoCompleteResults, addressTextField);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        checkoutStage.addEventFilter(WindowEvent.WINDOW_SHOWING, event -> {
            contextMenu.hide();
        });

        confirmPaymentButton.setOnAction(event -> {
            try {

                int transactionId = 0;
                
                if (cardRadioButton.isSelected()){
                    transactionId = TransactionDAO.addTransaction(customer.getUsername(), totalPrice, "Paid");
                } else if (cashRadioButton.isSelected()){
                    transactionId = TransactionDAO.addTransaction(customer.getUsername(), totalPrice, "Pending");
                }

                // Update quantity and add in transaction
                for (CartItem cartItem : shoppingCart.getItems()) {

                    // Update quantity in stock
                    if (reduceProductQuantityInWarehouse(cartItem)) {
                        // add product to transaction
                        TransactionItemDAO.addTransactionItems(transactionId, cartItem);
                    } else {
                        // Error = transaction delete
                        System.out.println("Insufficient quantity in the warehouse.");
                        return;
                    }
                }

                checkoutStage.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        root.setPadding(new Insets(20));

        Scene checkoutScene = new Scene(root, 400, 400);
        checkoutStage.setScene(checkoutScene);
        checkoutStage.setTitle("Checkout");
        checkoutStage.show();
    }

    private static List<String> getAutoCompleteResults(String input) throws IOException {
        // OSM Nominatim servis za pretragu adresa
        String apiUrl = "https://nominatim.openstreetmap.org/search";
        String formatParam = "format=json";
        String addressDetailsParam = "addressdetails=1";
        String limitParam = "limit=5"; // Možete prilagoditi broj rezultata

        String query = URLEncoder.encode(input, "UTF-8");

        String requestUrl = String.format("%s?%s&%s&%s&q=%s", apiUrl, formatParam, addressDetailsParam, limitParam, query);

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(requestUrl);

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        reader.close();
        
        List<String> addresses = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(result.toString());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject addressObject = jsonArray.getJSONObject(i);
            String display_name = addressObject.getString("display_name");
            addresses.add(display_name);
        }

        return addresses;
    }

    private static void showAutoCompleteResults(ContextMenu contextMenu, List<String> results, TextField textField) {
        contextMenu.getItems().clear();

        if (!results.isEmpty()) {
            ObservableList<CustomMenuItem> items = FXCollections.observableArrayList();

            for (String result : results) {
                CustomMenuItem item = new CustomMenuItem(new Label(result));
                item.setOnAction(event -> {
                    textField.setText(result);
                    contextMenu.hide();
                });
                items.add(item);
            }

            contextMenu.getItems().addAll(items);
            contextMenu.show(textField, Side.BOTTOM, 0, 0);
        }
    }
    private static boolean reduceProductQuantityInWarehouse(CartItem cartItem) {
        Product product = cartItem.getProduct();
        int currentQuantityInWarehouse = product.getQuantityInStock();
        int quantityInCart = cartItem.getQuantity();

        if (currentQuantityInWarehouse >= quantityInCart) {
            product.setQuantityInStock(currentQuantityInWarehouse - quantityInCart);
            ProductCRUD.updateProductQuantity(product.getProductId(), product.getQuantityInStock());
            return true;
        } else {
            System.out.println("Insufficient quantity in the warehouse.");
            return false;
        }
    }

}
