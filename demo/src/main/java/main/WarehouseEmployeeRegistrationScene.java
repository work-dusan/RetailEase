package main;

import DB.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import users.WarehouseEmployee;
import validations.WarehouseValidations;

import java.time.LocalDate;

public class WarehouseEmployeeRegistrationScene {

    public Scene createWarehouseEmployeeRegistrationScene(Stage primaryStage, String username, String password) {
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

        Label responsibilityLabel = new Label("Responsibility:");
        GridPane.setConstraints(responsibilityLabel, 0, 7);

        TextField responsibilityInput = new TextField();
        responsibilityInput.setPromptText("Enter responsibility");
        GridPane.setConstraints(responsibilityInput, 1, 7);

        Label accessLevelLabel = new Label("Access Level:");
        GridPane.setConstraints(accessLevelLabel, 0, 8);

        // ChoiceBox za odabir nivoa pristupa
        ObservableList<String> accessLevels = FXCollections.observableArrayList(
                "Low", "High"
                // Dodajte ostale nivoe pristupa prema potrebi
        );
        ChoiceBox<String> accessLevelChoiceBox = new ChoiceBox<>(accessLevels);
        GridPane.setConstraints(accessLevelChoiceBox, 1, 8);

        Button registerButton = new Button("Complete Registration");
        GridPane.setConstraints(registerButton, 1, 9);

        // Dodajte logiku za registraciju magacionera kada se pritisne dugme
        registerButton.setOnAction(e -> {
            String firstName = firstNameInput.getText();
            String lastName = lastNameInput.getText();
            String jmbg = jmbgInput.getText();
            LocalDate dob = dobPicker.getValue();
            String address = addressInput.getText();
            String phoneNumber = phoneNumberInput.getText();
            LocalDate employmentDate = employmentDatePicker.getValue();
            String responsibility = responsibilityInput.getText();
            String accessLevel = accessLevelChoiceBox.getValue();

            if (validateWarehouseEmployeeData(firstName, lastName, jmbg, dob, address, phoneNumber, employmentDate, responsibility)) {
                WarehouseEmployee newWarehouseEmployee = new WarehouseEmployee(username, firstName, lastName, jmbg,
                        dob, address, phoneNumber, employmentDate, responsibility, accessLevel);
                DatabaseConnector.insertWarehouseEmployee(newWarehouseEmployee, password);

                // Vratite se na ekran za prijavu nakon registracije
                LoginScene loginScene = new LoginScene();
                primaryStage.setScene(loginScene.createLoginScene(primaryStage));
            } else {
                String errorMessage = "Invalid data. Please check your input.";
                showErrorDialog(errorMessage);
            }
        });

        accessLevelChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            registerButton.setDisable(newValue == null);
        });

        grid.getChildren().addAll(firstNameLabel, firstNameInput, lastNameLabel, lastNameInput, jmbgLabel, jmbgInput,
                dobLabel, dobPicker, addressLabel, addressInput, phoneNumberLabel, phoneNumberInput,
                employmentDateLabel, employmentDatePicker, responsibilityLabel, responsibilityInput,
                accessLevelLabel, accessLevelChoiceBox, registerButton);

        return new Scene(grid, 400, 550);
    }

    private boolean validateWarehouseEmployeeData(String firstName, String lastName, String jmbg, LocalDate dob,
                                                  String address, String phoneNumber, LocalDate employmentDate,
                                                  String responsibility) {
        return WarehouseValidations.validateFirstName(firstName) &&
                WarehouseValidations.validateLastName(lastName) &&
                WarehouseValidations.validateJMBG(jmbg) &&
                WarehouseValidations.validateDateOfBirth(dob) &&
                WarehouseValidations.validateAddress(address) &&
                WarehouseValidations.validatePhoneNumber(phoneNumber) &&
                WarehouseValidations.validateEmploymentDate(employmentDate) &&
                WarehouseValidations.validateResponsibility(responsibility);
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
