package my.scheduling.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.time.*;

public class BusinessHoursPageController {
    MainApp mainApp;

    @FXML
    private ComboBox<String> mondayStartComboBox;
    @FXML
    private ComboBox<String> mondayEndComboBox;
    @FXML
    private ComboBox<String> tuesdayStartComboBox;
    @FXML
    private ComboBox<String> tuesdayEndComboBox;
    @FXML
    private ComboBox<String> wednesdayStartComboBox;
    @FXML
    private ComboBox<String> wednesdayEndComboBox;
    @FXML
    private ComboBox<String> thursdayStartComboBox;
    @FXML
    private ComboBox<String> thursdayEndComboBox;
    @FXML
    private ComboBox<String> fridayStartComboBox;
    @FXML
    private ComboBox<String> fridayEndComboBox;

    private String[] arrayOfTimeStrings = {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    private ObservableList<String> listOfTimeStrings = FXCollections.observableArrayList(arrayOfTimeStrings);

    @FXML
    private void initialize() {
        mondayStartComboBox.setItems(listOfTimeStrings);
        mondayEndComboBox.setItems(listOfTimeStrings);
        tuesdayStartComboBox.setItems(listOfTimeStrings);
        tuesdayEndComboBox.setItems(listOfTimeStrings);
        wednesdayStartComboBox.setItems(listOfTimeStrings);
        wednesdayEndComboBox.setItems(listOfTimeStrings);
        thursdayStartComboBox.setItems(listOfTimeStrings);
        thursdayEndComboBox.setItems(listOfTimeStrings);
        fridayStartComboBox.setItems(listOfTimeStrings);
        fridayEndComboBox.setItems(listOfTimeStrings);

        //default selection
        mondayStartComboBox.getSelectionModel().select("09:00");
        mondayEndComboBox.getSelectionModel().select("17:00");
        tuesdayStartComboBox.getSelectionModel().select("09:00");
        tuesdayEndComboBox.getSelectionModel().select("17:00");
        wednesdayStartComboBox.getSelectionModel().select("09:00");
        wednesdayEndComboBox.getSelectionModel().select("17:00");
        thursdayStartComboBox.getSelectionModel().select("09:00");
        thursdayEndComboBox.getSelectionModel().select("17:00");
        fridayStartComboBox.getSelectionModel().select("09:00");
        fridayEndComboBox.getSelectionModel().select("17:00");

    }

    @FXML
    private void onOkClicked() {
        if(isInputValid()) {
            ZoneOffset userZoneOffset = ZonedDateTime.now().getOffset();

            LocalTime mondayStartLocalTime = LocalTime.of(HelperMethods.toHourInt(mondayStartComboBox.getSelectionModel().getSelectedItem()), 0);
            OffsetTime mondayStartOffsetTime = OffsetTime.of(mondayStartLocalTime, userZoneOffset);
            LocalTime mondayEndLocalTime = LocalTime.of(HelperMethods.toHourInt(mondayEndComboBox.getSelectionModel().getSelectedItem()), 0);
            OffsetTime mondayEndOffsetTime = OffsetTime.of(mondayEndLocalTime, userZoneOffset);

            LocalTime tuesdayStartLocalTime = LocalTime.of(HelperMethods.toHourInt(tuesdayStartComboBox.getSelectionModel().getSelectedItem()), 0);
            OffsetTime tuesdayStartOffsetTime = OffsetTime.of(tuesdayStartLocalTime, userZoneOffset);
            LocalTime tuesdayEndLocalTime = LocalTime.of(HelperMethods.toHourInt(tuesdayEndComboBox.getSelectionModel().getSelectedItem()), 0);
            OffsetTime tuesdayEndOffsetTime = OffsetTime.of(tuesdayEndLocalTime, userZoneOffset);

            LocalTime wednesdayStartLocalTime = LocalTime.of(HelperMethods.toHourInt(wednesdayStartComboBox.getSelectionModel().getSelectedItem()), 0);
            OffsetTime wednesdayStartOffsetTime = OffsetTime.of(wednesdayStartLocalTime, userZoneOffset);
            LocalTime wednesdayEndLocalTime = LocalTime.of(HelperMethods.toHourInt(wednesdayEndComboBox.getSelectionModel().getSelectedItem()), 0);
            OffsetTime wednesdayEndOffsetTime = OffsetTime.of(wednesdayEndLocalTime, userZoneOffset);

            LocalTime thursdayStartLocalTime = LocalTime.of(HelperMethods.toHourInt(thursdayStartComboBox.getSelectionModel().getSelectedItem()), 0);
            OffsetTime thursdayStartOffsetTime = OffsetTime.of(thursdayStartLocalTime, userZoneOffset);
            LocalTime thursdayEndLocalTime = LocalTime.of(HelperMethods.toHourInt(thursdayEndComboBox.getSelectionModel().getSelectedItem()), 0);
            OffsetTime thursdayEndOffsetTime = OffsetTime.of(thursdayEndLocalTime, userZoneOffset);

            LocalTime fridayStartLocalTime = LocalTime.of(HelperMethods.toHourInt(fridayStartComboBox.getSelectionModel().getSelectedItem()), 0);
            OffsetTime fridayStartOffsetTime = OffsetTime.of(fridayStartLocalTime, userZoneOffset);
            LocalTime fridayEndLocalTime = LocalTime.of(HelperMethods.toHourInt(fridayEndComboBox.getSelectionModel().getSelectedItem()), 0);
            OffsetTime fridayEndOffsetTime = OffsetTime.of(fridayEndLocalTime, userZoneOffset);

            BusinessHours businessHours = new BusinessHours(mondayStartOffsetTime, mondayEndOffsetTime, tuesdayStartOffsetTime, tuesdayEndOffsetTime, wednesdayStartOffsetTime, wednesdayEndOffsetTime, thursdayStartOffsetTime, thursdayEndOffsetTime, fridayStartOffsetTime, fridayEndOffsetTime);

            mainApp.setBusinessHours(businessHours);
            mainApp.showUserPage();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if(mondayStartComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must select a start time for Monday\n";
        }
        if(mondayEndComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must select an end time for Monday\n";
        }
        if(tuesdayStartComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must select a start time for Tuesday\n";
        }
        if(tuesdayEndComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must select an end time for Tuesday\n";
        }
        if(wednesdayStartComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must select a start time for Wednesday\n";
        }
        if(wednesdayEndComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must select an end time for Wednesday\n";
        }
        if(thursdayStartComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must select a start time for Thursday\n";
        }
        if(thursdayEndComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must select an end time for Thursday\n";
        }
        if(fridayStartComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must select a start time for Friday\n";
        }
        if(fridayEndComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You must select an end time for Friday\n";
        }

        if(errorMessage.equals("")) {
            if (HelperMethods.toHourInt(mondayStartComboBox.getSelectionModel().getSelectedItem()) >= HelperMethods.toHourInt(mondayEndComboBox.getSelectionModel().getSelectedItem())) {
                errorMessage += "Monday start time must be before Monday end time\n";
            }
            if (HelperMethods.toHourInt(tuesdayStartComboBox.getSelectionModel().getSelectedItem()) >= HelperMethods.toHourInt(tuesdayEndComboBox.getSelectionModel().getSelectedItem())) {
                errorMessage += "Tuesday start time must be before Tuesday end time\n";
            }
            if (HelperMethods.toHourInt(wednesdayStartComboBox.getSelectionModel().getSelectedItem()) >= HelperMethods.toHourInt(wednesdayEndComboBox.getSelectionModel().getSelectedItem())) {
                errorMessage += "Wednesday start time must be before Wednesday end time\n";
            }
            if (HelperMethods.toHourInt(thursdayStartComboBox.getSelectionModel().getSelectedItem()) >= HelperMethods.toHourInt(thursdayEndComboBox.getSelectionModel().getSelectedItem())) {
                errorMessage += "Thursday start time must be before Thursday end time\n";
            }
            if (HelperMethods.toHourInt(fridayStartComboBox.getSelectionModel().getSelectedItem()) >= HelperMethods.toHourInt(fridayEndComboBox.getSelectionModel().getSelectedItem())) {
                errorMessage += "Friday start time must be before Friday end time\n";
            }
        }

        if(errorMessage.equals("")) {
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

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
