package auth;

import DB.DatabaseConnector;
import encryptor.SHA256;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import main.CustomerMainScene;
import main.WarehouseEmployeeMainScene;
import users.Customer;
import users.WarehouseEmployee;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    public static void handleLogin(Stage primaryStage, String username, String password) {
        Connection connection = DatabaseConnector.connect();

        if (connection != null) {
            String query = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String storedPasswordHash = resultSet.getString("password");
                    String storedSalt = resultSet.getString("salt");

                    try {
                        // Generate hash
                        String hashedPassword = SHA256.hashPassword(password, storedSalt);

                        if (hashedPassword.equals(storedPasswordHash)) {
                            String role = resultSet.getString("role");
                            showAlert("Login Successful", "Welcome, " + username + "!");
                            if (role.equals("WarehouseEmployee")) {
                                WarehouseEmployee loggedInEmployee = WarehouseEmployee.findEmployee(username);
                                WarehouseEmployeeMainScene mainScreen = new WarehouseEmployeeMainScene();
                                primaryStage.setScene(mainScreen.createWarehouseEmployeeMainScene(primaryStage, loggedInEmployee));
                                primaryStage.setResizable(false);
                                primaryStage.setTitle("Warehouse Employee");
                            } else if (role.equals("Customer")) {
                                Customer loggedInCustomer = Customer.findCustomer(username);
                                CustomerMainScene mainScene = new CustomerMainScene();
                                primaryStage.setScene(mainScene.createCustomerMainScene(primaryStage, loggedInCustomer));
                                primaryStage.setResizable(false);
                                primaryStage.setTitle("Customer");
                            }
                        } else {
                            showAlert("Login Failed", "Invalid username or password.");
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
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

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
