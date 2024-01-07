package DB;

import users.Customer;

import java.sql.*;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/retailease";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static final DatabaseConnector instance = new DatabaseConnector();

    private DatabaseConnector(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static DatabaseConnector getInstance(){
        return instance;
    }

    public static Connection connect(){
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void disconnect(Connection connection){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean userExists(String username) {
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void insertCustomer(Customer customer, String password) {
        String insertUserQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        String insertCustomerQuery = "INSERT INTO customer (username, first_name, last_name, email, street_name, street_number, city, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;

        try {
            connection = connect();
            connection.setAutoCommit(false);

            // Unos podataka u tabelu users
            try (PreparedStatement userStatement = connection.prepareStatement(insertUserQuery)) {
                userStatement.setString(1, customer.getUsername());
                userStatement.setString(2, password);
                userStatement.setString(3, "Customer");
                userStatement.executeUpdate();
            }

            // Unos podataka u tabelu customer
            try (PreparedStatement customerStatement = connection.prepareStatement(insertCustomerQuery)) {
                customerStatement.setString(1, customer.getUsername());
                customerStatement.setString(2, customer.getFirstName());
                customerStatement.setString(3, customer.getLastName());
                customerStatement.setString(4, customer.getEmail());
                customerStatement.setString(5, customer.getStreetName());
                customerStatement.setString(6, customer.getStreetNumber());
                customerStatement.setString(7, customer.getCity());
                customerStatement.setString(8, customer.getPhoneNumber());

                customerStatement.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    disconnect(connection);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
