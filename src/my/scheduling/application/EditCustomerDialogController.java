package my.scheduling.application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditCustomerDialogController {
    private MainApp mainApp;
    private Connection databaseConnection;
    private Stage stage;
    private Customer customerToEdit;
    private CustomersPageController customersPageController;

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
    private void onOkClicked() {
        if(isInputValid()) {
            String name = nameTextField.getText();
            String address =addressTextField.getText();
            String address2 =address2TextField.getText();
            String city = cityTextField.getText();
            String country = countryComboBox.getValue();
            String postalCode = postalCodeTextField.getText();
            String phone = phoneTextField.getText();
            String lastUpdateBy = mainApp.getUserName();
            String createdBy = mainApp.getUserName();

            try {
                //change customer name in customer table in the database
                customerToEdit.setCustomerName(name);
                customerToEdit.setLastUpdateBy(lastUpdateBy);
                String updateCustomerString = "UPDATE customer SET " +
                        "customerName = \'" + name + "\', " +
                        "lastUpdate = NOW(), " +
                        "lastUpdateBy = \'" + lastUpdateBy + "\' " +
                        "WHERE customerId = " + customerToEdit.getCustomerId();
                PreparedStatement updateCustomer = databaseConnection.prepareStatement(updateCustomerString);
                updateCustomer.executeUpdate();

                //edit Address info in Customer
                customerToEdit.getAddress().setAddress(address);
                customerToEdit.getAddress().setAddress2(address2);
                customerToEdit.getAddress().setPostalCode(postalCode);
                customerToEdit.getAddress().setPhone(phone);

                //create objects from text field input and userName from mainApp
                City cityObject = new City(city, createdBy, lastUpdateBy);
                Country countryObject = new Country(country, createdBy, lastUpdateBy);

                /*adds objects to database and update them with database generated id's*/
                // adds countryObject to database only if it is not already in database
                int countryId = isCountryInDatabase(countryObject) ? getCountryIdFromDatabase(countryObject) : addCountryIntoDatabase(countryObject);
                countryObject.setCountryId(countryId);

                cityObject.setCountry(countryObject);
                int cityId = isCityInDatabase(cityObject) ? getCityIdFromDatabase(cityObject) : addCityIntoDatabase(cityObject);
                cityObject.setCityId(cityId);

                // set customers addressObject with created cityObject that contains created countryObject
                customerToEdit.getAddress().setCity(cityObject);

                //update address info in database
                Address addressObject = customerToEdit.getAddress();
                String updateAddressString = "UPDATE address SET " +
                        "address = \'" + addressObject.getAddress() + "\', " +
                        "address2 = \'" + addressObject.getAddress2() + "\', " +
                        "cityId = " + addressObject.getCityId() + ", " +
                        "postalCode = \'" + addressObject.getPostalCode() + "\'," +
                        "phone = \'" + addressObject.getPhone() + "\'," +
                        "lastUpdate = NOW(), " +
                        "lastUpdateBy = \'" + lastUpdateBy + "\'" +
                        "WHERE addressId = " + addressObject.getAddressId();
                PreparedStatement updateAddress = databaseConnection.prepareStatement(updateAddressString);
                updateAddress.executeUpdate();

                customersPageController.refreshCustomersTableView();
                //close stage
                stage.close();
            } catch (SQLException e) {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());

                alert.showAndWait();
            } catch (InternalException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());

                alert.showAndWait();
            }
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
        }  else {
            //TODO see if should do different exception handling here and everywhere where InternalException is thrown
            throw new InternalException("Something went wrong when adding city into database");
        }
    }

    @FXML
    private void onCancelClicked() {
        stage.close();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.databaseConnection = this.mainApp.getDatabaseConnection();
        countryComboBox.setItems(FXCollections.observableArrayList(mainApp.getArrayOfCountryNames()));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCustomer(Customer customerToEdit) {
        this.customerToEdit = customerToEdit;
        if(this.customerToEdit != null) {
            nameTextField.setText(customerToEdit.getCustomerName());
            addressTextField.setText(customerToEdit.getAddress().getAddress());
            address2TextField.setText(customerToEdit.getAddress().getAddress2());
            cityTextField.setText(customerToEdit.getAddress().getCity().getCityName());
            countryComboBox.setValue(customerToEdit.getAddress().getCity().getCountry().getCountryName());
            postalCodeTextField.setText(customerToEdit.getAddress().getPostalCode());
            phoneTextField.setText(customerToEdit.getAddress().getPhone());
        }
    }

    public void setCustomersPageController(CustomersPageController controller) {
        this.customersPageController = controller;
    }
}
