package products;

import DB.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static DB.DatabaseConnector.connect;

public class ProductCRUD {
    public static void updateProduct(String productId, String newProductName, double newPrice, int newQuantity, String newType, String newDescription, LocalDate newExpirationDate, String newSupplier) {
        String updateProductQuery = "UPDATE product SET NAME=?, PRICE=?, QUANTITY=?, TYPE=?, DESCRIPTION=?, EXPIRATION_DATE=?, SUPPLIER=? WHERE PRODUCT_ID=?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(updateProductQuery)) {

            statement.setString(1, newProductName);
            statement.setDouble(2, newPrice);
            statement.setInt(3, newQuantity);
            statement.setString(4, newType);
            statement.setString(5, newDescription);
            statement.setDate(6, java.sql.Date.valueOf(newExpirationDate));
            statement.setString(7, newSupplier);
            statement.setString(8, productId);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Product> fetchProducts() {
        List<Product> productList = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect();
             Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM product";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String productId = resultSet.getString("PRODUCT_ID");
                String productName = resultSet.getString("NAME");
                double price = resultSet.getDouble("PRICE");
                int quantity = resultSet.getInt("QUANTITY");
                String type = resultSet.getString("TYPE");
                String description = resultSet.getString("DESCRIPTION");
                LocalDate expirationDate = resultSet.getDate("EXPIRATION_DATE").toLocalDate();
                String supplier = resultSet.getString("SUPPLIER");

                Product product = new Product(productId, productName, price, quantity, type, description, expirationDate, supplier);
                productList.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(productList);
    }

    public static boolean deleteProduct(String productId) {
        String deleteProductQuery = "DELETE FROM product WHERE PRODUCT_ID=?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(deleteProductQuery)) {

            statement.setString(1, productId);
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0; // Vraća true ako je neki red obrisan, inače false

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
