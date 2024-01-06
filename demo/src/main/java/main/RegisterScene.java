package main;

import DB.DatabaseConnector;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegisterScene {

    private TextField usernameInput;
    private PasswordField passwordInput;
    private PasswordField confirmPasswordInput;
    private ChoiceBox<String> roleChoiceBox;
    private Button registerButton;

    public Scene createRegisterScene(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);

        usernameInput = new TextField();
        usernameInput.setPromptText("Enter your username");
        GridPane.setConstraints(usernameInput, 1, 0);

        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);

        passwordInput = new PasswordField();
        passwordInput.setPromptText("Enter your password");
        GridPane.setConstraints(passwordInput, 1, 1);

        Label confirmPasswordLabel = new Label("Confirm Password:");
        GridPane.setConstraints(confirmPasswordLabel, 0, 2);

        confirmPasswordInput = new PasswordField();
        confirmPasswordInput.setPromptText("Confirm your password");
        GridPane.setConstraints(confirmPasswordInput, 1, 2);

        Label roleLabel = new Label("Select Role:");
        GridPane.setConstraints(roleLabel, 0, 3);

        roleChoiceBox = new ChoiceBox<>();
        roleChoiceBox.getItems().addAll("Cashier", "Customer", "Storekeeper", "Delivery Person");
        roleChoiceBox.setValue("Select Role");
        GridPane.setConstraints(roleChoiceBox, 1, 3);

        registerButton = new Button("Next");
        GridPane.setConstraints(registerButton, 1, 4);
        registerButton.setDisable(true);

        Button backButton = new Button("Back to Login");
        GridPane.setConstraints(backButton, 1, 5);

        backButton.setOnAction(e -> {
            LoginScene loginScene = new LoginScene();
            primaryStage.setScene(loginScene.createLoginScene(primaryStage));
        });

        grid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, confirmPasswordLabel,
                confirmPasswordInput, roleLabel, roleChoiceBox, registerButton, backButton);

        usernameInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        passwordInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        confirmPasswordInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        roleChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> validateInputs());

        registerButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String role = roleChoiceBox.getValue();
            if (!DatabaseConnector.userExists(username)) {
                if (role.equals("Customer")) {
                    CustomerRegistrationScene customerRegistrationScene = new CustomerRegistrationScene();
                    primaryStage.setScene(customerRegistrationScene.createCustomerRegistrationScene(primaryStage, username, passwordInput.getText()));
                }
            } else {
                showAlert("Error", "Username already exists. Please choose another username.");
            }
        });

        return new Scene(grid, 300, 300);
    }

    private void validateInputs() {
        boolean usernameValid = validateUsername(usernameInput.getText());
        boolean passwordValid = validatePassword(passwordInput.getText());
        boolean confirmPasswordValid = validateConfirmPassword(passwordInput.getText(), confirmPasswordInput.getText());
        boolean roleValid = validateRole(roleChoiceBox.getValue());

        registerButton.setDisable(!(usernameValid && passwordValid && confirmPasswordValid && roleValid));
    }

    private boolean validateUsername(String username) {
        return username.length() >= 5 && username.length() <= 20 && username.matches("[a-zA-Z0-9]+");
    }

    private boolean validatePassword(String password) {
        return password.length() >= 7 && password.length() <= 30 && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$");
    }

    private boolean validateConfirmPassword(String originalPassword, String confirmPassword) {
        return originalPassword.equals(confirmPassword);
    }

    private boolean validateRole(String role) {
        return !role.equals("Select Role");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
