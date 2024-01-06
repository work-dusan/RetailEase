package users;

import validations.CustomerValidations;

public class Customer {

    private String username;
    private String email;
    private String streetName;
    private String streetNumber;
    private String city;
    private String phoneNumber;

    public Customer(String username, String email, String streetName, String streetNumber, String city, String phoneNumber) throws IllegalArgumentException {
        if (CustomerValidations.validateEmail(email) && CustomerValidations.validateStreetName(streetName) && CustomerValidations.validateStreetNumber(streetNumber) && CustomerValidations.validatePhoneNumber(phoneNumber)) {
            this.username = username;
            this.email = email;
            this.streetName = streetName;
            this.streetNumber = streetNumber;
            this.city = city;
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid data provided for customer registration");
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
                ", email='" + email + '\'' +
                ", streetName='" + streetName + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }


}
