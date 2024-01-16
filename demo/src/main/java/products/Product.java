package products;

import java.math.BigDecimal;
import java.time.LocalDate;
import validations.ProductValidation;

public class Product {
    private long productId;
    private String productName;
    private BigDecimal price;
    private int quantityInStock;
    private String productType;
    private String description;
    private LocalDate expirationDate;
    private String supplier;

    public Product(long productId, String productName, BigDecimal price, int quantityInStock, String productType,
                   String description, LocalDate expirationDate, String supplier) {
        if (ProductValidation.validateProductName(productName) &&
                ProductValidation.validatePrice(price) &&
                ProductValidation.validateQuantityInStock(quantityInStock) &&
                ProductValidation.validateProductType(productType) &&
                ProductValidation.validateDescription(description) &&
                ProductValidation.validateExpirationDate(expirationDate) &&
                ProductValidation.validateSupplier(supplier)) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.quantityInStock = quantityInStock;
            this.productType = productType;
            this.description = description;
            this.expirationDate = expirationDate;
            this.supplier = supplier;
        } else {
            throw new IllegalArgumentException("Invalid data provided for product");
        }
    }

    // Dodajte gettere i settere prema potrebi

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (ProductValidation.validateProductName(productName)) {
            this.productName = productName;
        } else {
            throw new IllegalArgumentException("Invalid product name");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (ProductValidation.validatePrice(price)) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Invalid price");
        }
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        if (ProductValidation.validateQuantityInStock(quantityInStock)) {
            this.quantityInStock = quantityInStock;
        } else {
            throw new IllegalArgumentException("Invalid quantity in stock");
        }
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        if (ProductValidation.validateProductType(productType)) {
            this.productType = productType;
        } else {
            throw new IllegalArgumentException("Invalid product type");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (ProductValidation.validateDescription(description)) {
            this.description = description;
        } else {
            throw new IllegalArgumentException("Invalid description");
        }
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        if (ProductValidation.validateExpirationDate(expirationDate)) {
            this.expirationDate = expirationDate;
        } else {
            throw new IllegalArgumentException("Invalid expiration date");
        }
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        if (ProductValidation.validateSupplier(supplier)) {
            this.supplier = supplier;
        } else {
            throw new IllegalArgumentException("Invalid supplier");
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantityInStock=" + quantityInStock +
                ", productType='" + productType + '\'' +
                ", description='" + description + '\'' +
                ", expirationDate=" + expirationDate +
                ", supplier='" + supplier + '\'' +
                '}';
    }
}
