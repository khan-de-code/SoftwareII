package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainPageCustomerEdit implements Initializable {
    Locale currentLocale;

    private String loggedInUser;
    private long loggedInUserID;

    private enum STATE {
        EDIT,
        NEW
    }

    private STATE state;

    @FXML
    public TextField name;
    @FXML
    public TextField address;
    @FXML
    public TextField address2;
    @FXML
    public TextField city;
    @FXML
    public TextField postalCode;
    @FXML
    public TextField country;
    @FXML
    public TextField phone;
    private Customer existingCustomer;

    @FXML
    public Text emptyFields;

    public static void start(String loggedInUser, long loggedInUserID, Customer customer) throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AddAppointment.class.getResource("MainPageCustomerEdit.fxml"));

        Parent productViewParent = loader.load();

        MainPageCustomerEdit mainPageCustomerEdit = loader.getController();
        mainPageCustomerEdit.loggedInUser = loggedInUser;
        mainPageCustomerEdit.loggedInUserID = loggedInUserID;
        mainPageCustomerEdit.name.setText(customer.getCustomerName());
        mainPageCustomerEdit.address.setText(customer.getAddress());
        mainPageCustomerEdit.address2.setText(customer.getAddress2());
        mainPageCustomerEdit.city.setText(customer.getCity());
        mainPageCustomerEdit.postalCode.setText(customer.getPostalCode());
        mainPageCustomerEdit.country.setText(customer.getCountry());
        mainPageCustomerEdit.phone.setText(customer.getPhone());
        mainPageCustomerEdit.existingCustomer = customer;

        mainPageCustomerEdit.state = STATE.EDIT;

        Scene partViewScene = new Scene(productViewParent);

        window.setScene(partViewScene);
        window.showAndWait();
    }

    public static void start(String loggedInUser, long loggedInUserID) throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AddAppointment.class.getResource("MainPageCustomerEdit.fxml"));

        Parent productViewParent = loader.load();

        MainPageCustomerEdit mainPageCustomerEdit = loader.getController();
        mainPageCustomerEdit.loggedInUser = loggedInUser;
        mainPageCustomerEdit.loggedInUserID = loggedInUserID;

        mainPageCustomerEdit.state = STATE.NEW;

        Scene partViewScene = new Scene(productViewParent);

        window.setScene(partViewScene);
        window.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentLocale = Locale.getDefault();

    }

    private Boolean requiredFieldsMissing(TextField... fields){
        boolean retVal = false;
        for(Object field : fields) {
            if (field instanceof javafx.scene.control.TextField){
                if (((javafx.scene.control.TextField) field).getText().equals("")){
                    retVal = true;
                    break;
                }
            }
        }

        return retVal;
    }

    public void saveButtonPushed(){
        TextField[] checkFields = {
                name,
                address,
                city,
                postalCode,
                country,
                phone
        };

        if (requiredFieldsMissing(checkFields)){
            emptyFields.setVisible(true);
            for (TextField textField: checkFields) {
                if (requiredFieldsMissing(textField)){
                    textField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
                } else {
                    textField.setStyle(null);
                }
            }
            return;
        } else {
            emptyFields.setVisible(false);

            for (TextField textField: checkFields) {
                textField.setStyle(null);
            }
        }

        if (state == STATE.NEW){
            saveNewCustomer();
        } else if (state == STATE.EDIT){
            updateCustomer();
        }
    }

    private void saveNewCustomer(){
        Customer customer = new Customer(
                name.getText(),
                address.getText(),
                address2.getText(),
                postalCode.getText(),
                phone.getText(),
                city.getText(),
                country.getText(),
                null
        );

        MainPageCustomerEditService mainPageCustomerEditService = new MainPageCustomerEditService();
        mainPageCustomerEditService.init(loggedInUser, loggedInUserID, customer, null);

        mainPageCustomerEditService.createCustomer();

        Stage window = (Stage) name.getScene().getWindow();
        window.close();
    }

    private void updateCustomer(){
        if (
                existingCustomer.getCustomerName().equals(name.getText())
                && existingCustomer.getAddress().equals(address.getText())
                && existingCustomer.getAddress2().equals(address.getText())
                && existingCustomer.getPostalCode().equals(postalCode.getText())
                && existingCustomer.getPhone().equals(phone.getText())
                && existingCustomer.getCity().equals(city.getText())
                && existingCustomer.getCountry().equals(country.getText())
        ){
            if (currentLocale.getLanguage() != Locale.GERMAN.getLanguage()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Update Customer Error");
                alert.setHeaderText("Update Customer Error");
                alert.setContentText("You have just attempted to update a customer without making any changes. " +
                        "Please ensure you have made some changes to the customer and attempt to save your changes again.");
                alert.showAndWait();
                return;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Kundenfehler aktualisieren");
                alert.setHeaderText("Kundenfehler aktualisieren");
                alert.setContentText("Sie haben gerade versucht, einen Kunden zu aktualisieren, ohne Änderungen " +
                        "vorzunehmen. Bitte stellen Sie sicher, dass Sie einige Änderungen am Kunden vorgenommen haben, " +
                        "und versuchen Sie erneut, Ihre Änderungen zu speichern.");
                alert.showAndWait();
            }
        }

        Customer customer = new Customer(
                name.getText(),
                address.getText(),
                address2.getText(),
                postalCode.getText(),
                phone.getText(),
                city.getText(),
                country.getText(),
                existingCustomer.getCustomerId()
        );

        MainPageCustomerEditService mainPageCustomerEditService = new MainPageCustomerEditService();
        mainPageCustomerEditService.init(loggedInUser, loggedInUserID, customer, existingCustomer);

        mainPageCustomerEditService.updateCustomer();

        Stage window = (Stage) name.getScene().getWindow();
        window.close();
    }
}
