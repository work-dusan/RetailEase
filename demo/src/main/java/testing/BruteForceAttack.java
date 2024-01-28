package testing;

import encryptor.SHA256;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class BruteForceAttack {

    private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_-+=<>?";
    private static final int MIN_LENGTH = 7;
    private static final int MAX_LENGTH = 30;
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/retailease";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            String username = "Dusan";
            for (int length = MIN_LENGTH; length <= MAX_LENGTH; length++) {
                Set<String> combinations = generateCombinations(CHARSET, length);
                for (String combination : combinations) {
                    String hashedPassword = hashPassword(combination);
                    if (checkPassword(username, hashedPassword)) {
                        System.out.println("Found the password: " + combination);
                        return;
                    }
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Failed to find the password.");
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        return SHA256.hashPassword(password, "HONGBrg7PLR5pJXdqPmj5w==");
    }

    private static boolean checkPassword(String username, String hashedPassword) {
        String storedPasswordHash = null;
        String storedSalt = null;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String query = "SELECT password, salt FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                storedPasswordHash = resultSet.getString("password");
                storedSalt = resultSet.getString("salt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (storedPasswordHash == null || storedSalt == null) {
            return false;
        }
        return hashedPassword.equals(storedPasswordHash);
    }

    private static Set<String> generateCombinations(String charset, int length) {
        Set<String> combinations = new HashSet<>();
        char[] chars = charset.toCharArray();
        for (char aChar : chars) {
            if (length == 1) {
                combinations.add(String.valueOf(aChar));
            } else {
                Set<String> subCombinations = generateCombinations(charset, length - 1);
                for (String subCombination : subCombinations) {
                    combinations.add(aChar + subCombination);
                }
            }
        }
        return combinations;
    }
}