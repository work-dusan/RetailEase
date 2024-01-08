package validations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CashierValidations {

    public static boolean validateFirstName(String firstName) {
        // Implementirajte željene validacije za ime
        return firstName.matches("^[a-zA-Z]+(?:\\s+[a-zA-Z]+)*$");
    }

    public static boolean validateLastName(String lastName) {
        // Implementirajte željene validacije za prezime
        return lastName.matches("^[a-zA-Z]+(?:\\s+[a-zA-Z]+)*$");
    }

    public static boolean validateJMBG(String jmbg) {
        // Implementirajte željene validacije za JMBG
        return jmbg.matches("\\d{13}");
    }

    public static boolean validateDateOfBirth(String dob) {
        // Implementirajte željene validacije za datum rođenja
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            Date date = dateFormat.parse(dob);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean validateAddress(String address) {
        // Implementirajte željene validacije za adresu
        return address.length() > 0; // Primer jednostavne validacije
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        // Implementirajte željene validacije za broj telefona
        return phoneNumber.matches("\\d{9,15}");
    }

    public static boolean validateEmploymentDate(String employmentDate) {
        // Implementirajte željene validacije za datum zaposlenja
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            Date date = dateFormat.parse(employmentDate);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
