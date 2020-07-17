package my.scheduling.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CalendarWeeklyViewPageController {
    private MainApp mainApp;
    private List<Appointment> appointments = new ArrayList<>();
    private ObservableList<Month> months = FXCollections.observableArrayList(Month.values());
    private ObservableList<Integer> years = FXCollections.observableArrayList();
    private int yearSelected;
    private Month monthSelected;
    private YearMonth yearMonthSelected;

    private UserPageController userPageController;

    private int numberOfDaysInMonth;
    private int valueOfDayOfWeek;

    private boolean atBeginningOfMonth =false;
    private boolean atEndOfMonth = false;

    @FXML
    private ComboBox<Integer> yearsComboBox;
    @FXML
    private ComboBox<Month> monthsComboBox;

    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;

    @FXML
    private Label dayLabel0;
    @FXML
    private Label dayLabel1;
    @FXML
    private Label dayLabel2;
    @FXML
    private Label dayLabel3;
    @FXML
    private Label dayLabel4;
    @FXML
    private Label dayLabel5;
    @FXML
    private Label dayLabel6;

    private Label[] arrayOfLabels;

    @FXML
    private ListView<String> listView0;
    @FXML
    private ListView<String> listView1;
    @FXML
    private ListView<String> listView2;
    @FXML
    private ListView<String> listView3;
    @FXML
    private ListView<String> listView4;
    @FXML
    private ListView<String> listView5;
    @FXML
    private ListView<String> listView6;

    private ListView[] arrayOfListViews;

    @FXML
    private void initialize() {
        arrayOfLabels = new Label[]{dayLabel0, dayLabel1, dayLabel2, dayLabel3, dayLabel4, dayLabel5, dayLabel6};
        arrayOfListViews = new ListView[]{listView0, listView1, listView2, listView3, listView4, listView5, listView6};

        //if no month is selected, disables the monthsComboBox so month won't be selected before year
        if (monthsComboBox.getSelectionModel().getSelectedItem() == null) {
            monthsComboBox.setDisable(true);
        }

        //disables buttons which will then be enabled when year and month selected
        previousButton.setDisable(true);
        nextButton.setDisable(true);

        resetLabelsAndListViews();
    }

    @FXML
    private void onYearSelected() {
        if (yearsComboBox.getSelectionModel().getSelectedItem() != null) {
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
        if (monthsComboBox.getSelectionModel().getSelectedItem() != null && yearSelected != 0) {
            //enables previous and next buttons
            previousButton.setDisable(false);
            nextButton.setDisable(false);

            //enables clicking next if reaching end of previous month disabled it
            atBeginningOfMonth = true;
            atEndOfMonth = false;

            monthSelected = monthsComboBox.getSelectionModel().getSelectedItem();

            for (ListView<Appointment> listView : arrayOfListViews) {
                listView.getItems().clear();
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
                //fills first week of month with correct dates and appointments.
                //valueOfFirstDayOfMonth represents which weekday the month starts on while j represents the day of month e.g. 1, 2, 3
                if(valueOfFirstDayOfMonth > -1) {
                    resetLabelsAndListViews();
                    numberOfDaysInMonth = yearMonthSelected.lengthOfMonth();
                    for(int i = valueOfFirstDayOfMonth, j = 1; i < arrayOfLabels.length; i++, j++) {
                        arrayOfLabels[i].setText(String.valueOf(j));
                        valueOfDayOfWeek = j;
                        //adds appointments to ListView at index i that are on the same day as the one represented by j
                        addAppointmentsToListView(i, j);
                    }
                    //increment it so onNextClicked() puts in the right dates;
                    valueOfDayOfWeek++;
                    //TODO see if should do  different error handling here
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Something went wrong when month was selected");

                    alert.showAndWait();
                }
            }
        }
    }

    private void addAppointmentsToListView(int indexOfDayOfWeek, int dayOfMonth) {
        //adds all User's appointments on the same day to the listOfAppointments_ToAddToDay ObservableArrayList
        ObservableList<Appointment> listOfAppointments_ToAddToDay = FXCollections.observableArrayList();
        for(Appointment appointment : appointments) {
            if(appointment.getUserId() == mainApp.getUserId()) {
                if(appointment.getStart().getYear() == yearSelected) {
                    if(appointment.getStart().getMonth().equals(monthSelected)) {
                        if(appointment.getStart().getDayOfMonth() == dayOfMonth) {
                            listOfAppointments_ToAddToDay.add(appointment);
                        }
                    }
                }
            }

        }
        //sorts list so appointments will be listed from earliest to latest start time
        Collections.sort(listOfAppointments_ToAddToDay, (Appointment a, Appointment b) -> { return a.getStart().compareTo(b.getStart()); });

        //add appointments to listView of day
        for(Appointment sortedAppointment : listOfAppointments_ToAddToDay) {
            arrayOfListViews[indexOfDayOfWeek].getItems().add(formatAppointment(sortedAppointment));
        }
    }

    @FXML
    private void onMonthViewClicked() {
        userPageController.showCalendarMonthPage();
    }

    private String formatAppointment(Appointment appointment) {
        String appointmentString =  "Appointment ID: " + appointment.getAppointmentId() +
                                    "  Time:" + appointment.getStart().getHour() + ":" + HelperMethods.formatMinute(appointment.getStart().getMinute()) + " - " + appointment.getEnd().getHour() + ":" + HelperMethods.formatMinute(appointment.getEnd().getMinute());
        return  appointmentString;

    }

    @FXML
    private void onPreviousClicked() {
        //causes alert if at beginning of year
        if(monthSelected.equals(Month.JANUARY) && atBeginningOfMonth) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Have reached beginning of current year. Please select the previous year to find week before this one");
            alert.showAndWait();
            return;
        }

        boolean inMiddleOfMonth = !atBeginningOfMonth && !atEndOfMonth;
        //clears listViews of appointments
        resetListViews();
        // if beginning of month has not been reached then sets labels to the correct valueOfDay
        if(inMiddleOfMonth) {
            for(int i = 0; i < arrayOfLabels.length; i++) {
                int valueOfDayOfWeek = Integer.parseInt(arrayOfLabels[i].getText());
                int valueOfDayOfPreviousWeek = valueOfDayOfWeek - 7;
                if (valueOfDayOfPreviousWeek > 0) {
                    arrayOfLabels[i].setText(String.valueOf(valueOfDayOfPreviousWeek));
                    //adds appointments to ListView located at index i that are on the same day as the one represented by valueOfDayOfPreviousWeek
                    addAppointmentsToListView(i, valueOfDayOfPreviousWeek);
                    // if any of the values of the days of the previous week are 1 then sets atBeginningOfMonth to true
                    if (valueOfDayOfPreviousWeek == 1) {
                        atBeginningOfMonth = true;
                    }
                } else {
                    arrayOfLabels[i].setText("");
                }
            }
            //sets this.valueOfDayOfWeek back 7 so that onNextClicked() will have the right value for valueOfDayOfWeek
            this.valueOfDayOfWeek -= 7;


        } else if(atEndOfMonth) {
            //TODO possibly do some exception handling for if the value is still negative
            int indexOfLastDayOfMonth = -1;
            int valueOfLastDayOfMonth = -1;
            for(int i = 0; i < arrayOfLabels.length; i++) {
                if (HelperMethods.isStringAnInt(arrayOfLabels[i].getText())) {
                    //for months where last day of month is on a saturday:
                    if (i == arrayOfLabels.length - 1) {
                        indexOfLastDayOfMonth = i;
                        valueOfLastDayOfMonth = Integer.parseInt(arrayOfLabels[indexOfLastDayOfMonth].getText());
                    } else {
                        //for months where last day of month is not on a saturday:
                        continue;
                    }
                } else {
                    indexOfLastDayOfMonth = i - 1;
                    valueOfLastDayOfMonth = Integer.parseInt(arrayOfLabels[indexOfLastDayOfMonth].getText());
                    break;
                }
            }

            int valueOfDayOfPreviousWeek = valueOfLastDayOfMonth - 7 - indexOfLastDayOfMonth;

            for(int j = 0; j < arrayOfLabels.length; j++) {
                arrayOfLabels[j].setText(String.valueOf(valueOfDayOfPreviousWeek));
                //adds appointments to ListView located at index j that are on the same day as the one represented by valueOfDayOfPreviousWeek
                addAppointmentsToListView(j, valueOfDayOfPreviousWeek);
                valueOfDayOfPreviousWeek++;
            }
            atEndOfMonth = false;
            //sets this.valueOfDayOfWeek back (indexOfLastDayOfMonth + 1) so that onNextClicked() will have the right value for valueOfDayOfWeek
            this.valueOfDayOfWeek -= (indexOfLastDayOfMonth + 1);

        } else if(atBeginningOfMonth){
            // if beginning of month has been reached then calls onMonthSelected() on previous month then calls onNextClicked() till end of previous month reached
            Month previousMonth = monthSelected.minus(1);
            //selects previousMonth which calls onMonthSelected()
            monthsComboBox.getSelectionModel().select(previousMonth);
            while(!atEndOfMonth){
                onNextClicked();
            }
        }
    }


    @FXML
    private void onNextClicked() {
        //causes alert if on last week of year
        if(monthSelected.equals(Month.DECEMBER) && atEndOfMonth) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Have reached end of current year. Please select the next year to find week after this one");
            alert.showAndWait();
            return;
        }
        // if end of month has not been reached then sets labels to the correct valueOfDay
        if(!atEndOfMonth) {
            resetLabelsAndListViews();
            atBeginningOfMonth = false;
            int valueOfFirstDayOfWeek = valueOfDayOfWeek;

            for (; valueOfDayOfWeek < valueOfFirstDayOfWeek + arrayOfLabels.length; valueOfDayOfWeek++) {
                if (valueOfDayOfWeek <= numberOfDaysInMonth) {
                    int correctIndexOfWeekDay = valueOfDayOfWeek - valueOfFirstDayOfWeek;
                    arrayOfLabels[correctIndexOfWeekDay].setText(String.valueOf(valueOfDayOfWeek));
                    //adds Appointments to ListView at correctIndexOfWeekDay that are on the same day as valueOfDayOfWeek
                    addAppointmentsToListView(correctIndexOfWeekDay, valueOfDayOfWeek);

                    // if end of month reached then sets atEndOfMonth to true increments valueOfDayOfWeek and breaks out of for-loop early
                    if(valueOfDayOfWeek == numberOfDaysInMonth){
                        atEndOfMonth = true;
                        valueOfDayOfWeek++;
                        break;
                    }
                }
            }
            // if end of month has been reached then calls onMonthSelected() on next month
        } else {
            Month nextMonth = monthSelected.plus(1);
            //selects next month which calls onMonthSelected()
            monthsComboBox.getSelectionModel().select(nextMonth);
        }
    }

    private void resetLabelsAndListViews() {
        for (Label label : arrayOfLabels) {
            label.setText("");
        }
        for(ListView<String> listView : arrayOfListViews) {
            listView.getItems().clear();
        }
    }
    private void resetListViews() {
        for(ListView<String> listView : arrayOfListViews) {
            listView.getItems().clear();
        }
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

