package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class AddAppointment implements Initializable { //TODO: Add German
    private String loggedInUser;
    private long loggedInUserID;

    @FXML
    private HBox changeable;

    @FXML
    private ToggleGroup customerType;
    @FXML
    private RadioButton newCustomer;
    @FXML
    private RadioButton existingCustomer;

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
    @FXML
    private Text requiredFieldsMissing;


    private NewCustomer newCustomerController;
    private FXMLLoader loadNewCustomer;
    private GridPane newCustomerPane;
    private ExistingCustomer existingCustomerController;
    private FXMLLoader loadExistingCustomer;
    private BorderPane existingCustomerPane;

    public static void start(String loggedInUser, long loggedInUserID) throws IOException{
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AddAppointment.class.getResource("Add_Appointment.fxml"));

        Parent productViewParent = loader.load();

        AddAppointment addAppointment = loader.getController();
        addAppointment.loggedInUser = loggedInUser;
        addAppointment.loggedInUserID = loggedInUserID;

        Scene partViewScene = new Scene(productViewParent);

        window.setScene(partViewScene);
        window.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNewCustomer = new FXMLLoader(getClass().getResource("New_Customer.fxml"));
            newCustomerPane = loadNewCustomer.load();
            newCustomerController = loadNewCustomer.<NewCustomer>getController();

            loadExistingCustomer = new FXMLLoader(getClass().getResource("ExistingCustomer.fxml"));
            existingCustomerPane = loadExistingCustomer.load();
            existingCustomerController = loadExistingCustomer.<ExistingCustomer>getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        changeable.getChildren().add(newCustomerPane);
        newCustomer.setSelected(true);

        numberOnlyFieldListener(startHour, startMin, endHour, endMin);
        startAMPM.getItems().addAll("AM", "PM");
        endAMPM.getItems().addAll("AM", "PM");

        startDate.getEditor().setDisable(true);
        endDate.getEditor().setDisable(true);

    }

    static void numberOnlyFieldListener(TextField... textFields) {
        for (TextField textField : textFields){
            textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
        }
    }

    public void newCustomerRadioSelected(){
        existingCustomer.setSelected(false);
        newCustomer.setSelected(true);
        changeable.getChildren().removeAll(existingCustomerPane);
        changeable.getChildren().add(newCustomerPane);
    }

    public void existingCustomerRadioSelected(){
        existingCustomer.setSelected(true);
        newCustomer.setSelected(false);
        changeable.getChildren().removeAll(newCustomerPane);
        changeable.getChildren().add(existingCustomerPane);
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

    public void saveAppointment(){
        AddAppointmentService addAppointmentService = new AddAppointmentService();
        DateTimeValidation dateTimeValidation = new DateTimeValidation();
        String startDateTime = dateTimeValidation.generateDateTimeString(startDate, startHour, startMin, startAMPM);
        String endDateTime = dateTimeValidation.generateDateTimeString(endDate, endHour, endMin, endAMPM);

        if (newCustomer.isSelected()) {
            addAppointmentService.init(loggedInUser, loggedInUserID, null, newCustomerController.name.getText(), newCustomerController.address.getText(),
                    newCustomerController.address2.getText(), newCustomerController.city.getText(),
                    newCustomerController.postalCode.getText(), newCustomerController.country.getText(), newCustomerController.phone.getText(),
                    appointmentName.getText(), description.getText(), location.getText(), contact.getText(), type.getText(),
                    url.getText(), startDateTime, endDateTime);

            ArrayList createCustomerResults = addAppointmentService.createCustomer(true);
            if (createCustomerResults.get(0) == ReturnCodes.CONNECTION_FAILURE) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Failure");
                alert.setHeaderText("Application Connection Failure");
                alert.setContentText("In the attempt to save your newly created appointment, the application encountered a" +
                        "connection issue. Please click \"Create Appointment\" again.");
                alert.showAndWait();
                return;
            } else if (createCustomerResults.get(0) == ReturnCodes.OVERLAPPING_APPOINTMENT) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Overlapping Appointment");
                alert.setHeaderText("Overlapping Appointment");
                alert.setContentText("The appointment time entered overlaps with an existing appointment. Please review " +
                        "the open appointment times and adjust your desired appointment time accordingly. Once finished, " +
                        "please click \"Create Appointment\" again.");
                alert.showAndWait();
                return;
            }

            ReturnCodes createAppointmentResults = addAppointmentService.createAppointment((Long) createCustomerResults.get(1));
            if (createAppointmentResults == ReturnCodes.CONNECTION_FAILURE) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Failure");
                alert.setHeaderText("Application Connection Failure");
                alert.setContentText("In the attempt to save your newly created appointment, the application encountered a" +
                        "connection issue. Please click \"Create Appointment\" again.");
                alert.showAndWait();
            } else if (createAppointmentResults == ReturnCodes.SUCCESS) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Appointment Creation Confirmation");
                alert.setContentText("The appointment you created has been successfully saved!");
                alert.showAndWait();

                Stage window = (Stage) appointmentName.getScene().getWindow();
                window.close();
            }
        } else {
            Customer customer = existingCustomerController.existingCustomerTable.getSelectionModel().getSelectedItem();
            addAppointmentService.init(loggedInUser, loggedInUserID, (long) customer.getCustomerId(),
                    null, null, null, null,
                    null, null, null, appointmentName.getText(),
                    description.getText(), location.getText(), contact.getText(), type.getText(), url.getText(),
                    startDateTime, endDateTime);

            ReturnCodes createAppointmentResults = addAppointmentService.createAppointment((long) customer.getCustomerId());

            if (createAppointmentResults == ReturnCodes.OVERLAPPING_APPOINTMENT) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Overlapping Appointment");
                alert.setHeaderText("Overlapping Appointment");
                alert.setContentText("The appointment time entered overlaps with an existing appointment. Please review " +
                        "the open appointment times and adjust your desired appointment time accordingly. Once finished, " +
                        "please click \"Create Appointment\" again.");
                alert.showAndWait();

            } else if (createAppointmentResults == ReturnCodes.CONNECTION_FAILURE) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Failure");
                alert.setHeaderText("Application Connection Failure");
                alert.setContentText("In the attempt to save your newly created appointment, the application encountered a" +
                        "connection issue. Please click \"Create Appointment\" again.");
                alert.showAndWait();

            } else if (createAppointmentResults == ReturnCodes.SUCCESS) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Appointment Creation Confirmation");
                alert.setContentText("The appointment you created has been successfully saved!");
                alert.showAndWait();

                Stage window = (Stage) appointmentName.getScene().getWindow();
                window.close();
            }

        }

        addAppointmentService.closeConnection();
    }

    public void createAppointmentBtnPushed() throws ParseException {
        if (newCustomer.isSelected()) {
            Object[] fields = {
                    newCustomerController.name, newCustomerController.address, newCustomerController.city,
                    newCustomerController.postalCode, newCustomerController.country, newCustomerController.phone,
                    appointmentName, description, location, contact, type, url, startDate, startHour, startMin, startAMPM,
                    endDate, endHour, endMin, endAMPM
            };

            if (requiredFieldsMissing(fields)) {
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
                    if (field instanceof TextField) {
                        ((TextField) field).setStyle(null);
                    } else if (field instanceof ComboBox) {
                        ((ComboBox) field).setStyle(null);
                    } else if (field instanceof DatePicker) {
                        ((DatePicker) field).setStyle(null);
                    }
                }

                DateTimeValidation validate = new DateTimeValidation();
                if (!validate.checkHours(startHour)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Entry");
                    alert.setHeaderText("Invalid Hours Entry");
                    alert.setContentText("The number submitted for number of hours is greater than 12. Please re-enter " +
                            "the start or end time and click \"Create Appointment\" again.");
                    alert.showAndWait();
                    return;
                }

                if (!validate.checkHours(endHour)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Entry");
                    alert.setHeaderText("Invalid Hours Entry");
                    alert.setContentText("The number submitted for number of hours is greater than 12. Please re-enter " +
                            "the start or end time and click \"Create Appointment\" again.");
                    alert.showAndWait();
                    return;
                }

                if (!validate.checkMins(startMin)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Entry");
                    alert.setHeaderText("Invalid Minutes Entry");
                    alert.setContentText("The number submitted for number of hours is greater than 59. Please re-enter " +
                            "the start or end time and click \"Create Appointment\" again.");
                    alert.showAndWait();
                    return;
                }

                if (!validate.checkMins(endMin)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Entry");
                    alert.setHeaderText("Invalid Minutes Entry");
                    alert.setContentText("The number submitted for number of hours is greater than 59. Please re-enter " +
                            "the start or end time and click \"Create Appointment\" again.");
                    alert.showAndWait();
                    return;
                }

                if (validate.compare(startDate, startHour, startMin, startAMPM, endDate, endHour, endMin, endAMPM)) {
                    saveAppointment();
                }
            }
        } else {
            Object[] fields = {
                    appointmentName, description, location, contact, type, url, startDate, startHour, startMin, startAMPM,
                    endDate, endHour, endMin, endAMPM
            };

            if (requiredFieldsMissing(fields)) {
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
                    if (field instanceof TextField) {
                        ((TextField) field).setStyle(null);
                    } else if (field instanceof ComboBox) {
                        ((ComboBox) field).setStyle(null);
                    } else if (field instanceof DatePicker) {
                        ((DatePicker) field).setStyle(null);
                    }
                }

                DateTimeValidation validate = new DateTimeValidation();
                if (!validate.checkHours(startHour)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Entry");
                    alert.setHeaderText("Invalid Hours Entry");
                    alert.setContentText("The number submitted for number of hours is greater than 12. Please re-enter " +
                            "the start or end time and click \"Create Appointment\" again.");
                    alert.showAndWait();
                    return;
                }

                if (!validate.checkHours(endHour)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Entry");
                    alert.setHeaderText("Invalid Hours Entry");
                    alert.setContentText("The number submitted for number of hours is greater than 12. Please re-enter " +
                            "the start or end time and click \"Create Appointment\" again.");
                    alert.showAndWait();
                    return;
                }

                if (!validate.checkMins(startMin)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Entry");
                    alert.setHeaderText("Invalid Minutes Entry");
                    alert.setContentText("The number submitted for number of hours is greater than 59. Please re-enter " +
                            "the start or end time and click \"Create Appointment\" again.");
                    alert.showAndWait();
                    return;
                }

                if (!validate.checkMins(endMin)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Entry");
                    alert.setHeaderText("Invalid Minutes Entry");
                    alert.setContentText("The number submitted for number of hours is greater than 59. Please re-enter " +
                            "the start or end time and click \"Create Appointment\" again.");
                    alert.showAndWait();
                    return;
                }

                if (validate.compare(startDate, startHour, startMin, startAMPM, endDate, endHour, endMin, endAMPM)) {
                    saveAppointment();
                }
            }
        }
    }
}
