package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class MainPageController implements Initializable {
    public String loggedInUser;
    public long loggedInUserID;

    private enum state {
        APPOINTMENTS,
        CUSTOMER_RECORDS
    }

    private state view = state.APPOINTMENTS;

    @FXML
    private Text pageTitle;

    @FXML
    private Button customerRecordsBtn;
    @FXML
    private Button appointmentsBtn;

    @FXML
    private ComboBox<String> weekMonthFilter;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchBtn;

    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deleteBtn;

    @FXML
    private TableView<Appointment> existingAppointmentTable;
    @FXML
    private TableColumn<Appointment, String> title;
    @FXML
    private TableColumn<Appointment, String> description;
    @FXML
    private TableColumn<Appointment, String> location;
    @FXML
    private TableColumn<Appointment, String> contact;
    @FXML
    private TableColumn<Appointment, String> type;
    @FXML
    private TableColumn<Appointment, String> urlCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> start;
    @FXML
    private TableColumn<Appointment, LocalDateTime> end;
    @FXML
    private TableColumn<Customer, String> name;

    private ObservableList<Appointment> appointments;
    private ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainPageControllerService mainPageControllerService = new MainPageControllerService();
        mainPageControllerService.init();
        RetVal retVal = mainPageControllerService.getAppointments();
        appointments = retVal.queryResultsAppointment;
        title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        description.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        location.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contact"));
        contact.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contact"));
        type.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        urlCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("url"));
        start.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("start"));
        end.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("end"));
        name.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));

        weekMonthFilter.getItems().add("View All Appointments");
        weekMonthFilter.getItems().add("View Calendar By Month");
        weekMonthFilter.getItems().add("View Calendar By Week");

        // lambda expression used here to listen for a change in the combobox value and do filter the current view
        // depending on the option chosen
        weekMonthFilter.valueProperty().addListener((options, oldValue, newValue) -> {

            if (newValue.equals("View All Appointments")){
                filteredAppointments.clear();

                for (Appointment appointment: appointments) {
                    filteredAppointments.add(appointment);
                }
                existingAppointmentTable.setItems(filteredAppointments);
            } else if (newValue.equals("View Calendar By Month")){
                filteredAppointments.clear();

                for (Appointment appointment: appointments) {

                    if(withinAMonth(appointment.getStart())){
                        filteredAppointments.add(appointment);
                    }
                }
                existingAppointmentTable.setItems(filteredAppointments);
            } else if (newValue.equals("View Calendar By Week")){
                filteredAppointments.clear();

                for (Appointment appointment: appointments) {
                    if(withinAWeek(appointment.getStart())){
                        filteredAppointments.add(appointment);
                    }
                }
                existingAppointmentTable.setItems(filteredAppointments);
            }
        });

        weekMonthFilter.getSelectionModel().selectFirst();

        existingAppointmentTable.setItems(filteredAppointments);


    }

    public void setLoggedInUser(String loggedInUser, long loggedInUserID){
        this.loggedInUser = loggedInUser;
        this.loggedInUserID = loggedInUserID;
    }

    public void customerRecordsBtnPushed(){
        if (view == state.CUSTOMER_RECORDS){
            return;
        }

        view = state.CUSTOMER_RECORDS;
        weekMonthFilter.setVisible(false);
        pageTitle.setText("Customer Management Portal");
    }

    public void appointmentsBtnPushed(){
        if (view == state.APPOINTMENTS){
            return;
        }

        view = state.APPOINTMENTS;
        weekMonthFilter.setVisible(true);
        pageTitle.setText("Appointment Management Portal");
    }

    public Boolean withinAWeek(LocalDateTime start){
        LocalDateTime now = LocalDateTime.now();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(now.atZone(ZoneId.of(TimeZone.getDefault().getID())).toInstant()));
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        calendar.setTime(Date.from(start.atZone(ZoneId.of(TimeZone.getDefault().getID())).toInstant()));
        int checkWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        if (currentWeek == checkWeek){
            return true;
        } else {
            return false;
        }
    }

    public Boolean withinAMonth(LocalDateTime start){
        LocalDateTime now = LocalDateTime.now();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(now.atZone(ZoneId.of(TimeZone.getDefault().getID())).toInstant()));
        int currentMonth = calendar.get(Calendar.MONTH);

        calendar.setTime(Date.from(start.atZone(ZoneId.of(TimeZone.getDefault().getID())).toInstant()));
        int checkMonth = calendar.get(Calendar.MONTH);

        if (currentMonth == checkMonth){
            return true;
        } else {
            return false;
        }
    }

    public void refreshFilter(String newValue){
        if (newValue.equals("View All Appointments")){
            filteredAppointments.clear();

            for (Appointment appointment: appointments) {
                filteredAppointments.add(appointment);
            }
            existingAppointmentTable.setItems(filteredAppointments);
        } else if (newValue.equals("View Calendar By Month")){
            filteredAppointments.clear();

            for (Appointment appointment: appointments) {

                if(withinAMonth(appointment.getStart())){
                    filteredAppointments.add(appointment);
                }
            }
            existingAppointmentTable.setItems(filteredAppointments);
        } else if (newValue.equals("View Calendar By Week")){
            filteredAppointments.clear();

            for (Appointment appointment: appointments) {
                if(withinAWeek(appointment.getStart())){
                    filteredAppointments.add(appointment);
                }
            }
            existingAppointmentTable.setItems(filteredAppointments);
        }
    }

    public void searchBtnPushed(){
        if (searchField.getText().equals("")){
            refreshFilter(weekMonthFilter.getValue());
            return;
        }

        filteredAppointments.removeIf(appointment -> !appointment.getCustomerName().toLowerCase()
                .equals(searchField.getText().toLowerCase()));

        existingAppointmentTable.setItems(filteredAppointments);
    }

    public void addBtnPushed() throws IOException {
        AddAppointment.start(loggedInUser, loggedInUserID);
        MainPageControllerService mainPageControllerService = new MainPageControllerService();
        mainPageControllerService.init();
        RetVal retVal = mainPageControllerService.getAppointments();
        appointments = retVal.queryResultsAppointment;

        existingAppointmentTable.setItems(appointments);
    }

    public void updateBtnPushed() throws IOException {

    }
}
