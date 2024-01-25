package DB;

import users.Cashier;
import users.Customer;
import users.DeliveryDriver;
import users.WarehouseEmployee;

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

    public static void insertCustomer(Customer customer, String password, String salt) {
        String insertUserQuery = "INSERT INTO users (username, password, salt, role) VALUES (?, ?, ?, ?)";
        String insertCustomerQuery = "INSERT INTO customer (username, first_name, last_name, email, street_name, street_number, city, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;

        try {
            connection = connect();
            connection.setAutoCommit(false);

            // Unos podataka u tabelu users
            try (PreparedStatement userStatement = connection.prepareStatement(insertUserQuery)) {
                userStatement.setString(1, customer.getUsername());
                userStatement.setString(2, password);
                userStatement.setString(3, salt);
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
    public static void insertCashier(Cashier cashier, String password, String salt) {
        String insertUserQuery = "INSERT INTO users (username, password, salt, role) VALUES (?, ?, ?, ?)";
        String insertCashierQuery = "INSERT INTO cashier (username, first_name, last_name, jmbg, date_of_birth, address, phone_number, employment_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;

        try {
            connection = connect();
            connection.setAutoCommit(false);

            // Unos podataka u tabelu users
            try (PreparedStatement userStatement = connection.prepareStatement(insertUserQuery)) {
                userStatement.setString(1, cashier.getUsername());
                // Ovde postavite šifrovanu verziju lozinke (npr. sa bcrypt)
                userStatement.setString(2, password);
                userStatement.setString(3, salt);
                userStatement.setString(3, "Cashier");
                userStatement.executeUpdate();
            }

            // Unos podataka u tabelu cashier
            try (PreparedStatement cashierStatement = connection.prepareStatement(insertCashierQuery)) {
                cashierStatement.setString(1, cashier.getUsername());
                cashierStatement.setString(2, cashier.getFirstName());
                cashierStatement.setString(3, cashier.getLastName());
                cashierStatement.setString(4, cashier.getJmbg());
                cashierStatement.setDate(5, java.sql.Date.valueOf(cashier.getDateOfBirth()));
                cashierStatement.setString(6, cashier.getAddress());
                cashierStatement.setString(7, cashier.getPhoneNumber());
                cashierStatement.setDate(8, java.sql.Date.valueOf(cashier.getEmploymentDate()));

                cashierStatement.executeUpdate();
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
    public static void insertWarehouseEmployee(WarehouseEmployee warehouseEmployee, String password, String salt) {
        String insertUserQuery = "INSERT INTO users (username, password, salt, role) VALUES (?, ?, ?, ?)";
        String insertWarehouseEmployeeQuery = "INSERT INTO WarehouseEmployee (username, first_name, last_name, jmbg, date_of_birth, address, phone_number, employment_date, responsibility, access_level) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;

        try {
            connection = connect();
            connection.setAutoCommit(false);

            // Unos podataka u tabelu users
            try (PreparedStatement userStatement = connection.prepareStatement(insertUserQuery)) {
                userStatement.setString(1, warehouseEmployee.getUsername());
                userStatement.setString(2, password);
                userStatement.setString(3, salt);
                userStatement.setString(4, "WarehouseEmployee");
                userStatement.executeUpdate();
            }

            // Unos podataka u tabelu WarehouseEmployee
            try (PreparedStatement warehouseEmployeeStatement = connection.prepareStatement(insertWarehouseEmployeeQuery)) {
                warehouseEmployeeStatement.setString(1, warehouseEmployee.getUsername());
                warehouseEmployeeStatement.setString(2, warehouseEmployee.getFirstName());
                warehouseEmployeeStatement.setString(3, warehouseEmployee.getLastName());
                warehouseEmployeeStatement.setString(4, warehouseEmployee.getJmbg());
                warehouseEmployeeStatement.setDate(5, java.sql.Date.valueOf(warehouseEmployee.getDateOfBirth()));
                warehouseEmployeeStatement.setString(6, warehouseEmployee.getAddress());
                warehouseEmployeeStatement.setString(7, warehouseEmployee.getPhoneNumber());
                warehouseEmployeeStatement.setDate(8, java.sql.Date.valueOf(warehouseEmployee.getEmploymentDate()));
                warehouseEmployeeStatement.setString(9, warehouseEmployee.getResponsibility());
                warehouseEmployeeStatement.setString(10, warehouseEmployee.getAccessLevel());

                warehouseEmployeeStatement.executeUpdate();
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
    public static void insertDeliveryDriver(DeliveryDriver driver, String password, String salt) {
        String insertUserQuery = "INSERT INTO users (username, password, salt, role) VALUES (?, ?, ?, ?)";
        String insertDriverQuery = "INSERT INTO driver (username, first_name, last_name, jmbg, date_of_birth, " +
                "address, phone_number, employment_date, license_number, vehicle_info) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;

        try {
            connection = connect();
            connection.setAutoCommit(false);

            // Unos podataka u tabelu users
            try (PreparedStatement userStatement = connection.prepareStatement(insertUserQuery)) {
                userStatement.setString(1, driver.getUsername());
                userStatement.setString(2, password);
                userStatement.setString(3, salt);
                userStatement.setString(4, "Driver");
                userStatement.executeUpdate();
            }

            // Unos podataka u tabelu driver
            try (PreparedStatement driverStatement = connection.prepareStatement(insertDriverQuery)) {
                driverStatement.setString(1, driver.getUsername());
                driverStatement.setString(2, driver.getFirstName());
                driverStatement.setString(3, driver.getLastName());
                driverStatement.setString(4, driver.getJmbg());
                driverStatement.setDate(5, java.sql.Date.valueOf(driver.getDateOfBirth()));
                driverStatement.setString(6, driver.getAddress());
                driverStatement.setString(7, driver.getPhoneNumber());
                driverStatement.setDate(8, java.sql.Date.valueOf(driver.getEmploymentDate()));
                driverStatement.setString(9, driver.getLicenseNumber());
                driverStatement.setString(10, driver.getVehicleInfo());

                driverStatement.executeUpdate();
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
