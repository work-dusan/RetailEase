package users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import validations.WarehouseValidations;

import static DB.DatabaseConnector.connect;

public class WarehouseEmployee {
    private String username;
    private String firstName;
    private String lastName;
    private String jmbg;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
    private LocalDate employmentDate;
    private String responsibility;
    private String accessLevel;

    public WarehouseEmployee(String username, String firstName, String lastName, String jmbg, LocalDate dateOfBirth,
                             String address, String phoneNumber, LocalDate employmentDate, String responsibility, String accessLevel) {
        if (WarehouseValidations.validateFirstName(firstName) &&
                WarehouseValidations.validateLastName(lastName) &&
                WarehouseValidations.validateJMBG(jmbg) &&
                WarehouseValidations.validateDateOfBirth(dateOfBirth) &&
                WarehouseValidations.validateAddress(address) &&
                WarehouseValidations.validatePhoneNumber(phoneNumber) &&
                WarehouseValidations.validateEmploymentDate(employmentDate) &&
                WarehouseValidations.validateResponsibility(responsibility)) {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.jmbg = jmbg;
            this.dateOfBirth = dateOfBirth;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.employmentDate = employmentDate;
            this.responsibility = responsibility;
            this.accessLevel = accessLevel;
        } else {
            throw new IllegalArgumentException("Invalid data provided for warehouse employee");
        }
    }

    // Dodajte gettere i settere prema potrebi

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (WarehouseValidations.validateFirstName(firstName)){
            this.firstName = firstName;
        } else {
         throw new IllegalArgumentException("Invalid first name");
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (WarehouseValidations.validateLastName(lastName)){
            this.lastName = lastName;
        } else {
            throw new IllegalArgumentException("Invalid last name");
        }
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        if (WarehouseValidations.validateJMBG(jmbg)){
            this.jmbg = jmbg;
        } else {
            throw new IllegalArgumentException("Invalid JMBG");
        }
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (WarehouseValidations.validateDateOfBirth(dateOfBirth)){
            this.dateOfBirth = dateOfBirth;
        } else {
            throw new IllegalArgumentException("Invalid date of birth");
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (WarehouseValidations.validateAddress(address)){
            this.address = address;
        } else {
            throw new IllegalArgumentException("Invalid address");
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (WarehouseValidations.validatePhoneNumber(phoneNumber)){
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        if (WarehouseValidations.validateEmploymentDate(employmentDate)){
            this.employmentDate = employmentDate;
        } else {
            throw new IllegalArgumentException("Invalid employment date");
        };
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        if (WarehouseValidations.validateResponsibility(responsibility)){
            this.responsibility = responsibility;
        } else {
            throw new IllegalArgumentException("Invalid responsibility");
        }
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    @Override
    public String toString() {
        return "WarehouseEmployee{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", jmbg='" + jmbg + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", employmentDate=" + employmentDate +
                ", responsibility='" + responsibility + '\'' +
                ", accessLevel='" + accessLevel + '\'' +
                '}';
    }

    public static WarehouseEmployee findEmployee(String username){
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM warehouseemployee WHERE username = ?")) {
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            WarehouseEmployee employee = null;

            while (resultSet.next()){
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String jmbg = resultSet.getString("jmbg");
                LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone_number");
                LocalDate employmentDate = resultSet.getDate("employment_date").toLocalDate();
                String responsibility = resultSet.getString("responsibility");
                String accessLevel = resultSet.getString("access_level");

                employee = new WarehouseEmployee(username, firstName, lastName, jmbg, dateOfBirth, address, phoneNumber, employmentDate, responsibility, accessLevel);

            }
            return employee;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
