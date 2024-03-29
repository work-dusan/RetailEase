package users;

import validations.DeliveryDriverValidations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static DB.DatabaseConnector.connect;

public class DeliveryDriver {
    private String username;
    private String firstName;
    private String lastName;
    private String jmbg;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
    private LocalDate employmentDate;
    private String licenseNumber;
    private String vehicleInfo;

    public DeliveryDriver(String username, String firstName, String lastName, String jmbg, LocalDate dateOfBirth, String address,
                          String phoneNumber, LocalDate employmentDate, String licenseNumber, String vehicleInfo) {
        if (DeliveryDriverValidations.validateFirstName(firstName) &&
                DeliveryDriverValidations.validateLastName(lastName)&&
                DeliveryDriverValidations.validateJMBG(jmbg) &&
                DeliveryDriverValidations.validateDateOfBirth(dateOfBirth) &&
                DeliveryDriverValidations.validateAddress(address) &&
                DeliveryDriverValidations.validatePhoneNumber(phoneNumber) &&
                DeliveryDriverValidations.validateEmploymentDate(employmentDate) &&
                DeliveryDriverValidations.validateLicensePlate(licenseNumber) &&
                DeliveryDriverValidations.validateVehicleInfo(vehicleInfo)) {
            // Inicijalizacija atributa
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.jmbg = jmbg;
            this.dateOfBirth = dateOfBirth;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.employmentDate = employmentDate;
            this.licenseNumber = licenseNumber;
            this.vehicleInfo = vehicleInfo;
        } else {
            throw new IllegalArgumentException("Invalid data provided for delivery driver");
        }
    }

    // Getteri i setteri

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (DeliveryDriverValidations.validateFirstName(firstName)) {
            this.firstName = firstName;
        } else {
            throw new IllegalArgumentException("Invalid first name");
        }
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName) {
        if (DeliveryDriverValidations.validateLastName(lastName)) {
            this.lastName = lastName;
        } else {
            throw new IllegalArgumentException("Invalid last name");
        }
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        if (DeliveryDriverValidations.validateJMBG(jmbg)) {
            this.jmbg = jmbg;
        } else {
            throw new IllegalArgumentException("Invalid JMBG");
        }
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (DeliveryDriverValidations.validateDateOfBirth(dateOfBirth)) {
            this.dateOfBirth = dateOfBirth;
        } else {
            throw new IllegalArgumentException("Invalid date of birth");
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (DeliveryDriverValidations.validateAddress(address)) {
            this.address = address;
        } else {
            throw new IllegalArgumentException("Invalid address");
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (DeliveryDriverValidations.validatePhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        if (DeliveryDriverValidations.validateEmploymentDate(employmentDate)) {
            this.employmentDate = employmentDate;
        } else {
            throw new IllegalArgumentException("Invalid employment date");
        }
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        if (DeliveryDriverValidations.validateLicensePlate(licenseNumber)) {
            this.licenseNumber = licenseNumber;
        } else {
            throw new IllegalArgumentException("Invalid license plate");
        }
    }

    public String getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(String vehicleInfo) {
        if (DeliveryDriverValidations.validateVehicleInfo(vehicleInfo)) {
            this.vehicleInfo = vehicleInfo;
        } else {
            throw new IllegalArgumentException("Invalid vehicle info");
        }
    }

    public static DeliveryDriver findDriver(String username) {
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM driver WHERE username = ?")) {
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            DeliveryDriver employee = null;

            while (resultSet.next()){
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                String jmbg = resultSet.getString("jmbg");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone_number");
                LocalDate employmentDate = resultSet.getDate("employment_date").toLocalDate();
                String licenceNumber = resultSet.getString("license_number");
                String vehicleInfo = resultSet.getString("vehicle_info");

                employee = new DeliveryDriver(username, firstName, lastName, jmbg, dateOfBirth, address, phoneNumber, employmentDate, licenceNumber, vehicleInfo);

            }
            return employee;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public String toString() {
        return "DeliveryDriver{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", jmbg='" + jmbg + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", employmentDate=" + employmentDate +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", vehicleInfo='" + vehicleInfo + '\'' +
                '}';
    }
}
