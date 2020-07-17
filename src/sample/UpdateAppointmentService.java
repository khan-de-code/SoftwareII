package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UpdateAppointmentService {
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

//    public getAppointment(){
//        // TODO
//    }

    public ReturnCodes closeConnection() {
        try {
            connection.close();
        } catch (SQLException throwables){
            return ReturnCodes.CONNECTION_FAILURE;
        }
        return ReturnCodes.SUCCESS;
    }
}
