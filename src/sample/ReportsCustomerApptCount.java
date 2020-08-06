package sample;

public class ReportsCustomerApptCount {
    private final int count;
    private final String customerName;

    public ReportsCustomerApptCount(int count, String customerName) {
        this.count = count;
        this.customerName = customerName;
    }

    public int getCount() {
        return count;
    }

    public String getCustomerName() {
        return customerName;
    }
}
