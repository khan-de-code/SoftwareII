package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateAppointment implements Initializable {
    private String loggedInUser;
    private long loggedInUserID;
    private Appointment selectedAppointment;

    @FXML
    private TextField appointmentName;
    private String origAppointmentName;
    @FXML
    private TextField description;
    private String origDescription;
    @FXML
    private TextField location;
    private String origLocation;
    @FXML
    private TextField contact;
    private String origContact;
    @FXML
    private TextField type;
    private String origType;
    @FXML
    private TextField url;
    private String origUrl;
    @FXML
    private DatePicker startDate;
    private String origStartDate;
    @FXML
    private TextField startHour;
    private String origStartHour;
    @FXML
    private TextField startMin;
    private String origStartMin;
    @FXML
    private ComboBox<String> startAMPM;
    private String origStartAMPM;
    @FXML
    private DatePicker endDate;
    private String origEndDate;
    @FXML
    private TextField endHour;
    private String origEndHour;
    @FXML
    private TextField endMin;
    private String origEndMin;
    @FXML
    private ComboBox<String> endAMPM;
    private String origEndAMPM;

    @FXML
    private Text requiredFieldsMissing;

    public static void start(String loggedInUser, long loggedInUserID, Appointment selectedAppointment) throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UpdateAppointment.class.getResource("UpdateAppointment.fxml"));

        Parent productViewParent = loader.load();

        UpdateAppointment updateAppointment = loader.getController();
        updateAppointment.loggedInUser = loggedInUser;
        updateAppointment.loggedInUserID = loggedInUserID;
        updateAppointment.selectedAppointment = selectedAppointment;
        updateAppointment.appointmentName.setText(selectedAppointment.getTitle());
        updateAppointment.origAppointmentName = selectedAppointment.getTitle();
        updateAppointment.description.setText(selectedAppointment.getDescription());
        updateAppointment.origDescription = selectedAppointment.getDescription();
        updateAppointment.location.setText(selectedAppointment.getDescription());
        updateAppointment.origLocation = selectedAppointment.getLocation();
        updateAppointment.contact.setText(selectedAppointment.getContact());
        updateAppointment.origContact = selectedAppointment.getContact();
        updateAppointment.type.setText(selectedAppointment.getType());
        updateAppointment.origType = selectedAppointment.getType();
        updateAppointment.url.setText(selectedAppointment.getUrl());
        updateAppointment.origUrl = selectedAppointment.getUrl();
        updateAppointment.startDate.setValue(LocalDate.from(selectedAppointment.getStart()));
        updateAppointment.origStartDate = LocalDate.from(selectedAppointment.getStart()) + "";
        updateAppointment.startHour.setText(selectedAppointment.getStart().getHour() + "");
        updateAppointment.origStartHour = selectedAppointment.getStart().getHour() + "";
        updateAppointment.startMin.setText(selectedAppointment.getStart().getMinute() + "");
        updateAppointment.origStartMin = selectedAppointment.getStart().getMinute() + "";
        updateAppointment.startAMPM.setValue(ampm(selectedAppointment.getStart()));
        updateAppointment.origStartAMPM = ampm(selectedAppointment.getStart());
        updateAppointment.endDate.setValue(LocalDate.from(selectedAppointment.getEnd()));
        updateAppointment.origEndDate = LocalDate.from(selectedAppointment.getEnd()) + "";
        updateAppointment.endHour.setText(selectedAppointment.getEnd().getHour() + "");
        updateAppointment.origEndHour = selectedAppointment.getEnd().getHour() + "";
        updateAppointment.endMin.setText(selectedAppointment.getEnd().getMinute() + "");
        updateAppointment.origEndMin = selectedAppointment.getEnd().getMinute() + "";
        updateAppointment.endAMPM.setValue(ampm(selectedAppointment.getEnd()));
        updateAppointment.origEndAMPM = ampm(selectedAppointment.getEnd());

        Scene partViewScene = new Scene(productViewParent);

        window.setScene(partViewScene);
        window.showAndWait();
    }

    private static String ampm(ZonedDateTime localDateTime) {

        return (localDateTime.getHour() < 13 ? "AM" : "PM");
    }

    static void numberOnlyFieldListener(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberOnlyFieldListener(startHour, startMin, endHour, endMin);

        startAMPM.getItems().addAll("AM", "PM");
        endAMPM.getItems().addAll("AM", "PM");

        startDate.getEditor().setDisable(true);
        endDate.getEditor().setDisable(true);

        requiredFieldsMissing.setVisible(false);
    }

    private Boolean requiredFieldsMissing(Object... fields){
        Boolean retVal = false;
        for(Object field : fields) {
            if (field instanceof TextField){
                if (((TextField) field).getText().equals("")){
                    retVal = true;
                    break;
                }
            } else if (field instanceof ComboBox){
                if (((ComboBox) field).getValue() == null){
                    retVal = true;
                    break;
                }

            } else if (field instanceof DatePicker){
                if (((DatePicker) field).getValue() == null){
                    retVal = true;
                    break;
                }
            }
        }

        return retVal;
    }

    public void adjustAppointmentBtnPushed() {
        // TODO add data checking as in AddAppointment with red borders etc.
        Object[] fields = {
                appointmentName, description, location, contact, type, url, startHour, startMin, startMin,
                startAMPM, endHour, endMin, endAMPM
        };

        if (requiredFieldsMissing(fields)){
            requiredFieldsMissing.setVisible(true);
            for (Object field : fields) {
                if (requiredFieldsMissing(field)) {
                    if (field instanceof TextField) {
                        ((TextField) field).setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
                    } else if (field instanceof ComboBox) {
                        ((ComboBox) field).setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");
                    } else if (field instanceof DatePicker) {
                        ((DatePicker) field).setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");
                    }
                } else {
                    if (field instanceof TextField) {
                        ((TextField) field).setStyle(null);
                    } else if (field instanceof ComboBox) {
                        ((ComboBox) field).setStyle(null);
                    } else if (field instanceof DatePicker) {
                        ((DatePicker) field).setStyle(null);
                    }
                }
            }
            return;
        } else {
            requiredFieldsMissing.setVisible(false);

            for (Object field : fields) {
                if (requiredFieldsMissing(field)) {
                    if (field instanceof TextField) {
                        ((TextField) field).setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
                    } else if (field instanceof ComboBox) {
                        ((ComboBox) field).setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");
                    } else if (field instanceof DatePicker) {
                        ((DatePicker) field).setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");
                    }
                } else {
                    if (field instanceof TextField) {
                        ((TextField) field).setStyle(null);
                    } else if (field instanceof ComboBox) {
                        ((ComboBox) field).setStyle(null);
                    } else if (field instanceof DatePicker) {
                        ((DatePicker) field).setStyle(null);
                    }
                }
            }
        }

        List<adjApptObj> changedVals = new ArrayList<>();

        if (!origAppointmentName.equals(appointmentName.getText())) {
            changedVals.add(new adjApptObj("title", appointmentName.getText()));
        }
        if (!origDescription.equals(description.getText())) {
            changedVals.add(new adjApptObj("description", description.getText()));
        }
        if (!origLocation.equals(location.getText())) {
            changedVals.add((new adjApptObj("location", location.getText())));
        }
        if (!origContact.equals(contact.getText())) {
            changedVals.add(new adjApptObj("contact", contact.getText()));
        }
        if (!origType.equals(type.getText())) {
            changedVals.add(new adjApptObj("type", type.getText()));
        }
        if (!origUrl.equals(url.getText())) {
            changedVals.add(new adjApptObj("url", url.getText()));
        }
        if (
                !origStartDate.equals(startDate.getValue().toString())
                        || !origStartHour.equals(startHour.getText())
                        || !origStartMin.equals(startMin.getText())
                        || !origStartAMPM.equals(startAMPM.getValue())
        ) {
            changedVals.add(new adjApptObj("startDate", new DateTimeValidation().generateDateTimeString(startDate, startHour, startMin, startAMPM)));
        }
        if (
                !origEndDate.equals(endDate.getValue().toString())
                        || !origEndHour.equals(endHour.getText())
                        || !origEndMin.equals(endMin.getText())
                        || !origStartAMPM.equals(endAMPM.getValue())
        ) {
            changedVals.add(new adjApptObj("endDate", new DateTimeValidation().generateDateTimeString(endDate, startHour, startMin, startAMPM)));
        }

        if (changedVals.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Changes Made");
            alert.setHeaderText("No Changes Made");
            alert.setContentText("You have just attempted to update an appointment without changing any of the appointment's " +
                    "details. Please review the original appointment details and make an update before clicking the 'Adjust Appointment' button.");
            alert.showAndWait();
        } else {
            UpdateAppointmentService updateAppointmentService = new UpdateAppointmentService();
            updateAppointmentService.init();
            ReturnCodes updateResults = updateAppointmentService.updateAppointment(selectedAppointment.getAppointmentId(), changedVals, loggedInUser, loggedInUserID);
            if (updateResults == ReturnCodes.SUCCESS){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Successful");
                alert.setHeaderText("Update Successful");
                alert.setContentText("The appointment update has been successfully saved!");
                alert.showAndWait();

                Stage window = (Stage) appointmentName.getScene().getWindow();
                window.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Error In Saving Appointment");
                alert.setContentText("There has been an error in saving your appointment. Please try again.");
                alert.showAndWait();
            }
        }
    }
}
