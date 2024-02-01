package delivery;

import DB.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeliveryOrderDAO {
    public static void addDelivery(DeliveryOrder deliveryOrder){
        String sql = "INSERT INTO delivery_order (adress, apartment_number, delivery_time, special_request, status, transaction_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, deliveryOrder.getDeliveryAddress());
            statement.setString(2, deliveryOrder.getApartmentNumber());
            statement.setString(3, deliveryOrder.getDeliveryTime());
            statement.setString(4, deliveryOrder.getSpecialRequest());
            statement.setString(5, deliveryOrder.getStatus());
            statement.setInt(6, deliveryOrder.getTransactionId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
