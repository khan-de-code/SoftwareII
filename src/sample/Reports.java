package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Reports implements Initializable {
    static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ObservableList displayedVals = FXCollections.observableArrayList();

    @FXML
    private RadioButton appointmentTypes;
    @FXML
    private RadioButton consultantSchedule;
    @FXML
    private RadioButton customerSchedule;

    @FXML
    private TableColumn<ReportsAppointmentTypes, String> apptTypes;
    @FXML
    private TableColumn<ReportsAppointmentTypes, Integer> count;
    @FXML
    private TableColumn<ReportsConsultantSched, String> consultantName;
    @FXML
    private TableColumn<ReportsConsultantSched, String> appointment;
    @FXML
    private TableColumn<ReportsConsultantSched, String> end;
    @FXML
    private TableColumn<ReportsCustomerApptCount, String> customer;
    @FXML
    private TableColumn<ReportsCustomerApptCount, Integer> numOfAppointments;
    @FXML
    private TableView reports;

    public static void start(ObservableList<Appointment> appointments) throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AddAppointment.class.getResource("Reports.fxml"));

        Reports reports = loader.getController();
        reports.appointments = appointments;

        Parent productViewParent = loader.load();

        Scene partViewScene = new Scene(productViewParent);

        window.setScene(partViewScene);
        window.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reports.getItems().clear();
        reports.getColumns().clear();

        apptTypes.setCellValueFactory(new PropertyValueFactory<>("appointmentName"));
        count.setCellValueFactory(new PropertyValueFactory<>("count"));
        consultantName.setCellValueFactory(new PropertyValueFactory<>("name"));
        appointment.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        numOfAppointments.setCellValueFactory(new PropertyValueFactory<>("count"));

        appointmentTypesBtn();
    }

    public void appointmentTypesBtn(){
        appointmentTypes.setSelected(true);
        consultantSchedule.setSelected(false);
        customerSchedule.setSelected(false);
        reports.getItems().clear();
        reports.getColumns().clear();

        Map<String, Integer> dictionary = new HashMap<String, Integer>();

        for (Appointment appointment: appointments) {
            dictionary.merge(appointment.getTitle(), 1, Integer::sum);
        }

        reports.getColumns().addAll(apptTypes, count);

        for (Map.Entry<String, Integer> entry : dictionary.entrySet()){
            reports.getItems().add(new ReportsAppointmentTypes(entry.getValue(), entry.getKey()));
        }
    }

    public void consultantSchedule(){
        appointmentTypes.setSelected(false);
        consultantSchedule.setSelected(true);
        customerSchedule.setSelected(false);
        reports.getItems().clear();
        reports.getColumns().clear();

        reports.getColumns().addAll(consultantName, appointment, end);

        for (Appointment appointment: appointments) {
            reports.getItems().add(new ReportsConsultantSched(appointment.getContact(), appointment.getStart().toString(),
                    appointment.getEnd().toString()));
        }
    }

    public void customerSchedule(){
        appointmentTypes.setSelected(false);
        consultantSchedule.setSelected(false);
        customerSchedule.setSelected(true);
        reports.getItems().clear();
        reports.getColumns().clear();

        Map<String, Integer> dictionary = new HashMap<String, Integer>();

        for (Appointment appointment: appointments) {
            dictionary.merge(appointment.getCustomerName(), 1, Integer::sum);
        }

        reports.getColumns().addAll(customer, numOfAppointments);

        for (Map.Entry<String, Integer> entry : dictionary.entrySet()){
            reports.getItems().add(new ReportsCustomerApptCount(entry.getValue(), entry.getKey()));
        }
    }
}
