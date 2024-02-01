package products;

import DB.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static DB.DatabaseConnector.connect;

public class ProductCRUD {
    public static void addProduct(String productId, String productName, double price, int quantity,
                                  String type, String description, LocalDate expirationDate,
                                  String supplier, File productImage, String username) {
        String addProductQuery = "INSERT INTO product (PRODUCT_ID, NAME, PRICE, QUANTITY, TYPE, DESCRIPTION, EXPIRATION_DATE, SUPPLIER, IMAGE_DATA, WAREHOUSE_EMPLOYEE_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement statement = connection.prepareStatement(addProductQuery)) {

                statement.setString(1, productId);
                statement.setString(2, productName);
                statement.setDouble(3, price);
                statement.setInt(4, quantity);
                statement.setString(5, type);
                statement.setString(6, description);
                statement.setDate(7, Date.valueOf(expirationDate));
                statement.setString(8, supplier);
                statement.setBinaryStream(9, new FileInputStream(productImage), (int) productImage.length());
                statement.setString(10, username);

                statement.executeUpdate();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateProduct(String productId, String newProductName, double newPrice, int newQuantity, String newType, String newDescription, LocalDate newExpirationDate, String newSupplier, String username) {
        String updateProductQuery = "UPDATE product SET NAME=?, PRICE=?, QUANTITY=?, TYPE=?, DESCRIPTION=?, EXPIRATION_DATE=?, SUPPLIER=?, WAREHOUSE_EMPLOYEE_ID=? WHERE PRODUCT_ID=?";

        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement statement = connection.prepareStatement(updateProductQuery)) {

                statement.setString(1, newProductName);
                statement.setDouble(2, newPrice);
                statement.setInt(3, newQuantity);
                statement.setString(4, newType);
                statement.setString(5, newDescription);
                statement.setDate(6, Date.valueOf(newExpirationDate));
                statement.setString(7, newSupplier);
                statement.setString(8, username);
                statement.setString(9, productId);

                statement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Product> fetchProducts() {
        List<Product> productList = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {

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
                    Image productImage = convertToImage(resultSet.getBytes("IMAGE_DATA"));

                    Product product = new Product(productId, productName, price, quantity, type, description, expirationDate, supplier, productImage, true);
                    productList.add(product);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(productList);
    }


    public static boolean deleteProduct(String productId) {
        String deleteProductQuery = "DELETE FROM product WHERE PRODUCT_ID=?";

        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement statement = connection.prepareStatement(deleteProductQuery)) {

                statement.setString(1, productId);
                int rowsAffected = statement.executeUpdate();

                return rowsAffected > 0; // Returns true if deleted

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Image convertToImage(byte[] imageData) {
        try {
            if (imageData != null) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
                return new Image(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateProductQuantity(String productId, int newQuantity) {
        try {
            Connection connection = connect();
            String updateQuery = "UPDATE product SET quantity = ? WHERE product_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setInt(1, newQuantity);
                preparedStatement.setString(2, productId);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Product quantity updated successfully.");
                } else {
                    System.out.println("Failed to update product quantity.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Dodajte odgovarajući tretman izuzetaka i prikazivanje poruka o grešci
        }
    }
}
