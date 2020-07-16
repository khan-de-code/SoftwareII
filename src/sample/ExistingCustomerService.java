package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class ExistingCustomerService {
    private Connection connection;

    public ReturnCodes init(){
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

    public RetVal getCustomers() {
        RetVal retVal = new RetVal();

        try {
            Statement statement = connection.createStatement();
            String customerQuery = "select customerName, address, address2, postalCode, phone, city, country, customerId  from customer inner join address on customer.addressId=address.addressId inner join city on address.cityId=city.cityId inner join country on city.countryId=country.countryId";
            ResultSet resultSet = statement.executeQuery(customerQuery);

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnNumber = rsmd.getColumnCount();
            while(resultSet.next()){
                for (int i = 1; i <= columnNumber; i++){
                    if (i > 1) System.out.print(", ");
                    String columnVal = resultSet.getString(i);
                    System.out.print(columnVal +  " " + rsmd.getColumnName(i));
                }
                System.out.println();
                Customer existingCustomer = new Customer(resultSet.getString(1),
                        resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5),
                        resultSet.getString(6), resultSet.getString(7),
                        Integer.parseInt(resultSet.getString(8)));
                retVal.queryResults.add(existingCustomer);
            }

        } catch (SQLException throwables){
            retVal.status = ReturnCodes.CONNECTION_FAILURE;
            return retVal;
        }

        retVal.status = ReturnCodes.SUCCESS;
        return retVal;
    }

}
