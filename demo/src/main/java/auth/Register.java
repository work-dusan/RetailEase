package auth;

import DB.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.control.Alert;

public class Register {

    public static void handleRegistration(String username, String password, String type) {
        Connection connection = DatabaseConnector.connect();

        if (connection != null) {
            if (checkIfUserExists(connection, username)) {
                showAlert("Registration Failed", "Username already exists. Please choose another username.");
            } else {
                // Unesi novog korisnika u bazu
                if (insertUser(connection, username, password, type)) {
                    showAlert("Registration Successful", "Account created successfully.");
                } else {
                    showAlert("Registration Failed", "Failed to create an account. Please try again.");
                }
            }

            // Diskonektuj se iz baze
            DatabaseConnector.disconnect(connection);
        } else {
            showAlert("Error", "Could not connect to the database.");
        }
    }

    // Metoda za proveru da li korisnik sa datim korisničkim imenom već postoji
    private static boolean checkIfUserExists(Connection connection, String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metoda za unos novog korisnika u bazu
    private static boolean insertUser(Connection connection, String username, String password, String userType) {
        String query = "INSERT INTO users (username, password, user_type) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, userType);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
