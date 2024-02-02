package testing;

import main.WarehouseEmployeeMainScene;
import org.junit.Test;
import products.Product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WarehouseEmployeeMainSceneTest {

    @Test
    public void testFilterProductListWithMatchingProductIdShouldReturnFilteredProducts() {
        // Arrange
        WarehouseEmployeeMainScene warehouseEmployeeMainScene = new WarehouseEmployeeMainScene();
        ObservableList<Product> originalList = FXCollections.observableArrayList(
                new Product("KASKA5478996655", "Product1", 10.0, 5, "Type1", "Description1", LocalDate.now().plusYears(2), "Supplier1", null),
                new Product("KASKA5458963354", "Product2", 15.0, 8, "Type2", "Description2", LocalDate.now().plusYears(2), "Supplier2", null)
        );

        // Act
        ObservableList<Product> filteredList = warehouseEmployeeMainScene.filterProductList(originalList, null, "KASKA54");

        // Assert
        assertEquals(2, filteredList.size());
    }

    @Test
    public void testFilterProductListWithMatchingProductNameShouldReturnFilteredProducts() {
        // Arrange
        WarehouseEmployeeMainScene warehouseEmployeeMainScene = new WarehouseEmployeeMainScene();
        ObservableList<Product> originalList = FXCollections.observableArrayList(
                new Product("KASKA5478996655", "Product1", 10.0, 5, "Type1", "Description1", LocalDate.now().plusYears(2), "Supplier1", null),
                new Product("KASKA5458963354", "Product2", 15.0, 8, "Type2", "Description2", LocalDate.now().plusYears(2), "Supplier2", null)
        );

        // Act
        ObservableList<Product> filteredList = warehouseEmployeeMainScene.filterProductList(originalList, null, "KASKA5478996655");

        // Assert
        assertEquals(1, filteredList.size());
    }

    @Test
    public void testFilterProductListWithNonMatchingKeywordShouldReturnEmptyList() {
        // Arrange
        WarehouseEmployeeMainScene warehouseEmployeeMainScene = new WarehouseEmployeeMainScene();
        ObservableList<Product> originalList = FXCollections.observableArrayList(
                new Product("KASKA5478996655", "Product1", 10.0, 5, "Type1", "Description1", LocalDate.now().plusYears(2), "Supplier1", null),
                new Product("KASKA5458963354", "Product2", 15.0, 8, "Type2", "Description2", LocalDate.now().plusYears(2), "Supplier2", null)
        );

        // Act
        ObservableList<Product> filteredList = warehouseEmployeeMainScene.filterProductList(originalList, null, "MAKUF4589632567");

        // Assert
        assertEquals(0, filteredList.size());
    }
}
