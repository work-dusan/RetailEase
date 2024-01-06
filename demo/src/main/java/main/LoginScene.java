package main;

import auth.Login;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginScene {

    public Scene createLoginScene(Stage primaryStage){
        primaryStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);

        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Enter your username");
        GridPane.setConstraints(usernameInput, 1, 0);

        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);

        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Enter your password");
        GridPane.setConstraints(passwordInput, 1, 1);

        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 2);

        Label registerLabel = new Label("Don't have an account?");
        GridPane.setConstraints(registerLabel, 0, 3);

        Button registerButton = new Button("Register");
        GridPane.setConstraints(registerButton, 1, 3);

        // ---------------       EVENTS         -----------------------

        loginButton.setOnAction(e -> Login.handleLogin(usernameInput.getText(), passwordInput.getText()));
        registerButton.setOnAction(e -> {
            RegisterScene registerScene = new RegisterScene();
            primaryStage.setScene(registerScene.createRegisterScene(primaryStage));
        });

        grid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton,
                registerLabel, registerButton);

        return new Scene(grid,350, 200);
    }

}
