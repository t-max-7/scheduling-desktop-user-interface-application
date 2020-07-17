package my.scheduling.application;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


public class CustomersPageController {
    private MainApp mainApp;

    @FXML
    private TextField searchCustomerIdTextField;

    @FXML
    private TableView<Customer> customersTableView;

    @FXML
    private TableColumn<Customer,Number> customerIdColumn;
    @FXML
    private TableColumn<Customer,String> nameColumn;
    @FXML
    private TableColumn<Customer,String> addressColumn;
    @FXML
    private TableColumn<Customer,String> address2Column;
    @FXML
    private TableColumn<Customer,String> cityColumn;
    @FXML
    private TableColumn<Customer,String> countryColumn;
    @FXML
    private TableColumn<Customer,String> postalCodeColumn;
    @FXML
    private TableColumn<Customer,String> phoneColumn;

    @FXML
    private void initialize() {
        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        //CellValue is set from simpleStringProperty from Address object stored in Customer
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().getAddress().addressProperty());
        address2Column.setCellValueFactory(cellData -> cellData.getValue().getAddress().address2Property());
        //CellValue is set from simpleStingProperty from City and Country objects stored in the Address object stored in Customer
        cityColumn.setCellValueFactory(cellData -> cellData.getValue().getAddress().getCity().cityNameProperty());
        countryColumn.setCellValueFactory(cellData -> cellData.getValue().getAddress().getCity().getCountry().countryNameProperty());
        //CellValue is set from simpleStringProperty from Address object stored in Customer
        postalCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getAddress().postalCodeProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().getAddress().phoneProperty());
    }

    @FXML
    private void onSearchCustomerIdClicked(){
        boolean customerMatchFound = false;
        try {
            int customerId = Integer.parseInt(searchCustomerIdTextField.getText());
            for(Customer customer : customersTableView.getItems()) {
                if(customer.getCustomerId() == customerId) {
                    customersTableView.getSelectionModel().select(customer);
                    customerMatchFound = true;
                    break;
                }
            }
            if(!customerMatchFound){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No customer with an customer ID of " + customerId + " was found");

                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Customer ID must be an integer (e.g. 543)");

            alert.showAndWait();
        }
    }

    @FXML
    private void onNumberOfCustomersByCountryReportClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CustomersPageController.class.getResource("NumberOfCustomersByCountryReport.fxml"));
            AnchorPane page = loader.load();

            Scene scene = new Scene(page);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Number Of Customers By Country");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainApp.getPrimaryStage());

            NumberOfCustomersByCountryReport_Controller controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setStage(stage);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void onViewCustomerScheduleClicked() {
        Customer customerSelected = customersTableView.getSelectionModel().getSelectedItem();
        if(customerSelected != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(CustomersPageController.class.getResource("CustomerSchedule.fxml"));
                AnchorPane page = loader.load();

                Scene scene = new Scene(page);

                Stage customerScheduleViewStage = new Stage();
                customerScheduleViewStage.setTitle("Schedule of " + customerSelected.getCustomerName());
                customerScheduleViewStage.initModality(Modality.WINDOW_MODAL);
                customerScheduleViewStage.initOwner(mainApp.getPrimaryStage());
                customerScheduleViewStage.setScene(scene);

                CustomerScheduleController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.setStage(customerScheduleViewStage);
                controller.setCustomer(customerSelected);

                customerScheduleViewStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onAddCustomerClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CustomersPageController.class.getResource("AddCustomerDialog.fxml"));
            AnchorPane page = loader.load();

            Scene scene = new Scene(page);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Customer");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.setScene(scene);

            AddCustomerDialogController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setStage(dialogStage);
            controller.setCustomerTableView(customersTableView);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onEditCustomerClicked() {
        Customer customerToEdit = customersTableView.getSelectionModel().getSelectedItem();
        if(customerToEdit != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(CustomersPageController.class.getResource("EditCustomerDialog.fxml"));
                AnchorPane page = loader.load();

                Scene scene = new Scene(page);

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Edit Customer");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(mainApp.getPrimaryStage());
                dialogStage.setScene(scene);

                EditCustomerDialogController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.setStage(dialogStage);
                controller.setCustomer(customerToEdit);
                controller.setCustomersPageController(this);

                dialogStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    @FXML
    private void onDeleteCustomerClicked() {
        Customer customerToDelete = customersTableView.getSelectionModel().getSelectedItem();
        if(customerToDelete != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deleting a customer record removes it permanently from the database. Do you wish to proceed?");
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> deleteCustomerFromDatabase(customerToDelete));
        }
    }

    public void deleteCustomerFromDatabase(Customer customerToDelete) {
        Connection databaseConnection = mainApp.getDatabaseConnection();
        try {
            int customerId_OfCustomerToDelete = customerToDelete.getCustomerId();

            //deletes customer's appointment from appointments table in database
            String deleteFromAppointmentsWhereString = "DELETE FROM appointment WHERE customerId = " + customerId_OfCustomerToDelete;
            PreparedStatement deleteFromAppointmentsWhere = databaseConnection.prepareStatement(deleteFromAppointmentsWhereString);
            deleteFromAppointmentsWhere.executeUpdate();

            //deletes customer from customer table in database
            String deleteFromCustomerWhereString = "DELETE FROM customer WHERE customerId = " + customerId_OfCustomerToDelete;
            PreparedStatement deleteFromCustomerWhere = databaseConnection.prepareStatement(deleteFromCustomerWhereString);
            deleteFromCustomerWhere.executeUpdate();

            //deletes customer's address from address table in database
            int addressId_OfAddressToRemove = customerToDelete.getAddressId();
            String deleteFromAddressWhereString = "DELETE FROM address WHERE addressId = " + addressId_OfAddressToRemove;
            PreparedStatement deleteFromAddressWhere = databaseConnection.prepareStatement(deleteFromAddressWhereString);
            deleteFromAddressWhere.executeUpdate();


            //removes customer's appointments from mainApp's appointments ObservableList using removeAll on a filteredList
            List<Appointment> listOfAppointmentsToRemove = mainApp.getAppointments().stream().filter(a -> (a.getCustomerId() == customerId_OfCustomerToDelete)).collect(Collectors.toList());
            mainApp.getAppointments().removeAll(listOfAppointmentsToRemove);

            //removes customer from mainApp customer ObservableList
            int indexOfCustomerToRemove = customersTableView.getSelectionModel().getSelectedIndex();
            mainApp.getCustomers().remove(indexOfCustomerToRemove);

            // removes customer's addresses from mainApp's address ObservableList using removeAll on a filteredList
            List<Address> listOfAddressesToRemove = mainApp.getAddresses().stream().parallel().filter(a -> (a.getAddressId() == addressId_OfAddressToRemove)).collect(Collectors.toList());   //USE OF LAMBDA EXPRESSION: This use of a lambda expression makes this use of a parallel stream (which will greatly improve performance if there are large amounts of addresses stored in mainApp) more clear and understandable
            mainApp.getAddresses().removeAll(listOfAddressesToRemove);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.customersTableView.setItems(this.mainApp.getCustomers());
    }

    public void refreshCustomersTableView() {
        this.customersTableView.refresh();
    }
}
