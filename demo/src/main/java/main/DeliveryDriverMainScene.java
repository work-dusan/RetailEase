package main;

import delivery.DeliveryOrder;
import delivery.DeliveryOrderDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import users.DeliveryDriver;
import java.sql.SQLException;


public class DeliveryDriverMainScene {
    public Scene createDeliveryDriverMainScene(Stage primaryStage, DeliveryDriver loggedInDriver) throws SQLException {

        BorderPane root = new BorderPane();

        // TOP - START

        Label welcomeLabel = new Label("Welcome " + loggedInDriver.getFirstName());
        welcomeLabel.setPadding(new Insets(20));

        root.setTop(welcomeLabel);

        // TOP - END

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

        // BOTTOM - START

        Button logoutButton = new Button("Logout");
        logoutButton.setPadding(new Insets(20));

        root.setBottom(logoutButton);
        BorderPane.setAlignment(logoutButton, Pos.CENTER);

        // BOTTOM - END

        // EVENTS

        deliveries.setOnMouseClicked(event -> {

            if (event.getClickCount() == 2) {
                DeliveryOrder selectedOrder = deliveries.getSelectionModel().getSelectedItem();
                if (selectedOrder != null) {

                    Stage webViewStage = new Stage();
                    WebView webView = new WebView();
                    WebEngine webEngine = webView.getEngine();
                    BorderPane mapPane = new BorderPane();

                    String destinationAddress = selectedOrder.getDeliveryAddress();
                    String directionsUrl = "https://www.google.com/maps/dir/43.3072046,21.9473063/" + destinationAddress;
                    webEngine.load(directionsUrl);

                    Button confirmDelivery = new Button("Confirm delivery");
                    confirmDelivery.setPadding(new Insets(20));

                    mapPane.setCenter(webView);
                    mapPane.setBottom(confirmDelivery);
                    BorderPane.setAlignment(confirmDelivery, Pos.CENTER);

                    Scene webViewScene = new Scene(mapPane, 1000, 800);
                    webViewStage.setScene(webViewScene);
                    webViewStage.setTitle("Delivery Address");
                    webViewStage.show();
                }

            }




        });
        logoutButton.setOnAction(event -> {
            LoginScene scene = new LoginScene();
            primaryStage.setScene(scene.createLoginScene(primaryStage));
        });

        return new Scene(root, 1000, 600);
    }

}
