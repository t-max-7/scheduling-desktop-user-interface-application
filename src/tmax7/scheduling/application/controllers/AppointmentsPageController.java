package tmax7.scheduling.application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tmax7.scheduling.application.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;


public class AppointmentsPageController {
    private MainApp mainApp;

    @FXML
    private TextField searchAppointmentIdTextField;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment,Number> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment,Number> customerIdColumn;
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
        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        contactColumn.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        urlColumn.setCellValueFactory(cellData -> cellData.getValue().urlProperty());
        startColumn.setCellValueFactory(cellData -> cellData.getValue().startProperty());
        endColumn.setCellValueFactory(cellData -> cellData.getValue().endProperty());
    }

    @FXML
    private void onSearchAppointmentIdClicked(){
        boolean appointmentMatchFound = false;
        try {
            int appointmentId = Integer.parseInt(searchAppointmentIdTextField.getText());
            for(Appointment appointment : appointmentTableView.getItems()) {
                if(appointment.getAppointmentId() == appointmentId) {
                    appointmentTableView.getSelectionModel().select(appointment);
                    appointmentMatchFound = true;
                    break;
                }
            }
            if(!appointmentMatchFound){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No appointment with an appointment ID of " + appointmentId + " was found");

                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Appointment ID must be an integer (e.g. 543)");

            alert.showAndWait();
        }
    }

    @FXML
    private void onAppointmentTypeReportClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AppointmentsPageController.class.getResource("AppointmentTypeReport.fxml"));
            AnchorPane page = loader.load();

            Scene scene = new Scene(page);

            Stage appointmentTypeReportStage = new Stage();
            appointmentTypeReportStage.setTitle("Appointment Type Report");
            appointmentTypeReportStage.initModality(Modality.WINDOW_MODAL);
            appointmentTypeReportStage.initOwner(mainApp.getPrimaryStage());
            appointmentTypeReportStage.setScene(scene);

            AppointmentTypeReportController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setStage(appointmentTypeReportStage);


            appointmentTypeReportStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onGetCustomerInfoClicked() {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if(selectedAppointment != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(AppointmentsPageController.class.getResource("CustomerInfo.fxml"));
                AnchorPane page = loader.load();

                Scene scene = new Scene(page);

                Stage stage = new Stage();
                stage.setTitle("Customer Info");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(mainApp.getPrimaryStage());
                stage.setScene(scene);

                CustomerInfoController controller = loader.getController();
                controller.setStage(stage);
                //get customer with customerId found in selectedAppointment and set controller with it
                for(Customer customer : mainApp.getCustomers()) {
                    if(customer.getCustomerId() == selectedAppointment.getCustomerId()) {
                        controller.setCustomer(customer);
                        break;
                    }
                }

                stage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onAddAppointmentClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AppointmentsPageController.class.getResource("AddAppointmentDialog.fxml"));
            AnchorPane dialog = loader.load();

            Scene scene = new Scene(dialog);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Appointment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.setScene(scene);

            AddAppointmentDialogController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setStage(dialogStage);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onEditAppointmentClicked() {
        Appointment appointmentToEdit = appointmentTableView.getSelectionModel().getSelectedItem();
        if (appointmentToEdit != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(AppointmentsPageController.class.getResource("EditAppointmentDialog.fxml"));
                AnchorPane page = loader.load();

                Scene scene = new Scene(page);

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Edit Appointment");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(mainApp.getPrimaryStage());
                dialogStage.setScene(scene);

                EditAppointmentDialogController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.setStage(dialogStage);
                controller.setAppointmentToEdit(appointmentToEdit);
                controller.setAppointmentsPageController(this);

                dialogStage.showAndWait();
            } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    @FXML
    private void onDeleteAppointmentClicked() {
        Appointment appointmentToDelete = appointmentTableView.getSelectionModel().getSelectedItem();
        if(appointmentToDelete != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deleting an appointment record removes it permanently from the database. Do you wish to proceed?");
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> deleteAppointmentFromDatabase(appointmentToDelete));

        }
    }

    private void deleteAppointmentFromDatabase(Appointment appointmentToDelete) {
        Connection databaseConnection = mainApp.getDatabaseConnection();
        try {
            //deletes appointment from appointment table in database
            int appointmentId_OfAppointmentToDelete = appointmentToDelete.getAppointmentId();
            String deleteFromAppointmentWhereString = "DELETE FROM appointment WHERE appointmentId = " + appointmentId_OfAppointmentToDelete;
            PreparedStatement deleteFromAppointmentsWhere = databaseConnection.prepareStatement(deleteFromAppointmentWhereString);
            deleteFromAppointmentsWhere.executeUpdate();

            //removes appointment from mainApp's appointments ObservableList
            int indexOf_AppointmentToRemove = appointmentTableView.getSelectionModel().getSelectedIndex();
            mainApp.getAppointments().remove(indexOf_AppointmentToRemove);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.appointmentTableView.setItems(this.mainApp.getAppointments());
    }

    public void refreshAppointmentsTableView() {
        this.appointmentTableView.refresh();
    }
}
