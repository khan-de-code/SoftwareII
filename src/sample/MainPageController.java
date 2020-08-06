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
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class MainPageController implements Initializable {
    Locale currentLocale;

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
    private RadioButton appointmentRadio;
    @FXML
    private RadioButton customerRadio;
    @FXML
    private Button reports;

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
    @FXML
    private TableColumn<Customer, String> phone;

    private ObservableList<Customer> customers;
    private ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointments;
    private ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentLocale = Locale.getDefault();

        MainPageControllerService mainPageControllerService = new MainPageControllerService();
        mainPageControllerService.init();
        RetVal retVal = mainPageControllerService.getAppointments();

        for (Appointment appointment: retVal.queryResultsAppointment){
            ZoneId currentZone = ZoneId.systemDefault();

            ZonedDateTime current = LocalDateTime.now().atZone(currentZone);

            if (
                    appointment.getStart().getDayOfYear() == current.getDayOfYear()
                    && appointment.getStart().isBefore(current.plusMinutes(15))
                    && appointment.getStart().isAfter(current)
            ){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Upcoming Appointment");
                alert.setHeaderText("Upcoming Appointment Warning");
                alert.setContentText("This message is to alert you to an appointment for " + appointment.getCustomerName() +
                        " that is within 15 minutes from now.");
                alert.showAndWait();
            }
        }

        if (currentLocale.getLanguage() != Locale.GERMAN.getLanguage()) {
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

            customerName.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));
            address.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));
            address2.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));
            city.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));
            zip.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));
            country.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));
            phone.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));

            customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            address.setCellValueFactory(new PropertyValueFactory<>("address"));
            address2.setCellValueFactory(new PropertyValueFactory<>("address2"));
            city.setCellValueFactory(new PropertyValueFactory<>("city"));
            zip.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            country.setCellValueFactory(new PropertyValueFactory<>("country"));
            phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        } else {
            appointments = retVal.queryResultsAppointment;
            title.setCellValueFactory(new PropertyValueFactory<>("titel"));
            description.setCellValueFactory(new PropertyValueFactory<>("eschreibung"));
            location.setCellValueFactory(new PropertyValueFactory<>("ort"));
            contact.setCellValueFactory(new PropertyValueFactory<>("kontakt"));
            type.setCellValueFactory(new PropertyValueFactory<>("art"));
            urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
            start.setCellValueFactory(new PropertyValueFactory<>("anfang"));
            end.setCellValueFactory(new PropertyValueFactory<>("ende"));
            name.setCellValueFactory(new PropertyValueFactory<>("kundenname"));

            customerName.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));
            address.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));
            address2.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));
            city.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));
            zip.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));
            country.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));
            phone.prefWidthProperty().bind(existingAppointmentTable.widthProperty().divide(7));

            customerName.setCellValueFactory(new PropertyValueFactory<>("kundenname"));
            address.setCellValueFactory(new PropertyValueFactory<>("adresse"));
            address2.setCellValueFactory(new PropertyValueFactory<>("adresse2"));
            city.setCellValueFactory(new PropertyValueFactory<>("stadt"));
            zip.setCellValueFactory(new PropertyValueFactory<>("postleitzahl"));
            country.setCellValueFactory(new PropertyValueFactory<>("land"));
            phone.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        }

        if (currentLocale.getLanguage() != Locale.GERMAN.getLanguage()) {
            weekMonthFilter.getItems().add("View All Appointments");
            weekMonthFilter.getItems().add("View Calendar By Month");
            weekMonthFilter.getItems().add("View Calendar By Week");
        } else {
            weekMonthFilter.getItems().add("Alle Termine anzeigen");
            weekMonthFilter.getItems().add("Kalender nach Monat anzeigen");
            weekMonthFilter.getItems().add("Kalender nach Woche anzeigen");
        }

        appointmentRadio.setSelected(true);

        // lambda expression used here to listen for a change in the combobox value and do filter the current view
        // depending on the option chosen
        weekMonthFilter.valueProperty().addListener((options, oldValue, newValue) -> {

            switch (newValue) {
                case "View All Appointments", "Alle Termine anzeigen" -> {
                    filteredAppointments.clear();
                    filteredAppointments.addAll(appointments);
                    existingAppointmentTable.setItems(filteredAppointments);
                }
                case "View Calendar By Month", "Kalender nach Monat anzeigen" -> {
                    filteredAppointments.clear();
                    for (Appointment appointment : appointments) {

                        if (withinAMonth(appointment.getStart())) {
                            filteredAppointments.add(appointment);
                        }
                    }
                    existingAppointmentTable.setItems(filteredAppointments);
                }
                case "View Calendar By Week", "Kalender nach Woche anzeigen" -> {
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

        customerRadio.setSelected(true);
        appointmentRadio.setSelected(false);

        view = state.CUSTOMER_RECORDS;
        weekMonthFilter.setVisible(false);

        if (currentLocale.getLanguage() != Locale.GERMAN.getLanguage()) {
            pageTitle.setText("Customer Management Portal");
        } else {
            pageTitle.setText("Kundenverwaltungsportal");
        }

        existingAppointmentTable.getItems().clear();
        existingAppointmentTable.getColumns().clear();

        ExistingCustomerService existingCustomerService = new ExistingCustomerService();
        existingCustomerService.init();
        RetVal results = existingCustomerService.getCustomers();

        if (results.status == ReturnCodes.CONNECTION_FAILURE) {
            if (currentLocale.getLanguage() != Locale.GERMAN.getLanguage()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Failure");
                alert.setHeaderText("Application Connection Failure");
                alert.setContentText("In the attempt to access the database an error occurred. Please try to close the add" +
                        "appointment screen then reopen it. If the error persists, please contact support.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Verbindungsfehler");
                alert.setHeaderText("Anwendungsverbindungsfehler");
                alert.setContentText("Beim Versuch, auf die Datenbank zuzugreifen, ist ein Fehler aufgetreten. " +
                        "Bitte versuchen Sie, den Bildschirm zum Hinzufügen eines Termins zu schließen und ihn dann " +
                        "erneut zu öffnen. Wenn der Fehler weiterhin besteht, wenden Sie sich an den Support.");
                alert.showAndWait();
            }
        } else {
            customers = results.queryResults;
            filteredCustomers.addAll(customers);
        }

        existingAppointmentTable.getColumns().addAll(customerName, address, address2, city, zip, country, phone);
        existingAppointmentTable.setItems(filteredCustomers);
    }

    public void appointmentsBtnPushed(){
        if (view == state.APPOINTMENTS){
            return;
        }

        appointmentRadio.setSelected(true);
        customerRadio.setSelected(false);

        view = state.APPOINTMENTS;
        weekMonthFilter.setVisible(true);
        if (currentLocale.getLanguage() != Locale.GERMAN.getLanguage()) {
            pageTitle.setText("Appointment Management Portal");

            existingAppointmentTable.getItems().clear();
            existingAppointmentTable.getColumns().clear();

            existingAppointmentTable.getColumns().addAll(name, title, description, location, contact, type, urlCol, start, end);
            refreshFilter("View All Appointments");
            existingAppointmentTable.setItems(filteredAppointments);
        } else {
            pageTitle.setText("Terminverwaltungsportal");

            existingAppointmentTable.getItems().clear();
            existingAppointmentTable.getColumns().clear();

            existingAppointmentTable.getColumns().addAll(name, title, description, location, contact, type, urlCol, start, end);
            refreshFilter("Alle Termine anzeigen");
            existingAppointmentTable.setItems(filteredAppointments);
        }
    }

    public Boolean withinAWeek(ZonedDateTime start){
        LocalDateTime now = LocalDateTime.now();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(now.atZone(ZoneId.of(TimeZone.getDefault().getID())).toInstant()));
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        calendar.setTime(Date.from(start.toInstant()));
        int checkWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        return currentWeek == checkWeek;
    }

    public Boolean withinAMonth(ZonedDateTime start){
        LocalDateTime now = LocalDateTime.now();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(now.atZone(ZoneId.of(TimeZone.getDefault().getID())).toInstant()));
        int currentMonth = calendar.get(Calendar.MONTH);

        calendar.setTime(Date.from(start.toInstant()));
        int checkMonth = calendar.get(Calendar.MONTH);

        return currentMonth == checkMonth;
    }

    public void refreshFilter(String newValue){

        switch (newValue) { // This switch statement implements lambdas to immediately execute code as soon as a case is identified
            case "View All Appointments", "Alle Termine anzeigen" -> {
                filteredAppointments.clear();
                filteredAppointments.addAll(appointments);
                existingAppointmentTable.setItems(filteredAppointments);
            }
            case "View Calendar By Month", "Kalender nach Monat anzeigen" -> {
                filteredAppointments.clear();
                for (Appointment appointment : appointments) {

                    if (withinAMonth(appointment.getStart())) {
                        filteredAppointments.add(appointment);
                    }
                }
                existingAppointmentTable.setItems(filteredAppointments);
            }
            case "View Calendar By Week", "Kalender nach Woche anzeigen" -> {
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
        if (view == state.APPOINTMENTS){
            AddAppointment.start(loggedInUser, loggedInUserID);

            refreshMainPageAppointments();
        } else if (view == state.CUSTOMER_RECORDS){
            MainPageCustomerEdit.start(loggedInUser, loggedInUserID);

            refreshMainPageAppointments();
        }

    }

    public void updateBtnPushed() throws IOException {
        if (view == state.APPOINTMENTS) {
            Appointment selectedAppointment = (Appointment) existingAppointmentTable.getSelectionModel().getSelectedItem();

            if (selectedAppointment == null) {
                if (currentLocale.getLanguage() != Locale.GERMAN.getLanguage()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Update Appointment Error");
                    alert.setHeaderText("Unselected Appointment");
                    alert.setContentText("You have just attempted to update an appointment without selecting an appointment. " +
                            "Please select an appointment to update then click the \'Update\' button again.");
                    alert.showAndWait();
                    return;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Terminfehler aktualisieren");
                    alert.setHeaderText("Nicht ausgewählter Termin");
                    alert.setContentText("Sie haben gerade versucht, einen Termin zu aktualisieren, ohne einen Termin " +
                            "auszuwählen. Bitte wählen Sie einen zu aktualisierenden Termin aus und klicken Sie erneut " +
                            "auf die Schaltfläche Aktualisieren.");
                    alert.showAndWait();
                }
            }

            UpdateAppointment.start(loggedInUser, loggedInUserID, selectedAppointment);

            refreshMainPageAppointments();
        } else {
            Customer selectedCustomer = (Customer) existingAppointmentTable.getSelectionModel().getSelectedItem();

            if (selectedCustomer == null){
                if (currentLocale.getLanguage() != Locale.GERMAN.getLanguage()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Update Customer Error");
                    alert.setHeaderText("Unselected Customer");
                    alert.setContentText("You have just attempted to update a Customer without selecting a Customer. " +
                            "Please select a Customer to update then click the \'Update\' button again.");
                    alert.showAndWait();
                    return;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Kundenfehler aktualisieren");
                    alert.setHeaderText("Nicht ausgewählter Kunde");
                    alert.setContentText("Sie haben gerade versucht, einen Kunden zu aktualisieren, ohne einen Kunden " +
                            "auszuwählen. Bitte wählen Sie einen Kunden zum Aktualisieren aus und klicken Sie erneut " +
                            "auf die Schaltfläche Aktualisieren.");
                    alert.showAndWait();
                }
            }

            MainPageCustomerEdit.start(loggedInUser, loggedInUserID, selectedCustomer);

            refreshMainPageAppointments();
        }
    }

    public void deleteBtnPushed() throws IOException {
        if (view == state.APPOINTMENTS) {
            Appointment selectedAppointment = (Appointment) existingAppointmentTable.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                if (currentLocale.getLanguage() != Locale.GERMAN.getLanguage()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Delete Appointment Error");
                    alert.setHeaderText("Unselected Appointment");
                    alert.setContentText("You have just attempted to delete an appointment without selecting an appointment. " +
                            "Please select an appointment to update then click the \'Update\' button again.");
                    alert.showAndWait();
                    return;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Terminfehler löschen");
                    alert.setHeaderText("Nicht ausgewählter Termin");
                    alert.setContentText("Sie haben gerade versucht, einen Termin zu löschen, ohne einen Termin " +
                                    "auszuwählen. Bitte wählen Sie einen zu aktualisierenden Termin aus und klicken " +
                                    "Sie erneut auf die Schaltfläche Aktualisieren.");
                    alert.showAndWait();
                }
            }

            DeleteAppointmentService deleteAppointmentService = new DeleteAppointmentService();
            deleteAppointmentService.init();
            deleteAppointmentService.deleteAppointment(selectedAppointment);

            refreshMainPageAppointments();
        } else {
            Customer selectedCustomer = (Customer) existingAppointmentTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                if (currentLocale.getLanguage() != Locale.GERMAN.getLanguage()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Delete Customer Error");
                    alert.setHeaderText("Unselected Customer");
                    alert.setContentText("You have just attempted to delete a customer without selecting a customer. " +
                            "Please select a customer to update then click the \'Delete\' button again.");
                    alert.showAndWait();
                    return;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Kundenfehler löschen");
                    alert.setHeaderText("Nicht ausgewählter Kunde");
                    alert.setContentText("Sie haben gerade versucht, einen Kunden zu löschen, ohne einen Kunden " +
                            "auszuwählen. Bitte wählen Sie einen Kunden aus, der aktualisiert werden soll, und klicken " +
                            "Sie erneut auf die Schaltfläche Löschen.");
                    alert.showAndWait();
                }
            }

            DeleteAppointmentService deleteAppointmentService = new DeleteAppointmentService();
            deleteAppointmentService.init();
            ReturnCodes returnCodes = deleteAppointmentService.deleteCustomer(selectedCustomer);
            if (returnCodes == ReturnCodes.DELETE_CUSTOMER_EXISTING_APPOINTMENT){
                if (currentLocale.getLanguage() != Locale.GERMAN.getLanguage()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Delete Customer Error");
                    alert.setHeaderText("Existing Customer Appointment");
                    alert.setContentText("You have just attempted to delete a customer that has an appointment in the system. " +
                            "Please ensure any customer you are attempting to delete first has no existing or past appointments.");
                    alert.showAndWait();
                    return;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Kundenfehler löschen");
                    alert.setHeaderText("Bestehender Kundentermin");
                    alert.setContentText("Sie haben gerade versucht, einen Kunden zu löschen, der einen Termin im System " +
                            "hat. Stellen Sie sicher, dass für jeden Kunden, den Sie zuerst löschen möchten, keine Termine " +
                            "vorhanden oder vergangen sind.");
                    alert.showAndWait();
                    return;
                }
            }

            refreshMainPageAppointments();
        }
    }

    public void reportsBtnPushed() throws IOException {
        new Reports().start(appointments);
    }

}
