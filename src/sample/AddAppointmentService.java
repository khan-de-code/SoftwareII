package sample;

import java.sql.*;
import java.util.ArrayList;

public class AddAppointmentService {
    private Connection connection;
    private final String databaseUsername = "U06gFz";
    private final String databasePassword = "53688759613";
    private String loggedInUser;
    private long loggedInUserID;

    private Long customerId;

    private String customerName;
    private String customerAddress;
    private String customerAddress2;
    private String customerCity;
    private String customerPostal;
    private String customerCountry;
    private String customerPhone;
    private String appointmentName;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private String startDateTime;
    private String endDateTime;

    public ReturnCodes init(String loggedInUser, long loggedInUserID, Long customerId, String customerName, String customerAddress, String customerAddress2, String customerCity, String customerPostal, String customerCountry, String customerPhone, String appointmentName, String description, String location, String contact, String type, String url, String startDateTime, String endDateTime) {
        this.loggedInUser = loggedInUser;
        this.loggedInUserID = loggedInUserID;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerAddress2 = customerAddress2;
        this.customerCity = customerCity;
        this.customerPostal = customerPostal;
        this.customerCountry = customerCountry;
        this.customerPhone = customerPhone;
        this.appointmentName = appointmentName;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;

        try {
            Class.forName("com.mysql.jdbc.Driver");
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

    public ArrayList createCustomer(Boolean queryAppointments) {
        //Ensure there are no overlapping appointment times
        ArrayList ret = new ArrayList();
        long createdCustomerID = -1;

        if (queryAppointments) {
            ReturnCodes result = queryAppointments();

            if (result != ReturnCodes.SUCCESS) {
                ret.add(result);
                ret.add(-1);
                return ret;
            }
        }

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


        } catch(SQLException throwables) {
            throwables.printStackTrace();
            ret.add(ReturnCodes.CONNECTION_FAILURE);
            ret.add(-1);
            return ret;
        }

        ret.add(ReturnCodes.SUCCESS);
        ret.add(createdCustomerID);
        return ret;
    }

    public ReturnCodes createAppointment(long customerId$){
        if (this.customerId != null) {
            customerId$ = this.customerId;
        }

        ReturnCodes result = queryAppointments();

        if (result != ReturnCodes.SUCCESS) {
            return result;
        }

        try {
            Statement statement = connection.createStatement();

            java.util.Date date = new java.util.Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
            String countryData = "INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy) " +
                    "VALUES ( " + s(customerId$ + "") + s(loggedInUserID + "") + s(appointmentName) + s(description) + s(location) + s(contact) + s(type) + s(url) + s(startDateTime) + s(endDateTime) + "'" + timestamp + "', " + s(loggedInUser) + "'" + loggedInUser + "' )";
            statement.executeUpdate(countryData, Statement.RETURN_GENERATED_KEYS);

        } catch (SQLException throwables){
            throwables.printStackTrace();
            return ReturnCodes.CONNECTION_FAILURE;
        }

        return ReturnCodes.SUCCESS;
    }

    public ReturnCodes queryAppointments() {
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM appointment WHERE" +
                    " (start <= '" + startDateTime + "' AND end >= '" + startDateTime + "' )" +
                    " OR (start <= '" + endDateTime + "' AND end >= '" + endDateTime + "' )" +
                    " OR (('" + startDateTime + "' <= start AND '" + endDateTime + "' >= start) AND ( " + "'" + startDateTime + "' <= end AND '" + endDateTime + "' >= end))";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.first()){
                return ReturnCodes.OVERLAPPING_APPOINTMENT;
            }

        } catch(SQLException throwables) {
            throwables.printStackTrace();
            return ReturnCodes.CONNECTION_FAILURE;
        }

        return ReturnCodes.SUCCESS;
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
