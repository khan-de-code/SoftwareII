package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class UpdateAppointment {
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
}
