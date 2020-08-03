package sample;

public class Customer {
    private final String customerName;
    private final String address;
    private final String address2;
    private final String postalCode;
    private final String phone;
    private final String city;
    private final String country;
    private final Integer customerId;


    public Customer(String customerName, String address, String address2, String postalCode, String phone, String city, String country, Integer customerId) {
        this.customerName = customerName;
        this.address = address;
        if (address2.equals("")){
            this.address2 = null;
        } else {
            this.address2 = address2;
        }
        this.postalCode = postalCode;
        this.phone = phone;
        this.city = city;
        this.country = country;
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress2() {
        return address2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Integer getCustomerId(){
        return customerId;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerName='" + customerName + '\'' +
                ", address='" + address + '\'' +
                ", address2='" + address2 + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", customerId=" + customerId +
                '}';
    }
}
