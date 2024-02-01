package transactions;

import users.Customer;

public class Transaction {
    private Customer customer;
    private Double total;
    private String status;

    public Transaction(Customer customer, Double total, String status) {
        this.customer = customer;
        this.total = total;
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "customer=" + customer +
                ", total=" + total +
                ", status='" + status + '\'' +
                '}';
    }
}
