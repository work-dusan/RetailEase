package validations;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class DeliveryDriverValidations {

    private static final String NAME_REGEX = "^[A-Za-z\\s]{1,}[.-]{0,1}[A-Za-z\\s]{0,}$";

    public static boolean validateFirstName(String firstName) {
        // Provera da li je ime validno
        return Pattern.matches(NAME_REGEX, firstName);
    }

    public static boolean validateLastName(String lastName) {
        // Provera da li je prezime validno
        return Pattern.matches(NAME_REGEX, lastName);
    }


    public static boolean validateJMBG(String jmbg) {
        // Provera dužine i da li sadrži samo cifre
        return jmbg.matches("\\d{13}");
    }

    public static boolean validateDateOfBirth(LocalDate dateOfBirth) {
        // Provera da li je dostavljač star najmanje 18 godina
        LocalDate eighteenYearsAgo = LocalDate.now().minusYears(18);
        return dateOfBirth.isBefore(eighteenYearsAgo);
    }

    public static boolean validateAddress(String address) {
        // Provera da li adresa nije prazna
        return !address.trim().isEmpty();
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        // Provera da li broj telefona sadrži samo cifre i da li ima odgovarajuću dužinu
        return phoneNumber.matches("\\d{9,10}");
    }

    public static boolean validateEmploymentDate(LocalDate employmentDate) {
        // Provera da li je datum zaposlenja u prošlosti
        return employmentDate.isBefore(LocalDate.now());
    }

    public static boolean validateLicensePlate(String licensePlate) {
        // Provera formata tablice (SS-BBBB-SS ili SS-BBB-SS)
        return licensePlate.matches("[A-Z]{2}-\\d{3,4}-[A-Z]{2}");
    }

    public static boolean validateVehicleInfo(String vehicleInfo) {
        // Dodajte sopstvene validacije prema potrebi
        return !vehicleInfo.trim().isEmpty();
    }
}
