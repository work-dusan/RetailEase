package products;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class ShoppingCart {

    private ObservableList<CartItem> items;

    public ShoppingCart() {
        this.items = FXCollections.observableArrayList();
    }

    public ObservableList<CartItem> getItems() {
        return items;
    }

    public void addItem(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().equals(product)) {
                int newQuantity = item.getQuantity() + quantity;
                if (newQuantity <= product.getQuantityInStock()) {
                    item.setQuantity(newQuantity);
                } else {
                    showAlert("No more products", "No more " + product.getProductName() + "in stock", Alert.AlertType.INFORMATION);
                }
                return;
            }
        }

        if (quantity <= product.getQuantityInStock()) {
            items.add(new CartItem(product, quantity));
        } else {
            showAlert("No more products", "No more " + product.getProductName() + "in stock", Alert.AlertType.INFORMATION);
        }
    }

    public void removeItem(CartItem cartItem) {
        items.remove(cartItem);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
