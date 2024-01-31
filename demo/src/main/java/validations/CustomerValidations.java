package validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerValidations {

    private static final String NAME_REGEX = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String STREET_NAME_REGEX = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
    private static final String STREET_NUMBER_REGEX = "^\\d+[a-zA-Z]*$";
    private static final String PHONE_NUMBER_REGEX = "^\\d{10}$";

    public static boolean validateName(String name){
        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validateStreetName(String streetName) {
        Pattern pattern = Pattern.compile(STREET_NAME_REGEX);
        Matcher matcher = pattern.matcher(streetName);
        return matcher.matches();
    }

    public static boolean validateStreetNumber(String streetNumber) {
        Pattern pattern = Pattern.compile(STREET_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(streetNumber);
        return matcher.matches();
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean validateAllInfo(String firstName,
                                          String lastName,
                                          String email,
                                          String streetName,
                                          String streetNumber,
                                          String phoneNumber){
        return validateName(firstName) &&
                validateName(lastName) &&
                validateEmail(email) &&
                validateStreetName(streetName) &&
                validateStreetNumber(streetNumber) &&
                validatePhoneNumber(phoneNumber);
    }
}
