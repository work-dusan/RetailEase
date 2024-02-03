package transactions;

import DB.DatabaseConnector;
import products.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionItemDAO {

    public static void addTransactionItems(int transactionId, CartItem item) {
        String sql = "INSERT INTO transaction_items (transaction_id, product_id, quantity) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, transactionId);
            preparedStatement.setString(2, item.getProduct().getProductId());
            preparedStatement.setInt(3, item.getQuantity());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Dodajte metode za dohvat informacija o stavkama transakcija ako je potrebno
}

