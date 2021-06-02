package tmax7.scheduling.application.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import tmax7.scheduling.application.Customer;
import tmax7.scheduling.application.MainApp;

public class NumberOfCustomersByCountryReport_Controller {
    MainApp mainApp;
    Stage stage;

    @FXML
    private TableView<CountryCustomerReport> countryCustomerReportTableView;
    @FXML
    private TableColumn<CountryCustomerReport, String> countryColumn;
    @FXML
    private TableColumn<CountryCustomerReport, Number> numberOfCustomersColumn;

    @FXML
    private void initialize() {
        countryColumn.setCellValueFactory(cellData -> cellData.getValue().countyNameProperty());
        numberOfCustomersColumn.setCellValueFactory(cellData -> cellData.getValue().numberOfCustomersProperty());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        countryCustomerReportTableView.setItems(generateCountryCustomerReports(mainApp));
    }

    private ObservableList<CountryCustomerReport> generateCountryCustomerReports(MainApp mainApp) {
        ObservableList<CountryCustomerReport> countryCustomerReports = FXCollections.observableArrayList();

        for(Customer customer : mainApp.getCustomers()) {
            int countryId = customer.getAddress().getCity().getCountry().getCountryId();
            String countryName = customer.getAddress().getCity().getCountry().getCountryName();

            CountryCustomerReport cCR = new CountryCustomerReport(countryId, countryName);
            cCR.setNumberOfCustomers(1);
            boolean shouldAdd = true;

            //if countryCustomer is a duplicate then increments the number of Customers associated with that country, else it will add a new countryCustomerReports associated with a different country
            if(countryCustomerReports.size() > 0) {
                for(CountryCustomerReport countryCustomerReport : countryCustomerReports) {
                    if(cCR.equals(countryCustomerReport)) {
                        countryCustomerReport.incrementNumberOfCustomers();
                        shouldAdd = false;
                        break;
                    }
                }

                if(shouldAdd) {
                    countryCustomerReports.add(cCR);
                }
                // ^ if above not associated with else below !!!!!!!!!!!!
            } else {
                countryCustomerReports.add(cCR);
            }
        }

        return countryCustomerReports;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

class CountryCustomerReport {
    private int countryId;
    private SimpleStringProperty countyName = new SimpleStringProperty("none");
    private SimpleIntegerProperty numberOfCustomers = new SimpleIntegerProperty(0);

    public CountryCustomerReport(int countryId, String countryName) {
        this.countryId = countryId;
        this.countyName.setValue(countryName);
    }

    void incrementNumberOfCustomers() {
        int oldValue = numberOfCustomers.getValue();
        numberOfCustomers.setValue(oldValue + 1);
    }

    public void setNumberOfCustomers(int numberOfCustomers) {
        this.numberOfCustomers.set(numberOfCustomers);
    }

    public int getCountryId() {
        return countryId;
    }

    public String getCountyName() {
        return countyName.get();
    }

    public SimpleStringProperty countyNameProperty() {
        return countyName;
    }

    public int getNumberOfCustomers() {
        return numberOfCustomers.get();
    }

    public SimpleIntegerProperty numberOfCustomersProperty() {
        return numberOfCustomers;
    }

    @Override
    public boolean equals(Object thatObject) {
        if(thatObject == this) {
            return true;
        }
        if(thatObject instanceof CountryCustomerReport) {
            CountryCustomerReport thatCountryCustomerReport = (CountryCustomerReport) thatObject;
            if(thatCountryCustomerReport.getCountryId() == this.countryId) {
                return true;
            }
        }
        return false;
    }
}
