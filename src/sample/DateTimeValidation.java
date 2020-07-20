package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateTimeValidation {
    public Boolean compare(DatePicker startDate, TextField startHour, TextField startMin, ComboBox startAMPM,
                           DatePicker endDate, TextField endHour, TextField endMin, ComboBox endAMPM) throws ParseException {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String startDateTime = generateDateTimeString(startDate, startHour, startMin, startAMPM);
        Date startDateTimeVal = dateTimeFormat.parse(startDateTime);

        String endDateTime = generateDateTimeString(endDate, endHour, endMin, endAMPM);
        Date endDateTimeVal = dateTimeFormat.parse(endDateTime);

        if (startDateTimeVal.compareTo(endDateTimeVal) <= 0){
            startDate.setStyle(null);
            startHour.setStyle(null);
            startMin.setStyle(null);
            endDate.setStyle(null);
            endHour.setStyle(null);
            endMin.setStyle(null);

            return true;
        } else if (startDateTimeVal.compareTo(endDateTimeVal) > 0){
            startDate.setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");
            startHour.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            startMin.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            startAMPM.setStyle("-fx-border-color: #B22222; -fx-focus-color: #B22222;");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Entry");
            alert.setHeaderText("End Date/ Time Error");
            alert.setContentText("The submitted start date occurs after the submitted end date. Please ensure " +
                    "you have entered the correct dates and click \"Create Appointment\" again.");
            alert.showAndWait();

            return false;
        }

        return false;
    }

    public String generateDateTimeString(DatePicker date, TextField hour, TextField min, ComboBox am_pm){
        String returnDateTime = date.getValue().toString();
        String hourStr = (am_pm.getValue() == "AM") ? hour.getText() : (Integer.parseInt(hour.getText()) + 12 + "");
        hourStr = Integer.parseInt(hourStr) > 9 ? hourStr : "0" + hourStr;
        String minStr = Integer.parseInt(min.getText()) > 9 ? min.getText() : "0" + min.getText();

        returnDateTime += " " + hourStr + ":" + minStr + ":00";

        return returnDateTime;
    }

    public Boolean checkHours(TextField hourField){
        return Integer.parseInt(hourField.getText()) <= 12;
    }

    public Boolean checkMins(TextField minField){
        return Integer.parseInt(minField.getText()) <= 59;
    }
};
