package transactions;

import DB.DatabaseConnector;

import javax.swing.plaf.nimbus.State;
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

    public static String getCustomer(int transactionId){
        String sql = "SELECT username FROM transactions WHERE transaction_id = ?";
        String customerUsername = null;

        try (Connection connection = DatabaseConnector.connect()) {
            assert connection != null;
            try (PreparedStatement statement = connection.prepareStatement(sql)){

                statement.setInt(1, transactionId);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()){
                    customerUsername = resultSet.getString("username");
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customerUsername;
    }
}
