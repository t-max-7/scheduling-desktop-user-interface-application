package tmax7.scheduling.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import tmax7.scheduling.application.Appointment;
import tmax7.scheduling.application.Customer;
import tmax7.scheduling.application.MainApp;

import java.time.ZonedDateTime;

public class CustomerScheduleController {
    private MainApp mainApp;
    private Stage stage;
    private Customer customer;

    private ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();

    @FXML
    private Label customerIdLabel;
    @FXML
    private Label customerNameLabel;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment,Number> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment,Number> userIdColumn;
    @FXML
    private TableColumn<Appointment,String> titleColumn;
    @FXML
    private TableColumn<Appointment,String> descriptionColumn;
    @FXML
    private TableColumn<Appointment,String> locationColumn;
    @FXML
    private TableColumn<Appointment,String> contactColumn;
    @FXML
    private TableColumn<Appointment,String> typeColumn;
    @FXML
    private TableColumn<Appointment,String> urlColumn;
    @FXML
    private TableColumn<Appointment,ZonedDateTime> startColumn;
    @FXML
    private TableColumn<Appointment,ZonedDateTime> endColumn;

    @FXML
    private void initialize() {
        appointmentIdColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentIdProperty());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        contactColumn.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        urlColumn.setCellValueFactory(cellData -> cellData.getValue().urlProperty());
        startColumn.setCellValueFactory(cellData -> cellData.getValue().startProperty());
        endColumn.setCellValueFactory(cellData -> cellData.getValue().endProperty());

        appointmentTableView.setSortPolicy((TableView<Appointment> appointmentTableView) -> {
            FXCollections.sort(appointmentTableView.getItems(), (Appointment a, Appointment b) -> {
                return a.getStart().compareTo(b.getStart());
            });
            return true;
        });
        appointmentTableView.setItems(customerAppointments);
    }

    @FXML
    private void onOkClicked() {
        stage.close();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customerIdLabel.setText(String.valueOf(customer.getCustomerId()));
        customerNameLabel.setText(customer.getCustomerName());

        // fills customerAppointments with appointments that have same customer ID as the customer
        for(Appointment appointment : mainApp.getAppointments()) {
            if(appointment.getCustomerId() == customer.getCustomerId()) {
                customerAppointments.add(appointment);
            }
        }

        //for some reason have to call sort here to make sure tableView is sorted according to new sort policy
        appointmentTableView.sort();
    }
}
