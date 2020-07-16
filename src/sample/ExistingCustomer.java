package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ExistingCustomer implements Initializable {
    @FXML
    public TableView<Customer> existingCustomerTable;
    @FXML
    private TableColumn<Customer, String> name;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ExistingCustomerService existingCustomerService = new ExistingCustomerService();
        if (existingCustomerService.init() == ReturnCodes.CONNECTION_FAILURE){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Failure");
            alert.setHeaderText("Application Connection Failure");
            alert.setContentText("In the attempt to access the database an error occurred. Please try to close the add" +
                    "appointment screen then reopen it. If the error persists, please contact support.");
            alert.showAndWait();
        }
        RetVal results = existingCustomerService.getCustomers();
        if (results.status == ReturnCodes.CONNECTION_FAILURE){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Failure");
            alert.setHeaderText("Application Connection Failure");
            alert.setContentText("In the attempt to access the database an error occurred. Please try to close the add" +
                    "appointment screen then reopen it. If the error persists, please contact support.");
            alert.showAndWait();
        } else {
            name.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
            address.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
            address2.setCellValueFactory(new PropertyValueFactory<Customer, String>("address2"));
            city.setCellValueFactory(new PropertyValueFactory<Customer, String>("city"));
            zip.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
            country.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));

            existingCustomerTable.setItems(results.queryResults);
        }
    }
}
