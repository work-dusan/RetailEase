package users;

import validations.CustomerValidations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static DB.DatabaseConnector.connect;

public class Customer {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String streetName;
    private String streetNumber;
    private String city;
    private String phoneNumber;

    public Customer(String username, String firstName, String lastName, String email, String streetName, String streetNumber, String city, String phoneNumber) throws IllegalArgumentException {
        if (CustomerValidations.validateEmail(email) &&
                CustomerValidations.validateStreetName(streetName) &&
                CustomerValidations.validateStreetNumber(streetNumber) &&
                CustomerValidations.validatePhoneNumber(phoneNumber) &&
                CustomerValidations.validateName(firstName) &&
                CustomerValidations.validateName(lastName)) {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.streetName = streetName;
            this.streetNumber = streetNumber;
            this.city = city;
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid data provided for customer registration");
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (CustomerValidations.validateName(firstName)){
            this.firstName = firstName;
        } else {
            throw new IllegalArgumentException("Invalid first name format");
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (CustomerValidations.validateName(lastName)){
            this.lastName = lastName;
        } else {
            throw new IllegalArgumentException("Invalid last name format");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (CustomerValidations.validateEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        if (CustomerValidations.validateStreetName(streetName)) {
            this.streetName = streetName;
        } else {
            throw new IllegalArgumentException("Invalid street name");
        }
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        if (CustomerValidations.validateStreetNumber(streetNumber)) {
            this.streetNumber = streetNumber;
        } else {
            throw new IllegalArgumentException("Invalid street number");
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (CustomerValidations.validatePhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", streetName='" + streetName + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public static Customer findCustomer(String username){
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer WHERE username = ?")) {
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            Customer employee = null;

            while (resultSet.next()){
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String streetName = resultSet.getString("street_name");
                String streetNumber = resultSet.getString("street_number");
                String phoneNumber = resultSet.getString("phone_number");
                String city = resultSet.getString("city");

                employee = new Customer(username, firstName, lastName, email, streetName, streetNumber, city, phoneNumber);

            }
            return employee;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}



