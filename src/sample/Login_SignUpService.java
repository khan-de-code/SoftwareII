package sample;

import javax.print.DocFlavor;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Login_SignUpService {
    private final String username;
    private final String password;

    private final String databaseUsername = "U06gFz";
    private final String databasePassword = "53688759613";

    public Login_SignUpService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ArrayList attemptLogin() throws SQLException {
        ArrayList ret = new ArrayList();
        Connection connection;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://3.227.166.251/" + databaseUsername,
                    databaseUsername,
                    databasePassword
            );
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            ret.add(ReturnCodes.CONNECTION_FAILURE);
            return ret;
        }

        try {
            Statement statement = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            String sql = "SELECT * FROM user WHERE userName= '" + username + "' AND password= '" + password + "'";
            ResultSet result = statement.executeQuery(sql);

            if (result.first()){
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                result.updateTimestamp("lastUpdate", timestamp);
                ret.add(ReturnCodes.SUCCESS);
                ret.add(result.getLong(1));
                return ret;
            } else {
                ret.add(ReturnCodes.USER_NOT_FOUND);
                return ret;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ret.add(ReturnCodes.STATEMENT_ERROR);
            return ret;
        } finally {
            connection.close();
        }
    }

    public ReturnCodes attemptSignUp() throws SQLException {
        Connection connection;
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

        try{
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM user WHERE userName='" + username + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.first()){
                return ReturnCodes.SIGN_UP_USER_EXISTS;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return ReturnCodes.CONNECTION_FAILURE;
        }

        try{
            String query = "INSERT INTO user (userName, password, active, createDate, createdBy, lastUpdateBy) " +
                    "values (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, 1);
            Timestamp saveDateTime = new Timestamp(System.currentTimeMillis());
            preparedStatement.setTimestamp(4, saveDateTime);
            preparedStatement.setString(5, username);
            preparedStatement.setString(6, username);

            preparedStatement.execute();
            return ReturnCodes.SUCCESS;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return ReturnCodes.STATEMENT_ERROR;
        } finally {
            connection.close();
        }
    }

}
