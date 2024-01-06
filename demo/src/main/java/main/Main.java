package main;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("RetailEase");

        LoginScene loginScene = new LoginScene();
        primaryStage.setScene(loginScene.createLoginScene(primaryStage));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
