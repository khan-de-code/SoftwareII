package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RetVal {
    public ReturnCodes status;
    public ObservableList<Customer> queryResults = FXCollections.observableArrayList();
    public ObservableList<Appointment> queryResultsAppointment = FXCollections.observableArrayList();

}
