package tmax7.scheduling.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import tmax7.scheduling.application.Appointment;
import tmax7.scheduling.application.MainApp;
import tmax7.scheduling.application.User;

import java.time.ZonedDateTime;

public class ConsultantsPageController {
    private MainApp mainApp;

    @FXML
    private ListView<User> usersListView;

    @FXML
    private TableView<Appointment> appointmentTableView;
    @FXML
    private TableColumn<Appointment, Number> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> startColumn;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> endColumn;

    @FXML
    private void initialize() {
        appointmentIdColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentIdProperty());
        startColumn.setCellValueFactory(cellData -> cellData.getValue().startProperty());
        endColumn.setCellValueFactory(cellData -> cellData.getValue().endProperty());

        // make tableView sort appointments in order of start time: earliest to latest
        appointmentTableView.setSortPolicy((TableView<Appointment> appointmentTableView) -> {
            FXCollections.sort(appointmentTableView.getItems(), (Appointment a, Appointment b) -> {
                return a.getStart().compareTo(b.getStart());
            });
            return true;
        });
    }

    @FXML
    private void onUserSelected() {
        User user = usersListView.getSelectionModel().getSelectedItem();

        if(user != null) {
            ObservableList<Appointment> appointmentsToDisplay = FXCollections.observableArrayList();
            for(Appointment appointment : mainApp.getAppointments()) {
                if(appointment.getUserId() == user.getUserId()) {
                    appointmentsToDisplay.add(appointment);
                }
            }
            appointmentTableView.setItems(appointmentsToDisplay);
            //for some reason have to call sort here to make sure tableView is sorted according to new sort policy
            appointmentTableView.sort();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        usersListView.setItems(this.mainApp.getUsers());
    }
}
