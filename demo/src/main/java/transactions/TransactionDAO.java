package transactions;

import DB.DatabaseConnector;

import java.sql.*;

public class TransactionDAO {

    public static int addTransaction(String customerId, double totalPrice, String status) throws SQLException {
        String sql = "INSERT INTO transactions (username, total_amount, status) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, customerId);
            preparedStatement.setDouble(2, totalPrice);
            preparedStatement.setString(3, status);

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Return transaction ID
            }

        }

        return -1; // Transaction failed
    }
}
