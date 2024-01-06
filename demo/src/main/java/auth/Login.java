package auth;

import DB.DatabaseConnector;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

        public static void handleLogin(String username, String password) {
            Connection connection = DatabaseConnector.connect();

            if (connection != null) {
                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        showAlert("Login Successful", "Welcome, " + username + "!");
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

        // Primer funkcije za proveru korisničkih podataka (treba prilagoditi prema vašoj bazi podataka)
        private static boolean checkUserCredentials(String username, String password) {
            // Ovde treba implementirati logiku za proveru korisničkih podataka u bazi podataka
            // Vratiti true ako su podaci tačni, inače false
            // Ovo je samo primer, stvarna implementacija zavisi od vaše baze podataka
            return username.equals("korisnik") && password.equals("sifra");
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

