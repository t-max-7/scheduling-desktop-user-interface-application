package tmax7.scheduling.application.controllers;


import com.sun.jdi.InternalException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tmax7.scheduling.application.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddCustomerDialogController {
    private MainApp mainApp;
    private Connection databaseConnection;
    private Stage stage;
    private TableView<Customer> customerTableView;

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField address2TextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private TextField phoneTextField;

    @FXML
    private void initialize() {
        //set default address2Test to none
        address2TextField.setText("none");
    }

    @FXML
    private void onOkClicked() {
        if(isInputValid()) {
            String name = nameTextField.getText();
            String address =addressTextField.getText();
            String address2 =address2TextField.getText();
            String city = cityTextField.getText();
            String country = countryComboBox.getValue();
            String postalCode = postalCodeTextField.getText();
            String phone = phoneTextField.getText();

            String createdBy = mainApp.getUserName();
            String lastUpdateBy = createdBy;

            //create objects from text field input and userName from mainApp
            Customer customerObject = new Customer(name, createdBy, lastUpdateBy);
            Address addressObject = new Address(address, address2, postalCode, phone, createdBy, lastUpdateBy);
            City cityObject = new City(city, createdBy, lastUpdateBy);
            Country countryObject = new Country(country, createdBy, lastUpdateBy);

            /*adds objects to database and update them with database generated id's*/
            // adds countryObject to database only if it is not already in database
            try {
                int countryId = isCountryInDatabase(countryObject) ? getCountryIdFromDatabase(countryObject) : addCountryIntoDatabase(countryObject);
                countryObject.setCountryId(countryId);

                cityObject.setCountry(countryObject);
                int cityId = isCityInDatabase(cityObject) ? getCityIdFromDatabase(cityObject) : addCityIntoDatabase(cityObject);
                cityObject.setCityId(cityId);


                addressObject.setCity(cityObject);
                int addressId = addAddressIntoDataBase(addressObject);
                addressObject.setAddressId(addressId);

                customerObject.setAddress(addressObject);
                int customerId = addCustomerIntoDatabase(customerObject);
                customerObject.setCustomerId(customerId);

                mainApp.getCustomers().add(customerObject);
            } catch (InternalException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());

                alert.showAndWait();
            }

            stage.close();
        }
    }

    private boolean isInputValid() {
       String errorMessage = "";

       if(nameTextField.getText() == null || nameTextField.getText().equals("")) {
           errorMessage += "Name field cannot be empty\n";
       }
       if(addressTextField.getText() == null || nameTextField.getText().equals("")) {
           errorMessage += "Address field cannot be empty\n";
       }
       if(address2TextField.getText() == null || address2TextField.getText().equals("")) {
           errorMessage += "Address 2 field cannot be empty: it must contain \"none\" or an address\n";
       }
       if(cityTextField.getText() == null || cityTextField.getText().equals("")) {
           errorMessage += "City field cannot be empty\n";
       }
       if(countryComboBox.getValue() == null || countryComboBox.getValue().equals("")) {
           errorMessage += "A country must be selected\n";
       }
       if(postalCodeTextField.getText() == null || postalCodeTextField.getText().equals("")) {
           errorMessage += "Postal Code field cannot be empty\n";
       }
       if(phoneTextField.getText() == null || phoneTextField.getText().equals("")) {
           errorMessage += "Phone field cannot be empty\n";
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

    private boolean isCountryInDatabase(Country countryObject) {
        try {
            String selectCountryWhereString = "SELECT * FROM country WHERE country = \'" + countryObject.getCountryName() + "\'";
            PreparedStatement selectCountryWhere = databaseConnection.prepareStatement(selectCountryWhereString);
            ResultSet countryResultSet = selectCountryWhere.executeQuery();
            //TODO see if this actually returns false if there are no matches
            //returns true if countryResult has at least one row containing a matching country
            return countryResultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getCountryIdFromDatabase(Country countryObject) {
        try {
            String selectCountryWhereString = "SELECT * FROM country WHERE country = \'" + countryObject.getCountryName() + "\'";
            PreparedStatement selectCountryWhere = databaseConnection.prepareStatement(selectCountryWhereString);
            ResultSet countryResultSet = selectCountryWhere.executeQuery();
            //returns countryId of first match which is hopefully only match
            countryResultSet.next();
            int countryId = countryResultSet.getInt("countryId");
            return countryId;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int addCountryIntoDatabase(Country countryObject) {
        String country = countryObject.getCountryName();
        String createdBy = countryObject.getCreatedBy();
        String lastUpdateBy = countryObject.getLastUpdateBy();
        try {
            //statement to get id of last record inserted
            String selectLastInsertIdString = "SELECT LAST_INSERT_ID()";
            PreparedStatement selectLastInsertId = databaseConnection.prepareStatement(selectLastInsertIdString);

            //statement to insert country into country table
            String insertCountryString = "INSERT INTO country(country, createDate, createdBy, lastUpdateBy) VALUES(" +
                    "\'" + country +"\', NOW(), \'" + createdBy +"\', \'" + lastUpdateBy +"\')";
            PreparedStatement insertCountry = databaseConnection.prepareStatement(insertCountryString);
            insertCountry.executeUpdate();

            //get id of inserted country and return it
            ResultSet lastInsertResultSet = selectLastInsertId.executeQuery();
            lastInsertResultSet.next();
            int firstColumn = 1;
            int countryId_OfInsertedCountry = lastInsertResultSet.getInt(firstColumn);

            return countryId_OfInsertedCountry;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private boolean isCityInDatabase(City cityObject) {
        String city = cityObject.getCityName();
        int countryId = cityObject.getCountryId();
        try {
            String selectFromCityWhereString = "SELECT * FROM city WHERE city = \'" + city + "\' AND countryId = " + countryId;
            PreparedStatement selectFromCityWhere = databaseConnection.prepareStatement(selectFromCityWhereString);
            ResultSet cityResultSet = selectFromCityWhere.executeQuery();
            //TODO see if this actually returns false if there are no matches
            //returns true if cityResult has at least one row containing a matching city
            return cityResultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getCityIdFromDatabase(City cityObject) {
        String city = cityObject.getCityName();
        int countryId = cityObject.getCountryId();
        try {
            String selectFromCityWhereString = "SELECT * FROM city WHERE city = \'" + city + "\' AND countryId = " + countryId;
            PreparedStatement selectFromCityWhere = databaseConnection.prepareStatement(selectFromCityWhereString);
            ResultSet cityResultSet = selectFromCityWhere.executeQuery();
            //TODO see if this actually returns false if there are no matches
            //returns true if cityResult has at least one row containing a matching city
            cityResultSet.next();
            int cityId = cityResultSet.getInt("cityId");
            return cityId;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int addCityIntoDatabase(City cityObject) {
        if(cityObject.getCountryId() != 0) {
            String cityName = cityObject.getCityName();
            int countryId = cityObject.getCountryId();
            String createdBy = cityObject.getCreatedBy();
            String lastUpdateBy = cityObject.getLastUpdateBy();

            try {
                //statement to get id of last record inserted
                String selectLastInsertIdString = "SELECT LAST_INSERT_ID()";
                PreparedStatement selectLastInsertId = databaseConnection.prepareStatement(selectLastInsertIdString);

                //statement to insert city into city table
                String insertCityString = "INSERT INTO city(city, countryId, createDate, createdBy, lastUpdateBy) VALUES(" +
                                            "\'" + cityName +"\', " + countryId + ", NOW(), \'" + createdBy +"\', \'" + lastUpdateBy + "\')";
                PreparedStatement insertCity = databaseConnection.prepareStatement(insertCityString);
                insertCity.executeUpdate();

                //get id of inserted city and return it
                ResultSet lastInsertResultSet = selectLastInsertId.executeQuery();
                lastInsertResultSet.next();
                int firstColumn = 1;
                int cityId_OfInsertedCity = lastInsertResultSet.getInt(firstColumn);

                return cityId_OfInsertedCity;

            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            throw new InternalException("Something went wrong adding city into database");
        }
    }

    private int addAddressIntoDataBase(Address addressObject) {
        if(addressObject.getCityId() != 0) {
            String address = addressObject.getAddress();
            String address2 = addressObject.getAddress2();
            int cityId = addressObject.getCityId();
            String postalCode = addressObject.getPostalCode();
            String phone = addressObject.getPhone();
            String createdBy = addressObject.getCreatedBy();
            String lastUpdateBy = addressObject.getLastUpdateBy();

            try {
                //statement to get id of last record inserted
                String selectLastInsertIdString = "SELECT LAST_INSERT_ID()";
                PreparedStatement selectLastInsertId = databaseConnection.prepareStatement(selectLastInsertIdString);

                //statement to insert address into address table
                String insertAddressString = "INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) VALUES("
                        + "\'" + address + "\', \'" + address2 + "\', " + cityId + ", \'" + postalCode + "\', \'" + phone + "\', NOW(), \'" + createdBy + "\', \'" + lastUpdateBy + "\')";

                PreparedStatement insertAddress = databaseConnection.prepareStatement(insertAddressString);
                insertAddress.executeUpdate();

                //get id of inserted address and return it
                ResultSet lastInsertResultSet = selectLastInsertId.executeQuery();
                lastInsertResultSet.next();
                int firstColumn = 1;
                int addressId_OfInsertedAddress = lastInsertResultSet.getInt(firstColumn);

                return addressId_OfInsertedAddress;

            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }
        }  else {
            throw new InternalException("Something went wrong when adding address into database");
        }
    }

    private int addCustomerIntoDatabase(Customer customerObject) {
        if(customerObject.getAddressId() != 0) {
            String customerName = customerObject.getCustomerName();
            int addressId = customerObject.getAddressId();
            byte active = customerObject.getActive();
            String createdBy = customerObject.getCreatedBy();
            String lastUpdateBy = customerObject.getLastUpdateBy();

            try {
                //statement to get id of last record inserted
                String selectLastInsertIdString = "SELECT LAST_INSERT_ID()";
                PreparedStatement selectLastInsertId = databaseConnection.prepareStatement(selectLastInsertIdString);

                //statement to insert customer into customer table
                String insertCustomerString = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdateBy) VALUES("
                                                + "\'" + customerName + "\', " + addressId + ", " + active + ", NOW(), \'" + createdBy + "\', \'" + lastUpdateBy + "\')";
                PreparedStatement insertCustomer = databaseConnection.prepareStatement(insertCustomerString);
                insertCustomer.executeUpdate();

                //get id of inserted customer and return it
                ResultSet lastInsertResultSet = selectLastInsertId.executeQuery();
                lastInsertResultSet.next();
                int firstColumn = 1;
                int customerId_OfInsertedCustomer = lastInsertResultSet.getInt(firstColumn);

                return customerId_OfInsertedCustomer;

            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }
        }  else {
            //TODO see if should do different error handling here and everywhere where InternalException thrown
            throw new InternalException("Something went wrong when adding customer into database");
        }
    }

    @FXML
    private void onCancelClicked() {
        stage.close();
    }

    //setters
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.databaseConnection = mainApp.getDatabaseConnection();
        countryComboBox.setItems(FXCollections.observableArrayList(mainApp.getArrayOfCountryNames()));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCustomerTableView(TableView<Customer> customerTableView) {
        this.customerTableView = customerTableView;
    }

}
