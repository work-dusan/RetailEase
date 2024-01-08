package users;

import java.time.LocalDate;
import validations.CashierValidations;

public class Cashier {
    private String username;
    private String firstName;
    private String lastName;
    private String jmbg;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
    private LocalDate employmentDate;

    public Cashier(String username, String firstName, String lastName, String jmbg, String dateOfBirth, String address, String phoneNumber, String employmentDate) throws IllegalArgumentException {
        if (CashierValidations.validateFirstName(firstName) &&
                CashierValidations.validateLastName(lastName) &&
                CashierValidations.validateJMBG(jmbg) &&
                CashierValidations.validateDateOfBirth(dateOfBirth) &&
                CashierValidations.validateAddress(address) &&
                CashierValidations.validatePhoneNumber(phoneNumber) &&
                CashierValidations.validateEmploymentDate(employmentDate)) {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.jmbg = jmbg;
            this.dateOfBirth = LocalDate.parse(dateOfBirth);
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.employmentDate = LocalDate.parse(employmentDate);
        } else {
            throw new IllegalArgumentException("Invalid data provided for cashier registration");
        }
    }

    // dodajte gettere i settere

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
        if (CashierValidations.validateFirstName(firstName)) {
            this.firstName = firstName;
        } else {
            throw new IllegalArgumentException("Invalid first name");
        }
    }
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (CashierValidations.validateLastName(lastName)) {
            this.lastName = lastName;
        } else {
            throw new IllegalArgumentException("Invalid last name");
        }
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        if (CashierValidations.validateJMBG(jmbg)) {
            this.jmbg = jmbg;
        } else {
            throw new IllegalArgumentException("Invalid JMBG");
        }
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        if (CashierValidations.validateDateOfBirth(dateOfBirth)) {
            this.dateOfBirth = LocalDate.parse(dateOfBirth);
        } else {
            throw new IllegalArgumentException("Invalid date of birth");
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (CashierValidations.validateAddress(address)) {
            this.address = address;
        } else {
            throw new IllegalArgumentException("Invalid address");
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (CashierValidations.validatePhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(String employmentDate) {
        if (CashierValidations.validateEmploymentDate(employmentDate)) {
            this.employmentDate = LocalDate.parse(employmentDate);
        } else {
            throw new IllegalArgumentException("Invalid employment date");
        }
    }

    @Override
    public String toString() {
        return "Cashier{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", jmbg='" + jmbg + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", employmentDate=" + employmentDate +
                '}';
    }
}
