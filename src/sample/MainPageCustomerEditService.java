package sample;

import java.sql.*;
import java.util.ArrayList;

public class MainPageCustomerEditService {
    private Connection connection;
    private String loggedInUser;
    private long loggedInUserID;

    private String customerName;
    private String customerAddress;
    private String customerAddress2;
    private String customerCity;
    private String customerPostal;
    private String customerCountry;
    private String customerPhone;

    private Customer existingCustomer;

    public ReturnCodes init(String loggedInUser, long loggedInUserID, Customer customer, Customer existingCustomer) {
        this.loggedInUser = loggedInUser;
        this.loggedInUserID = loggedInUserID;
        this.customerName = customer.getCustomerName();
        this.customerAddress = customer.getAddress();
        this.customerAddress2 = customer.getAddress2();
        this.customerCity = customer.getCity();
        this.customerPostal = customer.getPostalCode();
        this.customerCountry = customer.getCountry();
        this.customerPhone = customer.getPhone();
        this.existingCustomer = existingCustomer;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String databaseUsername = "U06gFz";
            String databasePassword = "53688759613";
            connection = DriverManager.getConnection(
                    "jdbc:mysql://3.227.166.251/" + databaseUsername,
                    databaseUsername,
                    databasePassword
            );
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            return ReturnCodes.CONNECTION_FAILURE;
        }
        return ReturnCodes.SUCCESS;
    }

    private String s(String string){
        return " '" + string + "', ";
    }

    public ArrayList createCustomer() {
        ArrayList ret = new ArrayList();
        long createdCustomerID = -1;

        try {
            //Save country data
            Statement statement = connection.createStatement();

            java.util.Date date = new java.util.Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
            String countryData = "INSERT INTO country (countryId, country, createDate, createdBy, lastUpdateBy) " +
                    "VALUES (DEFAULT, " + s(customerCountry) + " '" + timestamp + "', " + s(loggedInUser) + "'" + loggedInUser + "' )";
            statement.executeUpdate(countryData, Statement.RETURN_GENERATED_KEYS);

            ResultSet generatedCountryID = statement.getGeneratedKeys();
            long createdCountryID = -1;

            if (generatedCountryID.next()){
                createdCountryID = generatedCountryID.getLong(1);
            }

            //Save city data
            String cityData = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdateBy)" +
                    "VALUES (" + s(customerCity) + s((createdCountryID + "")) + " '" + timestamp + "', " + s(loggedInUser) + "'" + loggedInUser + "' )";
            statement.executeUpdate(cityData, Statement.RETURN_GENERATED_KEYS);

            ResultSet generatedCityID = statement.getGeneratedKeys();
            long createdCityID = -1;

            if (generatedCityID.next()){
                createdCityID = generatedCityID.getLong(1);
            }

            //Save address
            String addressData;
            if (customerAddress2 == null){
                addressData = "INSERT INTO address (address, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy)" +
                        "VALUES (" + s(customerAddress) + " '" + createdCityID + "', " + s(customerPostal) + s(customerPhone) + " '" + timestamp + "', " + s(loggedInUser) + "'" + loggedInUser  + "' )";
            } else {
                addressData = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy)" +
                        "VALUES (" + s(customerAddress) + s(customerAddress2) + " '" + createdCityID + "', " + s(customerPostal) + s(customerPhone) + " '" + timestamp + "', " + s(loggedInUser) + "'" + loggedInUser  + "' )";
            }
            statement.executeUpdate(addressData, Statement.RETURN_GENERATED_KEYS);

            ResultSet generatedAddressID = statement.getGeneratedKeys();
            long createdAddressID = -1;

            if (generatedAddressID.next()){
                createdAddressID = generatedAddressID.getLong(1);
            }

            //Save customer
            String customerData = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy)" +
                    "VALUES (" + s(customerName) + s(createdAddressID + "") + s(1 + "") + " '" + timestamp + "', " + s(loggedInUser) + "'" + loggedInUser + "' )";
            statement.executeUpdate(customerData, Statement.RETURN_GENERATED_KEYS);

            ResultSet generatedCustomerID = statement.getGeneratedKeys();

            if (generatedCustomerID.next()){
                createdCustomerID = generatedCustomerID.getLong(1);
            }

            ret.add(ReturnCodes.SUCCESS);
            ret.add(createdCustomerID);
            return ret;

        } catch(SQLException throwables) {
            throwables.printStackTrace();
            ret.add(ReturnCodes.CONNECTION_FAILURE);
            ret.add(-1);
            return ret;
        } finally {
            closeConnection();
        }
    }

    public ReturnCodes updateCustomer(){
        try {
            Statement statement = connection.createStatement();
            String sql;

            //TODO get all relvant IDs for customer to be updated

            if(!existingCustomer.getCountry().equals(customerCountry)){
                sql = "UPDATE country\n"
                        + "INNER JOIN city ON country.countryId = city.countryId\n"
                        + "INNER JOIN address ON city.cityId = address.cityId\n"
                        + "INNER JOIN customer ON address.addressId = customer.addressId\n"
                        + "SET country.country = '" + customerCountry + "', " + "country.lastUpdateBy = '" + loggedInUser + "'\n"
                        + "WHERE customer.customerId = '" + existingCustomer.getCustomerId() + "'";

                statement.executeUpdate(sql);
            }

            if(!existingCustomer.getCity().equals(customerCity)){
                sql = "UPDATE city\n"
                        + "INNER JOIN address ON city.cityId = address.cityId\n"
                        + "INNER JOIN customer ON address.addressId = customer.addressId\n"
                        + "SET city.city = '" + customerCity + "', " + "city.lastUpdateBy = '" + loggedInUser + "'\n"
                        + "WHERE customer.customerId = '" + existingCustomer.getCustomerId() + "'";

                statement.executeUpdate(sql);
            }

            if(
                    !existingCustomer.getAddress().equals(customerAddress)
                    || !existingCustomer.getAddress2().equals(customerAddress2)
                    || !existingCustomer.getPostalCode().equals(customerPostal)
                    || !existingCustomer.getPhone().equals(customerPhone)
            ){
                sql = "UPDATE address\n"
                        + "INNER JOIN customer ON address.addressId = customer.addressId\n"
                        + "SET address.address = '" + customerAddress + "', "
                            + "address.address2 = '" + customerAddress2 + "', "
                            + "address.postalCode = '" + customerPostal + "', "
                            + "address.phone = '" + customerPhone + "', "
                            + "address.lastUpdateBy = '" + loggedInUser + "'\n"
                        + "WHERE customer.customerId = '" + existingCustomer.getCustomerId() + "'";

                statement.executeUpdate(sql);
            }

            if (!existingCustomer.getCustomerName().equals(customerName)){
                sql = "UPDATE customer\n"
                        + "SET customer.customerName = '" + customerName + "', " + "customer.lastUpdateBy = '" + loggedInUser + "'\n"
                        + "WHERE customer.customerId = '" + existingCustomer.getCustomerId() + "'";

                statement.executeUpdate(sql);
            }

            return closeConnection();

        } catch (SQLException throwables){
            System.out.println(throwables);
            closeConnection();
            return ReturnCodes.CONNECTION_FAILURE;
        }
    }

    public ReturnCodes closeConnection() {
        try {
            connection.close();
        } catch (SQLException throwables){
            return ReturnCodes.CONNECTION_FAILURE;
        }
        return ReturnCodes.SUCCESS;
    }
}
