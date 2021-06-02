package tmax7.scheduling.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tmax7.scheduling.application.Appointment;
import tmax7.scheduling.application.HelperMethods;
import tmax7.scheduling.application.MainApp;
import tmax7.scheduling.application.controllers.AppointmentsPageController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class EditAppointmentDialogController {
    private MainApp mainApp;
    private Connection databaseConnection;
    private Stage stage;
    private Appointment appointmentToEdit;
    private AppointmentsPageController appointmentsPageController;

    @FXML
    private TextField customerIdTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextArea locationTextArea;
    @FXML
    private TextArea contactTextArea;
    @FXML
    private TextArea typeTextArea;
    @FXML
    private TextField urlTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> startHourComboBox;
    @FXML
    private ComboBox<String> startMinuteComboBox;

    @FXML
    private ComboBox<String> endHourComboBox;
    @FXML
    private ComboBox<String> endMinuteComboBox;

    private final String[] hourStringsArray = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private ObservableList<String> hourStringsList = FXCollections.observableArrayList(hourStringsArray);
    private final String[] minuteStringsArray = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", };
    private ObservableList<String> minuteStringList = FXCollections.observableArrayList(minuteStringsArray);

    @FXML
    private void initialize() {
        startHourComboBox.setItems(hourStringsList);
        startMinuteComboBox.setItems(minuteStringList);

        endHourComboBox.setItems(hourStringsList);
        endMinuteComboBox.setItems(minuteStringList);
    }

    @FXML
    private void onOkClicked() {
        if (isInputValid()) {
            int customerId = Integer.parseInt(customerIdTextField.getText());
            String title = titleTextField.getText();
            String description = descriptionTextArea.getText();
            String location = locationTextArea.getText();
            String contact = contactTextArea.getText();
            String type = typeTextArea.getText();
            String url = urlTextField.getText();


            int year = datePicker.getValue().getYear();
            int month = datePicker.getValue().getMonthValue();
            int day = datePicker.getValue().getDayOfMonth();

            int startHour = Integer.parseInt(startHourComboBox.getSelectionModel().getSelectedItem());
            int startMinute = Integer.parseInt(startMinuteComboBox.getSelectionModel().getSelectedItem());
            int startSecond = 0;
            int startNanoOfSecond = 0;
            //uses the time zone of the user's system
            ZoneId zoneIdOfUser = ZoneId.systemDefault();
            ZonedDateTime startZonedDateTime = ZonedDateTime.of(year, month, day, startHour, startMinute, startSecond, startNanoOfSecond, zoneIdOfUser);

            int endHour = Integer.parseInt(endHourComboBox.getSelectionModel().getSelectedItem());
            int endMinute = Integer.parseInt(endMinuteComboBox.getSelectionModel().getSelectedItem());
            int endSecond = 0;
            int endNanoOfSecond = 0;
            ZonedDateTime endZonedDateTime = ZonedDateTime.of(year, month, day, endHour, endMinute, endSecond, endNanoOfSecond, zoneIdOfUser);

            if (HelperMethods.isValidAppointmentTime(startZonedDateTime, endZonedDateTime, mainApp, appointmentToEdit.getAppointmentId())) {
                appointmentToEdit.setCustomerId(customerId);
                appointmentToEdit.setTitle(title);
                appointmentToEdit.setDescription(description);
                appointmentToEdit.setLocation(location);
                appointmentToEdit.setContact(contact);
                appointmentToEdit.setType(type);
                appointmentToEdit.setUrl(url);
                appointmentToEdit.setStart(startZonedDateTime);
                appointmentToEdit.setEnd(endZonedDateTime);

                updateAppointmentInDatabase(appointmentToEdit);

                stage.close();
            }
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        //checks that field values are not null or empty
        if(customerIdTextField.getText() == null || customerIdTextField.getText().equals("")) {
            errorMessage += "Customer ID field cannot be empty\n";
        }
        if(titleTextField.getText() == null || titleTextField.getText().equals("")) {
            errorMessage += "Title field cannot be empty\n";
        }
        if(descriptionTextArea.getText() == null || descriptionTextArea.getText().equals("")) {
            errorMessage += "Description field cannot be empty\n";
        }
        if(locationTextArea.getText() == null || locationTextArea.getText().equals("")) {
            errorMessage += "Location field cannot be empty\n";
        }
        if(contactTextArea.getText() == null || contactTextArea.getText().equals("")) {
            errorMessage += "Contact field cannot be empty\n";
        }
        if(typeTextArea.getText() == null || typeTextArea.getText().equals("")) {
            errorMessage += "Type field cannot be empty\n";
        }
        if(urlTextField.getText() == null || urlTextField.getText().equals("")) {
            errorMessage += "URL field cannot be empty\n";
        }
        if(datePicker.getValue() == null) {
            errorMessage += "You must choose a date";
        }
        if(startHourComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must choose start hour\n";
        }
        if(startMinuteComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must choose start minute\n";
        }
        if(endHourComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must choose end hour\n";
        }
        if(endMinuteComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must choose end minute\n";
        }

        // checks if field that are supposed to contain integers do indeed contain integers
        if(errorMessage.equals("")) {
            if(!HelperMethods.isStringAnInt(customerIdTextField.getText())) {
                errorMessage += "Customer ID field must contain an integer (e.g. 387987)\n";
            }
        }

        //checks if any customer has the customerId in the customerIdTextField
        if(errorMessage.equals("")) {
            if(!HelperMethods.isCustomerIdValid(customerIdTextField.getText(), mainApp)) {
                errorMessage += "Customer with ID " + customerIdTextField.getText() + " not found\n";
            }
        }

        //If no errors where found then errorMessage will be empty and the method returns true. Else will display error message in an alert and method returns false
        if (errorMessage.equals("")) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;
        }


    }

    private void updateAppointmentInDatabase(Appointment appointmentToEdit) {
        try{

            //gets start and end time and converts it to GMT+0 which is the time zone that DateTimes stored in database should be in
            //then converts them to a LocalDateTime which can then be converted into a Timestamp
            ZonedDateTime startZonedDateTime_InGmtPlus0 = appointmentToEdit.getStart().withZoneSameInstant(ZoneId.of("GMT+0"));
            LocalDateTime startAsLocalDateTime = startZonedDateTime_InGmtPlus0.toLocalDateTime();
            Timestamp startTimestamp = Timestamp.valueOf(startAsLocalDateTime);

            ZonedDateTime endZonedDateTime_InGmtPlus0 = appointmentToEdit.getEnd().withZoneSameInstant(ZoneId.of("GMT+0"));
            LocalDateTime endAsLocalDateTime = endZonedDateTime_InGmtPlus0.toLocalDateTime();
            Timestamp endTimestamp = Timestamp.valueOf(endAsLocalDateTime);

            String lastUpdateBy = mainApp.getUserName();

            //change appointment in appointment table in the database
            String updateAppointmentString = "UPDATE appointment SET " +
                    "customerId = " + appointmentToEdit.getCustomerId() + ", " +
                    "title = \'" + appointmentToEdit.getTitle() + "\', " +
                    "description = \'" + appointmentToEdit.getDescription() + "\', " +
                    "location = \'" + appointmentToEdit.getLocation() + "\', " +
                    "contact =\'" + appointmentToEdit.getContact() + "\', " +
                    "type = \'" + appointmentToEdit.getType() + "\', " +
                    "url = \'" + appointmentToEdit.getUrl() + "\', " +
                    "start = \'" + startTimestamp + "\', " +
                    "end = \'" + endTimestamp + "\', " +
                    "lastUpdate = NOW(), " +
                    "lastUpdateBy = \'" + lastUpdateBy + "\' " +
                    "WHERE appointmentId = " + appointmentToEdit.getAppointmentId();
            PreparedStatement updateAppointment = databaseConnection.prepareStatement(updateAppointmentString);
            updateAppointment.executeUpdate();

            //refresh appointments TableView
            appointmentsPageController.refreshAppointmentsTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancelClicked() {
        stage.close();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.databaseConnection = this.mainApp.getDatabaseConnection();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAppointmentToEdit(Appointment appointmentToEdit) {
        this.appointmentToEdit = appointmentToEdit;

        customerIdTextField.setText(String.valueOf(appointmentToEdit.getCustomerId()));
        titleTextField.setText(appointmentToEdit.getTitle());
        descriptionTextArea.setText(appointmentToEdit.getDescription());
        locationTextArea.setText(appointmentToEdit.getLocation());
        //makes contactTextArea uneditable after initial setting of text
        contactTextArea.setText(appointmentToEdit.getContact());
        contactTextArea.setDisable(true);

        typeTextArea.setText(appointmentToEdit.getType());
        urlTextField.setText(appointmentToEdit.getUrl());

        datePicker.setValue(appointmentToEdit.getStart().toLocalDate());

        startHourComboBox.getSelectionModel().select(HelperMethods.formatHour(appointmentToEdit.getStart().getHour()));
        startMinuteComboBox.getSelectionModel().select(HelperMethods.formatMinute(appointmentToEdit.getStart().getMinute()));

        endHourComboBox.getSelectionModel().select(HelperMethods.formatHour(appointmentToEdit.getEnd().getHour()));
        endMinuteComboBox.getSelectionModel().select(HelperMethods.formatMinute(appointmentToEdit.getEnd().getMinute()));
    }

    public void setAppointmentsPageController(AppointmentsPageController controller) {
        this.appointmentsPageController = controller;
    }

}
