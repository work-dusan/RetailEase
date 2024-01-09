package main;

import DB.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import users.DeliveryDriver;
import validations.DeliveryDriverValidations;

import java.time.LocalDate;

public class DeliveryDriverRegisterScene {

    public Scene createDeliveryDriverRegisterScene(Stage primaryStage, String username, String password) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label firstNameLabel = new Label("First Name:");
        GridPane.setConstraints(firstNameLabel, 0, 0);

        TextField firstNameInput = new TextField();
        firstNameInput.setPromptText("Enter first name");
        GridPane.setConstraints(firstNameInput, 1, 0);

        Label lastNameLabel = new Label("Last Name:");
        GridPane.setConstraints(lastNameLabel, 0, 1);

        TextField lastNameInput = new TextField();
        lastNameInput.setPromptText("Enter last name");
        GridPane.setConstraints(lastNameInput, 1, 1);

        Label jmbgLabel = new Label("JMBG:");
        GridPane.setConstraints(jmbgLabel, 0, 2);

        TextField jmbgInput = new TextField();
        jmbgInput.setPromptText("Enter JMBG");
        GridPane.setConstraints(jmbgInput, 1, 2);

        Label dobLabel = new Label("Date of Birth:");
        GridPane.setConstraints(dobLabel, 0, 3);

        DatePicker dobPicker = new DatePicker();
        GridPane.setConstraints(dobPicker, 1, 3);

        Label addressLabel = new Label("Address:");
        GridPane.setConstraints(addressLabel, 0, 4);

        TextField addressInput = new TextField();
        addressInput.setPromptText("Enter address");
        GridPane.setConstraints(addressInput, 1, 4);

        Label phoneNumberLabel = new Label("Phone Number:");
        GridPane.setConstraints(phoneNumberLabel, 0, 5);

        TextField phoneNumberInput = new TextField();
        phoneNumberInput.setPromptText("Enter phone number");
        GridPane.setConstraints(phoneNumberInput, 1, 5);

        Label employmentDateLabel = new Label("Employment Date:");
        GridPane.setConstraints(employmentDateLabel, 0, 6);

        DatePicker employmentDatePicker = new DatePicker();
        GridPane.setConstraints(employmentDatePicker, 1, 6);

        Label licenseNumberLabel = new Label("License Number:");
        GridPane.setConstraints(licenseNumberLabel, 0, 7);

        TextField licenseNumberInput = new TextField();
        licenseNumberInput.setPromptText("Enter license number");
        GridPane.setConstraints(licenseNumberInput, 1, 7);

        Label vehicleInfoLabel = new Label("Vehicle Info:");
        GridPane.setConstraints(vehicleInfoLabel, 0, 8);

        TextField vehicleInfoInput = new TextField();
        vehicleInfoInput.setPromptText("Enter vehicle info");
        GridPane.setConstraints(vehicleInfoInput, 1, 8);

        Button registerButton = new Button("Complete Registration");
        GridPane.setConstraints(registerButton, 1, 9);

        // Dodajte logiku za registraciju dostavljača kada se pritisne dugme
        registerButton.setOnAction(e -> {
            String firstName = firstNameInput.getText();
            String lastName = lastNameInput.getText();
            String jmbg = jmbgInput.getText();
            LocalDate dob = dobPicker.getValue();
            String address = addressInput.getText();
            String phoneNumber = phoneNumberInput.getText();
            LocalDate employmentDate = employmentDatePicker.getValue();
            String licenseNumber = licenseNumberInput.getText();
            String vehicleInfo = vehicleInfoInput.getText();

            if (validateDeliveryDriverData(firstName, lastName, jmbg, dob, address, phoneNumber, employmentDate, licenseNumber, vehicleInfo)) {
                DeliveryDriver newDeliveryDriver = new DeliveryDriver(username, firstName, lastName, jmbg,
                        dob, address, phoneNumber, employmentDate, licenseNumber, vehicleInfo);
                DatabaseConnector.insertDeliveryDriver(newDeliveryDriver, password);

                successfulDialog("Registration Sucessful!");

                // Vratite se na ekran za prijavu nakon registracije
                LoginScene loginScene = new LoginScene();
                primaryStage.setScene(loginScene.createLoginScene(primaryStage));
            } else {
                String errorMessage = "Invalid data. Please check your input.";
                showErrorDialog(errorMessage);
            }
        });

        grid.getChildren().addAll(firstNameLabel, firstNameInput, lastNameLabel, lastNameInput, jmbgLabel, jmbgInput,
                dobLabel, dobPicker, addressLabel, addressInput, phoneNumberLabel, phoneNumberInput,
                employmentDateLabel, employmentDatePicker, licenseNumberLabel, licenseNumberInput,
                vehicleInfoLabel, vehicleInfoInput, registerButton);

        return new Scene(grid, 400, 600);
    }

    private boolean validateDeliveryDriverData(String firstName, String lastName, String jmbg, LocalDate dob,
                                               String address, String phoneNumber, LocalDate employmentDate,
                                               String licenseNumber, String vehicleInfo) {
        return DeliveryDriverValidations.validateFirstName(firstName) &&
                DeliveryDriverValidations.validateLastName(lastName) &&
                DeliveryDriverValidations.validateJMBG(jmbg) &&
                DeliveryDriverValidations.validateDateOfBirth(dob) &&
                DeliveryDriverValidations.validateAddress(address) &&
                DeliveryDriverValidations.validatePhoneNumber(phoneNumber) &&
                DeliveryDriverValidations.validateEmploymentDate(employmentDate) &&
                DeliveryDriverValidations.validateLicensePlate(licenseNumber) &&
                DeliveryDriverValidations.validateVehicleInfo(vehicleInfo);
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