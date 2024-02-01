package delivery;

public class DeliveryOrder {

    private String deliveryAddress;
    private String apartmentNumber;
    private String deliveryTime;
    private String specialRequest;
    private String status;
    private int transactionId;

    // Constructor
    public DeliveryOrder(String deliveryAddress, String apartmentNumber, String deliveryTime,
                         String specialRequest,
                         String status, int transactionId) {
        this.deliveryAddress = deliveryAddress;
        this.apartmentNumber = apartmentNumber;
        this.deliveryTime = deliveryTime;
        this.specialRequest = specialRequest;
        this.status = status;
        this.transactionId = transactionId;
    }

    // Getters and Setters

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    // Additional methods if needed...

    @Override
    public String toString() {
        return "DeliveryOrder{" +
                "deliveryAddress='" + deliveryAddress + '\'' +
                ", apartmentNumber='" + apartmentNumber + '\'' +
                ", deliveryTime='" + deliveryTime + '\'' +
                ", specialRequest='" + specialRequest + '\'' +
                ", status='" + status + '\'' +
                ", transactionId=" + transactionId +
                '}';
    }
}

