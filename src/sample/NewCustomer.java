package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class NewCustomer implements Initializable{ //TODO: Add German
    @FXML
    public TextField name;
    @FXML
    public TextField address;
    @FXML
    public TextField address2;
    @FXML
    public TextField city;
    @FXML
    public TextField postalCode;
    @FXML
    public TextField country;
    @FXML
    public TextField phone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AddAppointment.numberOnlyFieldListener(phone, postalCode);
    }
}
