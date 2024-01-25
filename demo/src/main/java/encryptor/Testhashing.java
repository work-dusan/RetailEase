package encryptor;

import encryptor.SHA256;

public class Testhashing {
    public static void main(String[] args) {
        try {
            // Generisanje salt-a
            String salt = SHA256.generateSalt();

            // Originalna lozinka
            String password = "IIgg11!!";

            // Hash-ovanje lozinke sa salt-om
            String hashedPassword = SHA256.hashPassword(password, salt);

            // Prikaz rezultata
            System.out.println("Lozinka: " + password);
            System.out.println("Generisani salt: " + salt);
            System.out.println("Hash-ovana lozinka: " + hashedPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}