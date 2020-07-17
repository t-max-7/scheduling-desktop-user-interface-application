package my.scheduling.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.*;

public class AddAppointmentDialogController {
    private MainApp mainApp;
    private Connection databaseConnection;
    private Stage stage;

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

    private final String[] hourStringsArray = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };
    private ObservableList<String> hourStringsList = FXCollections.observableArrayList(hourStringsArray);
    private final String[] minuteStringsArray = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                                            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" };
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
        if(isInputValid()) {
            int customerId = Integer.parseInt(customerIdTextField.getText());
            int userId = mainApp.getUserId();
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

            //creates appointment object, adds it into database, and sets it with database appointmentId
            if(HelperMethods.isValidAppointmentTime(startZonedDateTime, endZonedDateTime, mainApp)) {
                Appointment appointment = new Appointment(customerId, userId, title, description, location, contact, type, url, startZonedDateTime, endZonedDateTime);

                int appointmentId = addAppointmentIntoDatabase(appointment);
                appointment.setAppointmentId(appointmentId);

                mainApp.getAppointments().add(appointment);

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

    private int addAppointmentIntoDatabase(Appointment appointment) {
        int customerId = appointment.getCustomerId();
        int userId = appointment.getUserId();
        String title = appointment.getTitle();
        String description = appointment.getDescription();
        String location = appointment.getLocation();
        String contact = appointment.getContact();
        String type = appointment.getType();
        String url = appointment.getUrl();

        //gets start and end time and converts it to GMT+0 which is the time zone that DateTimes stored in database should be in
        //then converts them to a LocalDateTime which can then be converted into a Timestamp
        ZonedDateTime startZonedDateTime_InGmtPlus0 = appointment.getStart().withZoneSameInstant(ZoneId.of("GMT+0"));
        LocalDateTime startAsLocalDateTime = startZonedDateTime_InGmtPlus0.toLocalDateTime();
        Timestamp startTimestamp = Timestamp.valueOf(startAsLocalDateTime);

        ZonedDateTime endZonedDateTime_InGmtPlus0 = appointment.getEnd().withZoneSameInstant(ZoneId.of("GMT+0"));
        LocalDateTime endAsLocalDateTime = endZonedDateTime_InGmtPlus0.toLocalDateTime();
        Timestamp endTimestamp = Timestamp.valueOf(endAsLocalDateTime);

        String createdBy = mainApp.getUserName();
        String lastUpdateBy = createdBy;

        try {
            //statement to get id of last record inserted
            String selectLastInsertIdString = "SELECT LAST_INSERT_ID()";
            PreparedStatement selectLastInsertId = databaseConnection.prepareStatement(selectLastInsertIdString);
            //statement to insert appointment into appointment table
            String insertAppointmentString = "INSERT INTO appointment(customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy) VALUES(" +
                                                customerId + ", " + userId + ", \'" + title + "\', \'" + description + "\', \'" + location +"\', \'" + contact + "\', \'" + type + "\', \'" + url + "\', \'" + startTimestamp + "\', " +
                                                "\'" + endTimestamp + "\', " + "NOW(), \'" +createdBy + "\', \'" + lastUpdateBy + "\'" + ")";
            PreparedStatement insertAppointment = databaseConnection.prepareStatement(insertAppointmentString);
            insertAppointment.executeUpdate();

            //get id of inserted appointment and return if
            ResultSet lastInsertResultSet = selectLastInsertId.executeQuery();
            lastInsertResultSet.next();
            int firstColumn = 1;
            int appointmentId_OfInsertedAppointment = lastInsertResultSet.getInt(firstColumn);

            return appointmentId_OfInsertedAppointment;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @FXML
    private void onCancelClicked() {
        stage.close();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.databaseConnection = this.mainApp.getDatabaseConnection();
        //TODO see if the contact should be user and change from textArea to textbox
        //make the contact equal to the user and the text area uneditable
        this.contactTextArea.setText(mainApp.getUserName());
        this.contactTextArea.setDisable(true);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
