package main;

import DB.DatabaseConnector;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import users.Cashier;
import validations.CashierValidations;

public class CashierRegistrationScene {

    public Scene createCashierRegistrationScene(Stage primaryStage, String username, String password, String salt) {
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

        Label jmbgLabel = new Label("JMBG:");
        GridPane.setConstraints(jmbgLabel, 0, 2);

        TextField jmbgInput = new TextField();
        jmbgInput.setPromptText("Enter your JMBG");
        GridPane.setConstraints(jmbgInput, 1, 2);

        Label dobLabel = new Label("Date of Birth:");
        GridPane.setConstraints(dobLabel, 0, 3);

        DatePicker dobPicker = new DatePicker();
        GridPane.setConstraints(dobPicker, 1, 3);

        Label addressLabel = new Label("Address:");
        GridPane.setConstraints(addressLabel, 0, 4);

        TextField addressInput = new TextField();
        addressInput.setPromptText("Enter your address");
        GridPane.setConstraints(addressInput, 1, 4);

        Label phoneNumberLabel = new Label("Phone Number:");
        GridPane.setConstraints(phoneNumberLabel, 0, 5);

        TextField phoneNumberInput = new TextField();
        phoneNumberInput.setPromptText("Enter your phone number");
        GridPane.setConstraints(phoneNumberInput, 1, 5);

        Label employmentDateLabel = new Label("Employment Date:");
        GridPane.setConstraints(employmentDateLabel, 0, 6);

        DatePicker employmentDatePicker = new DatePicker();
        GridPane.setConstraints(employmentDatePicker, 1, 6);

        Button registerButton = new Button("Complete Registration");
        GridPane.setConstraints(registerButton, 1, 9);

        // Dodajte logiku za registraciju kasira kada se pritisne dugme
        registerButton.setOnAction(e -> {
            String firstName = firstNameInput.getText();
            String lastName = lastNameInput.getText();
            String jmbg = jmbgInput.getText();
            String dob = dobPicker.getValue().toString();
            String address = addressInput.getText();
            String phoneNumber = phoneNumberInput.getText();
            String employmentDate = employmentDatePicker.getValue().toString();

            // Dodajte validaciju pre nego što kreirate objekat Cashier
            if (validateCashierData(firstName, lastName, jmbg, dob, address, phoneNumber, employmentDate)) {
                Cashier newCashier = new Cashier(username, firstName, lastName, jmbg, dob, address, phoneNumber, employmentDate);
                DatabaseConnector.insertCashier(newCashier, password, salt);

                successfulDialog("Registration Successful!");

                // Vratite se na ekran za prijavu nakon registracije
                LoginScene loginScene = new LoginScene();
                primaryStage.setScene(loginScene.createLoginScene(primaryStage));
            } else {
                String errorMessage = "Invalid data. Please check your input.";
                showErrorDialog(errorMessage);
            }
        });

        grid.getChildren().addAll(firstNameLabel, firstNameInput, lastNameLabel, lastNameInput, jmbgLabel, jmbgInput, dobLabel, dobPicker,
                addressLabel, addressInput, phoneNumberLabel, phoneNumberInput, employmentDateLabel, employmentDatePicker, registerButton);

        return new Scene(grid, 400, 500);
    }

    private boolean validateCashierData(String firstName, String lastName, String jmbg, String dob, String address, String phoneNumber, String employmentDate) {
        return CashierValidations.validateFirstName(firstName) &&
                CashierValidations.validateLastName(lastName) &&
                CashierValidations.validateJMBG(jmbg) &&
                CashierValidations.validateDateOfBirth(dob) &&
                CashierValidations.validateAddress(address) &&
                CashierValidations.validatePhoneNumber(phoneNumber) &&
                CashierValidations.validateEmploymentDate(employmentDate);
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
