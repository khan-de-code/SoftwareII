package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Stage stage;

    @FXML
    private Text topText;
    @FXML
    private Text username;
    @FXML
    private TextField usernameInput;
    @FXML
    private Text password;
    @FXML
    private TextField passwordInput;
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;

    private String loginError = "Login Error!";
    private String loginErrorEmptyHeader = "Attempted Login With No Username Or Password";
    private String loginErrorEmptyContent = "You just attempted to login without entering a username or password. " +
            "Please check the your entries and attempt to login again.";

    private String loginErrorMismatchHeader = "Username Password Mismatch";
    private String loginErrorMismatchContent = "The username and password provided do not match any existing user. " +
            "Please try again or if you do not have an account, please create one.";

    private String login_signupErrorSQLHeader = "Bad Database Request";
    private String login_signupErrorSQLContent = "There was an error establishing a connection with the login server. Please " +
            "try again.";

    private String login_signupErrorLengthHeader = "Character Count Exceeds Limit";
    private String login_signupErrorLengthContent = "Your username or password exceeds the maximum number of characters. Please " +
            "ensure your username is not longer than 40 characters and your password is not longer than 50 characters.";

    private String signUpError = "Sign Up Error!";
    private String signUpErrorEmptyHeader = "Attempted Sign Up With No Username Or Password";
    private String signUpErrorEmptyContent = "You just attempted to sign up without entering a username or password. " +
            "Please check the your entries and attempt to login again.";

    private String signUpSuccess = "Success!";
    private String signUpSuccessHeader = "Sign Up Successful!";
    private String signUpSuccessContent = "Your sign up has been successful. Please feel free to log in.";

    private String signUpErrorUserExistsHeader = "Entered Username Already Exists";
    private String signUpErrorUserExistsContent = "The username provided already exists. Please enter a new username " +
            "and try again";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale currentLocale = Locale.getDefault();
        if (currentLocale.getLanguage() == Locale.GERMAN.getLanguage()){
            topText.setText("Ablaufplanung");
            username.setText("Nutzername");
            password.setText("Passwort");
            loginButton.setText("Einloggen");
            signupButton.setText("Anmelden");

            loginError = "Login Fehler!";
            loginErrorEmptyHeader = "Anmeldeversuch ohne Benutzername oder Passwort";
            loginErrorEmptyContent = "Sie haben gerade versucht, sich anzumelden, ohne einen Benutzernamen oder ein " +
                    "Passwort einzugeben. Bitte überprüfen Sie Ihre Eingaben und versuchen Sie erneut, sich anzumelden.";

            loginErrorMismatchHeader = "Nicht übereinstimmender Benutzername und Passwort";
            loginErrorMismatchContent = "Der angegebene Benutzername und das Passwort stimmen mit keinem vorhandenen " +
                    "Benutzer überein. Bitte versuchen Sie es erneut oder erstellen Sie ein Konto, wenn Sie kein Konto haben.";

            login_signupErrorSQLHeader = "Ungültige Datenbankanforderung";
            login_signupErrorSQLContent = "Hier ist ein Fehler beim Herstellen einer Verbindung mit dem Anmeldeserver " +
                    "aufgetreten. Bitte versuche es erneut.";

            login_signupErrorLengthHeader = "Die Anzahl der Zeichen überschreitet das Limit";
            login_signupErrorLengthContent = "Unser Benutzername oder Passwort überschreitet die maximale Anzahl von Zeichen." +
                    " Bitte stellen Sie sicher, dass Ihr Benutzername nicht länger als 40 Zeichen und Ihr Passwort" +
                    " nicht länger als 50 Zeichen ist.";

            signUpError = "Anmeldefehler!";
            signUpErrorEmptyHeader = "Versuch, sich ohne Benutzernamen oder Passwort anzumelden";
            signUpErrorEmptyContent = "Sie haben gerade versucht, p zu signieren, ohne einen Benutzernamen oder ein " +
                    "Passwort einzugeben. Bitte überprüfen Sie Ihre Eingaben und versuchen Sie erneut, sich anzumelden.";

            signUpSuccess = "Erfolg!";
            signUpSuccessHeader = "Registrierung erfolgreich!";
            signUpSuccessContent = "Ihre Anmeldung war erfolgreich. Bitte melden Sie sich an.";

            signUpErrorUserExistsHeader = "Eingegebener Benutzername existiert bereits";
            signUpErrorUserExistsContent = "Der angegebene Benutzername ist bereits vorhanden. Bitte geben Sie einen " +
                    "neuen Benutzernamen ein und versuchen Sie es erneut";

        }
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void login() throws SQLException, IOException {
        usernameInput.setText(usernameInput.getText().trim());
        passwordInput.setText(passwordInput.getText().trim());

        if (usernameInput.getText().isEmpty() || passwordInput.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(loginError);
            alert.setHeaderText(loginErrorEmptyHeader);
            alert.setContentText(loginErrorEmptyContent);
            alert.showAndWait();
            return;
        } else if (usernameInput.getText().length() > 40 || usernameInput.getText().length() > 50){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(loginError);
            alert.setHeaderText(login_signupErrorLengthHeader);
            alert.setContentText(login_signupErrorLengthContent);
            alert.showAndWait();
            return;
        }

        Login_SignUpService login = new Login_SignUpService(usernameInput.getText(), passwordInput.getText());
        ArrayList loginResults = login.attemptLogin();
        ReturnCodes loginResult = (ReturnCodes) loginResults.get(0);

        if (loginResult == ReturnCodes.USER_NOT_FOUND){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(loginError);
            alert.setHeaderText(loginErrorMismatchHeader);
            alert.setContentText(loginErrorMismatchContent);
            alert.showAndWait();
            return;
        } else if (loginResult == ReturnCodes.STATEMENT_ERROR ||
                loginResult == ReturnCodes.CONNECTION_FAILURE){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(loginError);
            alert.setHeaderText(login_signupErrorSQLHeader);
            alert.setContentText(login_signupErrorSQLContent);
            alert.showAndWait();
            return;
        } else if (loginResult == ReturnCodes.SUCCESS){
            File loginLog = new File("user_logins.log");
            FileWriter csvWriter = null;

            try {
                if (!loginLog.exists()){
                    csvWriter = new FileWriter("user_logins.log");
                    csvWriter.append("User,Timestamp,Time Zone\n");
                    csvWriter.append(usernameInput.getText());
                    csvWriter.append(",");
                    csvWriter.append(java.time.Clock.systemUTC().instant().toString());
                    csvWriter.append(",");
                    csvWriter.append(ZoneId.systemDefault().toString());
                    csvWriter.append("\n");
                } else {
                    csvWriter = new FileWriter("user_logins.log", true);
                    csvWriter.append(usernameInput.getText());
                    csvWriter.append(",");
                    csvWriter.append(java.time.Clock.systemUTC().instant().toString());
                    csvWriter.append(",");
                    csvWriter.append(ZoneId.systemDefault().toString());
                    csvWriter.append("\n");
                }

            } finally {
                csvWriter.close();
            }

            stage = (Stage) usernameInput.getScene().getWindow();

            stage.setMinHeight(800);
            stage.setMinWidth(1200);

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainPageController.class.getResource("Main_Page.fxml"));

            Parent root = loader.load();

            MainPageController mainPageController = loader.getController();
            mainPageController.setLoggedInUser(usernameInput.getText(), (Long) loginResults.get(1));

            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public void signUp() throws SQLException {
        usernameInput.setText(usernameInput.getText().trim());
        passwordInput.setText(passwordInput.getText().trim());

        if (usernameInput.getText().isEmpty() || passwordInput.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(signUpError);
            alert.setHeaderText(signUpErrorEmptyHeader);
            alert.setContentText(signUpErrorEmptyContent);
            alert.showAndWait();
            return;
        } else if (usernameInput.getText().trim().length() > 40 || usernameInput.getText().trim().length() > 50){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(signUpError);
            alert.setHeaderText(login_signupErrorLengthHeader);
            alert.setContentText(login_signupErrorLengthContent);
            alert.showAndWait();
            return;
        }

        Login_SignUpService signUp = new Login_SignUpService(usernameInput.getText(), passwordInput.getText());
        ReturnCodes signupResult = signUp.attemptSignUp();

        if (signupResult == ReturnCodes.STATEMENT_ERROR ||
                signupResult == ReturnCodes.CONNECTION_FAILURE) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(signUpError);
            alert.setHeaderText(login_signupErrorLengthHeader);
            alert.setContentText(login_signupErrorLengthContent);
            alert.showAndWait();
            return;
        } else if (signupResult == ReturnCodes.SUCCESS){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(signUpSuccess);
            alert.setHeaderText(signUpSuccessHeader);
            alert.setContentText(signUpSuccessContent);
            alert.showAndWait();
            return;
        } else if (signupResult == ReturnCodes.SIGN_UP_USER_EXISTS){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(signUpError);
            alert.setHeaderText(signUpErrorUserExistsHeader);
            alert.setContentText(signUpErrorUserExistsContent);
            alert.showAndWait();
            return;
        }
    }
}
