package main;

import delivery.DeliveryOrder;
import delivery.DeliveryOrderDAO;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import users.DeliveryDriver;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.net.URLEncoder;


public class DeliveryDriverMainScene {
    public Scene createDeliveryDriverMainScene(Stage primaryStage, DeliveryDriver loggedInDriver) throws SQLException {

        BorderPane root = new BorderPane();

        // CENTER - START

        TableView<DeliveryOrder> deliveries = new TableView<>(DeliveryOrderDAO.fetchOrders());

        TableColumn<DeliveryOrder, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryAddress"));
        addressColumn.setMaxWidth(350);
        addressColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<DeliveryOrder, String> apartmentNumberColumn = new TableColumn<>("apartment number");
        apartmentNumberColumn.setCellValueFactory(new PropertyValueFactory<>("apartmentNumber"));
        apartmentNumberColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<DeliveryOrder, String> deliveryTimeColumn = new TableColumn<>("Delivery Time");
        deliveryTimeColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));
        deliveryTimeColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<DeliveryOrder, String> specialRequestColumn = new TableColumn<>("Special request");
        specialRequestColumn.setCellValueFactory(new PropertyValueFactory<>("specialRequest"));
        specialRequestColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<DeliveryOrder, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER;");

        deliveries.getColumns().addAll(addressColumn, apartmentNumberColumn, deliveryTimeColumn, specialRequestColumn, statusColumn);
        deliveries.setMaxSize(800, 500);

        root.setCenter(deliveries);

        // CENTER - END

        // EVENTS

        deliveries.setOnMouseClicked(event -> {
            double deliveryLatitude = 43.3210;
            double deliveryLongitude = 21.8946;

            showWebView("https://www.google.com/maps/@43.3292659,21.8262318,15z?entry=ttu");

            if (event.getClickCount() == 2) {
                DeliveryOrder selectedOrder = deliveries.getSelectionModel().getSelectedItem();
                if (selectedOrder != null) {
                    String destinationAddress = selectedOrder.getDeliveryAddress();

                    // Kodiranje adrese kako bi se izbegli specijalni karakteri
                    String encodedAddress;
                    encodedAddress = URLEncoder.encode(destinationAddress, StandardCharsets.UTF_8);

                    // Koristi OSM Nominatim API za geokodiranje adrese i dobijanje geografskih koordinata
                    String geocodingUrl = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedAddress;
                    JSONParser parser = new JSONParser();
                    JSONArray results;
                    try {
                        results = (JSONArray) parser.parse(new InputStreamReader(new URL(geocodingUrl).openStream()));
                    } catch (IOException | ParseException e) {
                        throw new RuntimeException(e);
                    }

                    String directionsUrl = null;
                    if (!results.isEmpty()) {
                        JSONObject firstResult = (JSONObject) results.get(0);
                        double destinationLatitude = Double.parseDouble(firstResult.get("lat").toString());
                        double destinationLongitude = Double.parseDouble(firstResult.get("lon").toString());

                        // Formiranje URL-a za dobijanje informacija o putanji od dostavljača do odredišta
//                        directionsUrl = "https://www.openstreetmap.org/directions?engine=fossgis_osrm_car&route="
//                                + deliveryLatitude + "%2C" + deliveryLongitude + "%3B"
//                                + destinationLatitude + "%2C" + destinationLongitude
//                                + "#map=14/" + ((deliveryLatitude + destinationLatitude) / 2) + "/" + ((deliveryLongitude + destinationLongitude) / 2);


                    } else {
                        System.out.println("Nije pronađena geografska lokacija za adresu: " + destinationAddress);
                    }


                }
            }

        });

        return new Scene(root, 1000, 600);
    }

    private static void showWebView(String url) {
        Stage webViewStage = new Stage();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(url);

        BorderPane root = new BorderPane();
        root.setCenter(webView);

        Scene webViewScene = new Scene(root, 800, 600);
        webViewStage.setScene(webViewScene);
        webViewStage.setTitle("Web View");
        webViewStage.show();
    }
}
