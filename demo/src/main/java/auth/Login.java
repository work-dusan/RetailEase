package auth;

import DB.DatabaseConnector;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import main.WarehouseEmployeeMainScene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

        public static void handleLogin(Stage primaryStage, String username, String password) {
            Connection connection = DatabaseConnector.connect();

            if (connection != null) {
                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        String role = resultSet.getString("role");
                        System.out.println(role);
                        showAlert("Login Successful", "Welcome, " + username + "!");
                        if (role.equals("WarehouseEmployee")){
                            WarehouseEmployeeMainScene mainScreen = new WarehouseEmployeeMainScene();
                            primaryStage.setScene(mainScreen.createWarehouseEmployeeMainScene(primaryStage));
                        }
                    } else {
                        showAlert("Login Failed", "Invalid username or password.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnector.disconnect(connection);
                }
            } else {
                showAlert("Error", "Could not connect to the database.");
            }
        }

        // Metoda za prikazivanje JavaFX Alert prozora
        private static void showAlert(String title, String content) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }
}

