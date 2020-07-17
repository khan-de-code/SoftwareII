package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateAppointment implements Initializable {
    private String loggedInUser;
    private long loggedInUserID;

    @FXML
    private TextField appointmentName;
    @FXML
    private TextField description;
    @FXML
    private TextField location;
    @FXML
    private TextField contact;
    @FXML
    private TextField type;
    @FXML
    private TextField url;
    @FXML
    private DatePicker startDate;
    @FXML
    private TextField startHour;
    @FXML
    private TextField startMin;
    @FXML
    private ComboBox<String> startAMPM;
    @FXML
    private DatePicker endDate;
    @FXML
    private TextField endHour;
    @FXML
    private TextField endMin;
    @FXML
    private ComboBox<String> endAMPM;

    public static void start(String loggedInUser, long loggedInUserID) throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UpdateAppointment.class.getResource("UpdateAppointment.fxml"));

        Parent productViewParent = loader.load();

        UpdateAppointment updateAppointment = loader.getController();
        updateAppointment.loggedInUser = loggedInUser;
        updateAppointment.loggedInUserID = loggedInUserID;

        Scene partViewScene = new Scene(productViewParent);

        window.setScene(partViewScene);
        window.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void adjustAppointmentBtnPushed(){

    }
}
