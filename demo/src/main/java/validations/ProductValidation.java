package validations;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductValidation {
    public static boolean validateProductName(String productName) {
        // Validacija naziva proizvoda
        return productName != null && !productName.trim().isEmpty();
    }

    public static boolean validatePrice(BigDecimal price) {
        // Validacija cene
        return price != null && price.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean validateQuantityInStock(int quantityInStock) {
        // Validacija količine na stanju
        return quantityInStock >= 0;
    }

    public static boolean validateProductType(String productType) {
        // Validacija tipa proizvoda
        return productType != null && !productType.trim().isEmpty();
    }

    public static boolean validateDescription(String description) {
        // Validacija opisa
        return description != null;
    }

    public static boolean validateExpirationDate(LocalDate expirationDate) {
        // Validacija datuma isteka
        return expirationDate != null && !expirationDate.isBefore(LocalDate.now());
    }

    public static boolean validateSupplier(String supplier) {
        // Validacija dobavljača
        return supplier != null && !supplier.trim().isEmpty();
    }
}
