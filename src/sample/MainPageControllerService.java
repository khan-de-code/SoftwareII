package sample;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainPageControllerService {
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

    public RetVal getAppointments(){
        RetVal retVal = new RetVal();

        try {
            Statement statement = connection.createStatement();
            String customerQuery = "select title, description, location, contact, type, url, start, end, appointmentId, customerName, address, address2, postalCode, phone, city, country, customer.customerId from appointment inner join customer on appointment.customerId = customer.customerId inner join address on customer.addressId=address.addressId inner join city on address.cityId=city.cityId inner join country on city.countryId=country.countryId";
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
                Customer existingCustomer = new Customer(resultSet.getString(10),
                        resultSet.getString(11), resultSet.getString(12),
                        resultSet.getString(13), resultSet.getString(14),
                        resultSet.getString(15), resultSet.getString(16),
                        Integer.parseInt(resultSet.getString(17)));
                retVal.queryResults.add(existingCustomer);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                LocalDateTime startTime = LocalDateTime.parse(resultSet.getString(7), formatter);
                LocalDateTime endTime = LocalDateTime.parse(resultSet.getString(8), formatter);

                Appointment existingAppointment = new Appointment(
                        existingCustomer, resultSet.getString(1),
                        resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5),
                        resultSet.getString(6), startTime,
                        endTime, Integer.parseInt(resultSet.getString(9)));
                retVal.queryResultsAppointment.add(existingAppointment);
            }

        } catch (SQLException throwables){
            retVal.status = ReturnCodes.CONNECTION_FAILURE;
            return retVal;
        }

        retVal.status = ReturnCodes.SUCCESS;

        return retVal;
    }
}
