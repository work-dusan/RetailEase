package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import validations.CustomerValidations;
import users.Customer;
import DB.DatabaseConnector;

public class CustomerRegistrationScene {

    public Scene createCustomerRegistrationScene(Stage primaryStage, String username, String password) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label firstNameLabel = new Label("First Name:");
        GridPane.setConstraints(firstNameLabel, 0, 0);

        TextField firstNameInput = new TextField();
        firstNameInput.setPromptText("Enter your first name");
        GridPane.setConstraints(firstNameInput, 1, 0);

        Label lastNameLabel = new Label("Last Name:");
        GridPane.setConstraints(lastNameLabel, 0, 1);

        TextField lastNameInput = new TextField();
        lastNameInput.setPromptText("Enter your last name");
        GridPane.setConstraints(lastNameInput, 1, 1);

        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 0, 2);

        TextField emailInput = new TextField();
        emailInput.setPromptText("Enter your email");
        GridPane.setConstraints(emailInput, 1, 2);

        Label streetLabel = new Label("Street:");
        GridPane.setConstraints(streetLabel, 0, 3);

        TextField streetInput = new TextField();
        streetInput.setPromptText("Enter your street");
        GridPane.setConstraints(streetInput, 1, 3);

        Label numberLabel = new Label("Number:");
        GridPane.setConstraints(numberLabel, 0, 4);

        TextField numberInput = new TextField();
        numberInput.setPromptText("Enter your street number");
        GridPane.setConstraints(numberInput, 1, 4);

        Label cityLabel = new Label("City:");
        GridPane.setConstraints(cityLabel, 0, 5);

        // ChoiceBox za odabir grada
        ObservableList<String> cities = FXCollections.observableArrayList(
                "Belgrade", "Novi Sad", "Nis", "Subotica", "Kragujevac", "Krusevac", "Cacak", "Vranje", "Zrenjanin"
                // Dodajte ostale gradove prema potrebi
        );
        ChoiceBox<String> cityChoiceBox = new ChoiceBox<>(cities);
        GridPane.setConstraints(cityChoiceBox, 1, 5);

        Label phoneLabel = new Label("Phone Number:");
        GridPane.setConstraints(phoneLabel, 0, 6);

        TextField phoneInput = new TextField();
        phoneInput.setPromptText("Enter your phone number");
        GridPane.setConstraints(phoneInput, 1, 6);

        Button registerButton = new Button("Complete Registration");
        GridPane.setConstraints(registerButton, 1, 7);

        // Dodajte logiku za registraciju kupca kada se pritisne dugme
        registerButton.setOnAction(e -> {
            String firstName = firstNameInput.getText();
            String lastName = lastNameInput.getText();
            String email = emailInput.getText();
            String streetName = streetInput.getText();
            String streetNumber = numberInput.getText();
            String phoneNumber = phoneInput.getText();

            if (CustomerValidations.validateEmail(email) &&
                    CustomerValidations.validateStreetName(streetName) &&
                    CustomerValidations.validateStreetNumber(streetNumber) &&
                    CustomerValidations.validatePhoneNumber(phoneNumber) &&
                    CustomerValidations.validateName(firstName) &&
                    CustomerValidations.validateName(lastName)) {

                Customer newCustomer = new Customer(username, firstName, lastName, email, streetName, streetNumber, cityChoiceBox.getValue(), phoneNumber);
                DatabaseConnector.insertCustomer(newCustomer, password);

                successfulDialog("Registration Successful!");

                LoginScene loginScene = new LoginScene();
                primaryStage.setScene(loginScene.createLoginScene(primaryStage));
            } else {
                String errorMessage = "Invalid data. Please check your input.";
                showErrorDialog(errorMessage);
            }

        });

        grid.getChildren().addAll(firstNameLabel, firstNameInput, lastNameLabel, lastNameInput, emailLabel, emailInput, streetLabel, streetInput, numberLabel, numberInput, cityLabel, cityChoiceBox, phoneLabel, phoneInput, registerButton);

        return new Scene(grid, 400, 400);
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void successfulDialog(String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
