package tmax7.scheduling.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tmax7.scheduling.application.Appointment;
import tmax7.scheduling.application.HelperMethods;
import tmax7.scheduling.application.MainApp;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CalendarMonthPageController {
    private MainApp mainApp;
    private List<Appointment> appointments = new ArrayList<>();
    private ObservableList<Month> months = FXCollections.observableArrayList(Month.values());
    private ObservableList<Integer> years =FXCollections.observableArrayList();
    private int yearSelected;
    private Month monthSelected;
    private YearMonth yearMonthSelected;

    private UserPageController userPageController;

    @FXML
    private ComboBox<Integer> yearsComboBox;
    @FXML
    private ComboBox<Month> monthsComboBox;

    @FXML
    private  TitledPane row0_Sunday;
    @FXML
    private  TitledPane row0_Monday;
    @FXML
    private  TitledPane row0_Tuesday;
    @FXML
    private  TitledPane row0_Wednesday;
    @FXML
    private  TitledPane row0_Thursday;
    @FXML
    private  TitledPane row0_Friday;
    @FXML
    private  TitledPane row0_Saturday;

    @FXML
    private TitledPane row1_Sunday;
    @FXML
    private TitledPane row1_Monday;
    @FXML
    private TitledPane row1_Tuesday;
    @FXML
    private TitledPane row1_Wednesday;
    @FXML
    private TitledPane row1_Thursday;
    @FXML
    private TitledPane row1_Friday;
    @FXML
    private TitledPane row1_Saturday;

    @FXML
    private TitledPane row2_Sunday;
    @FXML
    private TitledPane row2_Monday;
    @FXML
    private TitledPane row2_Tuesday;
    @FXML
    private TitledPane row2_Wednesday;
    @FXML
    private TitledPane row2_Thursday;
    @FXML
    private TitledPane row2_Friday;
    @FXML
    private TitledPane row2_Saturday;

    @FXML
    private TitledPane row3_Sunday;
    @FXML
    private TitledPane row3_Monday;
    @FXML
    private TitledPane row3_Tuesday;
    @FXML
    private TitledPane row3_Wednesday;
    @FXML
    private TitledPane row3_Thursday;
    @FXML
    private TitledPane row3_Friday;
    @FXML
    private TitledPane row3_Saturday;

    @FXML
    private TitledPane row4_Sunday;
    @FXML
    private TitledPane row4_Monday;
    @FXML
    private TitledPane row4_Tuesday;
    @FXML
    private TitledPane row4_Wednesday;
    @FXML
    private TitledPane row4_Thursday;
    @FXML
    private TitledPane row4_Friday;
    @FXML
    private TitledPane row4_Saturday;

    @FXML
    private TitledPane row5_Sunday;
    @FXML
    private TitledPane row5_Monday;

    private TitledPane[] arrayOfTitledPanes;

    @FXML
    private void initialize() {

        arrayOfTitledPanes = new TitledPane[] { row0_Sunday, row0_Monday, row0_Tuesday, row0_Wednesday, row0_Thursday, row0_Friday, row0_Saturday,
                                                row1_Sunday, row1_Monday, row1_Tuesday, row1_Wednesday, row1_Thursday, row1_Friday, row1_Saturday,
                                                row2_Sunday, row2_Monday, row2_Tuesday, row2_Wednesday, row2_Thursday, row2_Friday, row2_Saturday,
                                                row3_Sunday, row3_Monday, row3_Tuesday, row3_Wednesday, row3_Thursday, row3_Friday, row3_Saturday,
                                                row4_Sunday, row4_Monday, row4_Tuesday, row4_Wednesday, row4_Thursday, row4_Friday, row4_Saturday,
                                                row5_Sunday, row5_Monday};

        //disable monthsComboBox so month won't be selected before year
        monthsComboBox.setDisable(true);

        resetTitledPanes();
    }

    @FXML
    private void onYearSelected() {
        if(yearsComboBox.getSelectionModel().getSelectedItem() != null) {
            yearSelected = yearsComboBox.getSelectionModel().getSelectedItem();
            //enable user to select month
            monthsComboBox.setDisable(false);
        }
         // calls onMonthSelected() when user changes the year after having previously changed the month
        if(monthSelected != null) {
            onMonthSelected();
        }
    }

    @FXML
    private void onMonthSelected() {
        if(monthsComboBox.getSelectionModel().getSelectedItem() != null && yearSelected != 0) {
            monthSelected = monthsComboBox.getSelectionModel().getSelectedItem();
            yearMonthSelected = YearMonth.of(yearSelected, monthSelected);

            DayOfWeek dayOfWeek_OfFirstDayOfMonth = yearMonthSelected.atDay(1).getDayOfWeek();
            int valueOfFirstDayOfMonth = -1;
            switch(dayOfWeek_OfFirstDayOfMonth) {
                case SUNDAY:
                    valueOfFirstDayOfMonth = 0;
                    break;
                case MONDAY:
                    valueOfFirstDayOfMonth = 1;
                    break;
                case TUESDAY:
                    valueOfFirstDayOfMonth = 2;
                    break;
                case WEDNESDAY:
                    valueOfFirstDayOfMonth = 3;
                    break;
                case THURSDAY:
                    valueOfFirstDayOfMonth = 4;
                    break;
                case FRIDAY:
                    valueOfFirstDayOfMonth = 5;
                    break;
                case SATURDAY:
                    valueOfFirstDayOfMonth = 6;
                    break;
            }

            // fills calendar with correct dates and appointments.
            //valueOfFirstDayOfMonth represents which weekday the month starts on while j represents the day of month e.g. 1, 2, 3
            if(valueOfFirstDayOfMonth > -1) {
                resetTitledPanes();
                int numberOfDaysInMonth = yearMonthSelected.lengthOfMonth();
                for (int i = valueOfFirstDayOfMonth, j = 1; i < valueOfFirstDayOfMonth + numberOfDaysInMonth; i++, j++) {
                    arrayOfTitledPanes[i].setText(String.valueOf(j));
                    arrayOfTitledPanes[i].getContent().setStyle("-fx-background-color: WHITE");

                    //adds all User's appointments on the same day to the listOfAppointments_ToAddToDay ArrayList
                    List<Appointment> listOfAppointments_ToAddToDay = new ArrayList<>();
                    for(Appointment appointment : appointments) {
                        if(appointment.getUserId() == mainApp.getUserId()){
                            if(appointment.getStart().getYear() == yearSelected) {
                                if(appointment.getStart().getMonth().equals(monthSelected)) {
                                    if(appointment.getStart().getDayOfMonth() == j) {
                                        listOfAppointments_ToAddToDay.add(appointment);
                                    }
                                }
                            }
                        }
                    }
                    //sorts list so appointments will be listed earliest to latest start time
                    Collections.sort(listOfAppointments_ToAddToDay, (Appointment a, Appointment b) -> { return a.getStart().compareTo(b.getStart()); });

                    //add appointments to vBox
                    VBox vBox_InTitledPanesAnchorPane = (VBox) ((AnchorPane)arrayOfTitledPanes[i].getContent()).getChildren().get(0);
                    for(Appointment sortedAppointment : listOfAppointments_ToAddToDay) {
                        vBox_InTitledPanesAnchorPane.getChildren().add(createAppointmentGridPane(sortedAppointment));
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Something went wrong when month was selected");

                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onPreviousClicked() {
        Month previousMonth = monthSelected.minus(1);
        monthsComboBox.getSelectionModel().select(previousMonth);
    }

    @FXML
    private void onNextClicked() {
        Month nextMonth = monthSelected.plus(1);
        monthsComboBox.getSelectionModel().select(nextMonth);
    }

    @FXML
    private void onWeeklyViewClicked() {
        userPageController.showCalendarWeeklyViewPage();
    }

    // empties TitlePanes' text and sets background color to GAINSBORO which is tha background color of page in app
    private void resetTitledPanes() {
        for(TitledPane titledPane : arrayOfTitledPanes) {
            titledPane.setText("");
            titledPane.getContent().setStyle("-fx-background-color: GAINSBORO");

            VBox vBox_InTitledPanesAnchorPane = (VBox) ((AnchorPane)titledPane.getContent()).getChildren().get(0);
            vBox_InTitledPanesAnchorPane.getChildren().clear();
        }
    }

    private Node createAppointmentGridPane(Appointment appointment) {
        GridPane gridPane = new GridPane();
        Label appointmentIdLabel = new Label("Appointment ID: " + appointment.getAppointmentId() + "  ");
        Label startTimeLabel = new Label("Time: " + appointment.getStart().getHour() + ":" + HelperMethods.formatMinute(appointment.getStart().getMinute()) + " - " + appointment.getEnd().getHour() + ":" + HelperMethods.formatMinute(appointment.getEnd().getMinute()));

        gridPane.add(appointmentIdLabel, 0, 0);
        gridPane.add(startTimeLabel, 1, 0);

        return gridPane;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        for(Object object : mainApp.getAppointments()) {
            appointments.add((Appointment) object);
        }

        //set comboBoxes Items
        monthsComboBox.setItems(months);

        int currentYear = LocalDateTime.now().getYear();
        int yearsLimit = mainApp.getYearsLimit();
        Stream<Integer> streamOfIntegers = Stream.iterate(currentYear, y -> y + 1);
        years.addAll(streamOfIntegers.limit(yearsLimit).collect(Collectors.toList()));
        yearsComboBox.setItems(years);
    }

    public void setUserPageController(UserPageController controller) {
        this.userPageController = controller;
    }
}
