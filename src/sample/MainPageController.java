package sample;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
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
    private ComboBox<String> weekMonthFilter;
    @FXML
    private TextField searchField;

    @FXML
    private TableView existingAppointmentTable;
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
    private TableColumn<Appointment, String> name;

    @FXML
    private TableColumn<Customer, String> customerName;
    @FXML
    private TableColumn<Customer, String> address;
    @FXML
    private TableColumn<Customer, String> address2;
    @FXML
    private TableColumn<Customer, String> city;
    @FXML
    private TableColumn<Customer, String> zip;
    @FXML
    private TableColumn<Customer, String> country;

    private ObservableList<Customer> customers;
    private ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointments;
    private ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainPageControllerService mainPageControllerService = new MainPageControllerService();
        mainPageControllerService.init();
        RetVal retVal = mainPageControllerService.getAppointments();
        appointments = retVal.queryResultsAppointment;
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        location.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        name.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        customerName.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(6));
        address.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(6));
        address2.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(6));
        city.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(6));
        zip.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(6));
        country.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(6));

        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        address2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        zip.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));

        weekMonthFilter.getItems().add("View All Appointments");
        weekMonthFilter.getItems().add("View Calendar By Month");
        weekMonthFilter.getItems().add("View Calendar By Week");

        // lambda expression used here to listen for a change in the combobox value and do filter the current view
        // depending on the option chosen
        weekMonthFilter.valueProperty().addListener((options, oldValue, newValue) -> {

            switch (newValue) {
                case "View All Appointments" -> {
                    filteredAppointments.clear();
                    filteredAppointments.addAll(appointments);
                    existingAppointmentTable.setItems(filteredAppointments);
                }
                case "View Calendar By Month" -> {
                    filteredAppointments.clear();
                    for (Appointment appointment : appointments) {

                        if (withinAMonth(appointment.getStart())) {
                            filteredAppointments.add(appointment);
                        }
                    }
                    existingAppointmentTable.setItems(filteredAppointments);
                }
                case "View Calendar By Week" -> {
                    filteredAppointments.clear();
                    for (Appointment appointment : appointments) {
                        if (withinAWeek(appointment.getStart())) {
                            filteredAppointments.add(appointment);
                        }
                    }
                    existingAppointmentTable.setItems(filteredAppointments);
                }
            }
        });

        weekMonthFilter.getSelectionModel().selectFirst();

        existingAppointmentTable.getColumns().clear();
        existingAppointmentTable.getColumns().addAll(name, title, description, location, contact, type, urlCol, start, end);
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

        existingAppointmentTable.getItems().clear();
        existingAppointmentTable.getColumns().clear();

        ExistingCustomerService existingCustomerService = new ExistingCustomerService();
        existingCustomerService.init();
        RetVal results = existingCustomerService.getCustomers();

        if (results.status == ReturnCodes.CONNECTION_FAILURE) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Failure");
            alert.setHeaderText("Application Connection Failure");
            alert.setContentText("In the attempt to access the database an error occurred. Please try to close the add" +
                    "appointment screen then reopen it. If the error persists, please contact support.");
            alert.showAndWait();
        } else {
            customers = results.queryResults;
            filteredCustomers.addAll(customers);
        }

        existingAppointmentTable.getColumns().addAll(customerName, address, address2, city, zip, country);
        existingAppointmentTable.setItems(filteredCustomers);
    }

    public void appointmentsBtnPushed(){
        if (view == state.APPOINTMENTS){
            return;
        }

        view = state.APPOINTMENTS;
        weekMonthFilter.setVisible(true);
        pageTitle.setText("Appointment Management Portal");

        existingAppointmentTable.getItems().clear();
        existingAppointmentTable.getColumns().clear();

        existingAppointmentTable.getColumns().addAll(name, title, description, location, contact, type, urlCol, start, end);
        refreshFilter("View All Appointments");
        existingAppointmentTable.setItems(filteredAppointments);
    }

    public Boolean withinAWeek(LocalDateTime start){
        LocalDateTime now = LocalDateTime.now();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(now.atZone(ZoneId.of(TimeZone.getDefault().getID())).toInstant()));
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        calendar.setTime(Date.from(start.atZone(ZoneId.of(TimeZone.getDefault().getID())).toInstant()));
        int checkWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        return currentWeek == checkWeek;
    }

    public Boolean withinAMonth(LocalDateTime start){
        LocalDateTime now = LocalDateTime.now();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(now.atZone(ZoneId.of(TimeZone.getDefault().getID())).toInstant()));
        int currentMonth = calendar.get(Calendar.MONTH);

        calendar.setTime(Date.from(start.atZone(ZoneId.of(TimeZone.getDefault().getID())).toInstant()));
        int checkMonth = calendar.get(Calendar.MONTH);

        return currentMonth == checkMonth;
    }

    public void refreshFilter(String newValue){

        switch (newValue) {
            case "View All Appointments" -> {
                filteredAppointments.clear();
                filteredAppointments.addAll(appointments);
                existingAppointmentTable.setItems(filteredAppointments);
            }
            case "View Calendar By Month" -> {
                filteredAppointments.clear();
                for (Appointment appointment : appointments) {

                    if (withinAMonth(appointment.getStart())) {
                        filteredAppointments.add(appointment);
                    }
                }
                existingAppointmentTable.setItems(filteredAppointments);
            }
            case "View Calendar By Week" -> {
                filteredAppointments.clear();
                for (Appointment appointment : appointments) {
                    if (withinAWeek(appointment.getStart())) {
                        filteredAppointments.add(appointment);
                    }
                }
                existingAppointmentTable.setItems(filteredAppointments);
            }
        }
    }

    public void searchBtnPushed(){
        if (view == state.APPOINTMENTS) {
            if (searchField.getText().equals("")){
                refreshFilter(weekMonthFilter.getValue());
                return;
            }

            filteredAppointments.removeIf(appointment -> !appointment.getCustomerName().toLowerCase()
                    .equals(searchField.getText().toLowerCase()));
            existingAppointmentTable.setItems(filteredAppointments);
        } else {
            if (searchField.getText().equals("")){
                filteredCustomers.addAll(customers);
                existingAppointmentTable.setItems(filteredCustomers);
                return;
            }

            filteredCustomers.removeIf(customer -> !customer.getCustomerName().toLowerCase()
                    .equals(searchField.getText().toLowerCase()));
            existingAppointmentTable.setItems(filteredCustomers);
        }


    }

    private void refreshMainPageAppointments(){
        if (view == state.APPOINTMENTS) {
            MainPageControllerService mainPageControllerService = new MainPageControllerService();
            mainPageControllerService.init();
            RetVal retVal = mainPageControllerService.getAppointments();
            appointments = retVal.queryResultsAppointment;

            filteredAppointments = FXCollections.observableArrayList();
            filteredAppointments.addAll(appointments);

            existingAppointmentTable.getItems().clear();
            existingAppointmentTable.setItems(filteredAppointments);
        } else {
            ExistingCustomerService existingCustomerService = new ExistingCustomerService();
            existingCustomerService.init();
            RetVal results = existingCustomerService.getCustomers();
            customers = results.queryResults;

            filteredCustomers = FXCollections.observableArrayList();
            filteredCustomers.addAll(customers);

            existingAppointmentTable.getItems().clear();
            existingAppointmentTable.setItems(filteredCustomers);
        }


    }

    public void addBtnPushed() throws IOException {
        AddAppointment.start(loggedInUser, loggedInUserID);

        refreshMainPageAppointments();
    }

    public void updateBtnPushed() throws IOException {
        Appointment selectedAppointment = (Appointment) existingAppointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Appointment Error");
            alert.setHeaderText("Unselected Appointment");
            alert.setContentText("You have just attempted to update an appointment without selecting an appointment. " +
                    "Please select an appointment to update then click the \'Update\' button again.");
            alert.showAndWait();
            return;
        }

        UpdateAppointment.start(loggedInUser, loggedInUserID, selectedAppointment);

        refreshMainPageAppointments();
    }

    public void deleteBtnPushed() throws IOException {
        if (view == state.APPOINTMENTS) {
            Appointment selectedAppointment = (Appointment) existingAppointmentTable.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Delete Appointment Error");
                alert.setHeaderText("Unselected Appointment");
                alert.setContentText("You have just attempted to delete an appointment without selecting an appointment. " +
                        "Please select an appointment to update then click the \'Update\' button again.");
                alert.showAndWait();
                return;
            }

            DeleteAppointmentService deleteAppointmentService = new DeleteAppointmentService();
            deleteAppointmentService.init();
            deleteAppointmentService.deleteAppointment(selectedAppointment);

            refreshMainPageAppointments();
        } else {
            Customer selectedCustomer = (Customer) existingAppointmentTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Delete Customer Error");
                alert.setHeaderText("Unselected Customer");
                alert.setContentText("You have just attempted to delete a customer without selecting a customer. " +
                        "Please select a customer to update then click the \'Update\' button again.");
                alert.showAndWait();
                return;
            }

            DeleteAppointmentService deleteAppointmentService = new DeleteAppointmentService();
            deleteAppointmentService.init();
            ReturnCodes returnCodes = deleteAppointmentService.deleteCustomer(selectedCustomer);
            if (returnCodes == ReturnCodes.DELETE_CUSTOMER_EXISTING_APPOINTMENT){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Delete Customer Error");
                alert.setHeaderText("Existing Customer Appointment");
                alert.setContentText("You have just attempted to delete a customer that has an appointment in the system. " +
                        "Please ensure any customer you are attempting to delete first has no existing or past appointments.");
                alert.showAndWait();
                return;
            }

            refreshMainPageAppointments();
        }
    }


}
