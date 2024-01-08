package validations;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class WarehouseValidations {

    private static final String NAME_REGEX = "^[A-Za-z\\s]{1,}[.-]{0,1}[A-Za-z\\s]{0,}$";
    private static final String JMBG_REGEX = "^[0-9]{13}$";
    private static final String PHONE_NUMBER_REGEX = "^[0-9]{9,15}$";

    public static boolean validateFirstName(String firstName) {
        return Pattern.matches(NAME_REGEX, firstName);
    }

    public static boolean validateLastName(String lastName) {
        return Pattern.matches(NAME_REGEX, lastName);
    }

    public static boolean validateJMBG(String jmbg) {
        return Pattern.matches(JMBG_REGEX, jmbg);
    }

    public static boolean validateDateOfBirth(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        return dateOfBirth.isBefore(currentDate);
    }

    public static boolean validateAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        return Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber);
    }

    public static boolean validateEmploymentDate(LocalDate employmentDate) {
        LocalDate currentDate = LocalDate.now();
        return !employmentDate.isAfter(currentDate);
    }

    public static boolean validateResponsibility(String responsibility) {
        return responsibility != null;
    }

}
