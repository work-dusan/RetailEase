package delivery;

import DB.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryOrderDAO {
    public static void addDelivery(DeliveryOrder deliveryOrder){
        String sql = "INSERT INTO delivery_order (adress, apartment_number, delivery_time, special_request, status, transaction_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnector.connect()) {
            assert connection != null;
            try (PreparedStatement statement = connection.prepareStatement(sql)){

                statement.setString(1, deliveryOrder.getDeliveryAddress());
                statement.setString(2, deliveryOrder.getApartmentNumber());
                statement.setString(3, deliveryOrder.getDeliveryTime());
                statement.setString(4, deliveryOrder.getSpecialRequest());
                statement.setString(5, deliveryOrder.getStatus());
                statement.setInt(6, deliveryOrder.getTransactionId());

                statement.executeUpdate();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<DeliveryOrder> fetchOrders() throws SQLException {

        List<DeliveryOrder> orderList = new ArrayList<>();
        String sql = "SELECT * FROM `delivery_order` WHERE delivery_order.status LIKE \"To be delivered\";";

        try (Connection connection = DatabaseConnector.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()){

                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()){
                    String deliveryAddress = resultSet.getString("adress");
                    String apartmentNumber = resultSet.getString("apartment_number");
                    String deliveryTime = resultSet.getString("delivery_time");
                    String specialRequest = resultSet.getString("special_request");
                    String status = resultSet.getString("status");
                    int transactionId = resultSet.getInt("transaction_id");

                    DeliveryOrder deliveryOrder = new DeliveryOrder(deliveryAddress, apartmentNumber, deliveryTime, specialRequest, status, transactionId);
                    orderList.add(deliveryOrder);
                }

            }
        }

        return FXCollections.observableArrayList(orderList);
    }

    public static void updateDeliveryOrder(int transactionId, String driver, String status) throws SQLException {
        try (Connection connection = DatabaseConnector.connect()){

            String sql = "UPDATE delivery_order SET status = ?, delivery_driver = ? WHERE transaction_id = ?";

            assert connection != null;
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1,status);
                statement.setString(2, driver);
                statement.setInt(3, transactionId);
            }
        }
    }
}
