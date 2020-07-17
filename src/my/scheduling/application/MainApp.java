package my.scheduling.application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.time.temporal.ChronoUnit.MINUTES;

public class MainApp extends Application {
    private Stage primaryStage;
    private Locale locale;
    private String iso3Language;
    private String iso3Country;
    private Connection databaseConnection;
    private int userId;
    private String userName;
    private BusinessHours businessHours;
    private String[] arrayOfCountryNames;
    private static int yearsLimit = 80;

    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private ObservableList<Address> addresses = FXCollections.observableArrayList();
    private ObservableList<City> cities = FXCollections.observableArrayList();
    private ObservableList<Country> countries = FXCollections.observableArrayList();

    //start method
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        //create List Of Countries
        this.arrayOfCountryNames = createArrayOfCountryNames();
        // sets the language and country based on user's system's default
        setLocale(Locale.getDefault());
        //connect to uCertify database
        connectToDatabase();
        //show login form
        showLogInForm();
        //get data from database after user logged in
        retrieveDataFromDatabase();
    }

    //getters and setters
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setLocale(Locale locale) {
        this.locale = locale;
        this.iso3Country = this.locale.getISO3Country();
        this.iso3Language = this.locale.getISO3Language();
    }

    public String getIso3Language() {
        return iso3Language;
    }

    public String getIso3Country() {
        return iso3Country;
    }

    //TODO find out if most methods not private should be default
    Connection getDatabaseConnection() {
        return databaseConnection;
    }

    int getUserId() { return userId; }

    String getUserName() { return userName; }

    BusinessHours getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(BusinessHours businessHours) {
        this.businessHours = businessHours;
    }

    public String[] getArrayOfCountryNames() { return arrayOfCountryNames; }

    public ObservableList<User> getUsers() { return users; }

    public ObservableList<Appointment> getAppointments() {
        return appointments;
    }

    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    public ObservableList<Address> getAddresses() {
        return addresses;
    }

    public ObservableList<City> getCities() {
        return cities;
    }

    public ObservableList<Country> getCountries() {
        return countries;
    }

    public static int getYearsLimit() {
        return yearsLimit;
    }

    //end of getters and setters

    private String[] createArrayOfCountryNames() {
        String[] arrayOfCountryNames = {
                "Afghanistan", "Albania", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan",
                "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi",
                "Côte d'lvoire", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czechia",
                "Democratic Republic of the Congo", "Denmark", "Dijibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia",
                "Fiji", "Finland", "France",
                "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Granada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana",
                "Haiti", "Holy See", "Honduras", "Hungary",
                "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy",
                "Jamaica", "Japan", "Jordan",
                "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan",
                "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
                "Madagascar", "Malawi", "Malasia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar",
                "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Korea", "North Macedonia", "Norway",
                "Oman",
                "Pakistan", "Palau", "Palestine State", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal",
                "Qatar",
                "Romania", "Russia", "Rwanda",
                "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia",
                "Solomon Islands", "Somalia", "South Africa", "South Korea", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Sweden", "Switzerland", "Syria",
                "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu",
                "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States of America", "Uruguay", "Uzbekistan",
                "Vanuatu", "Venezuela", "Vietnam",
                "Yemen",
                "Zambia", "Zimbabwe"
        };
        return arrayOfCountryNames;
    }


    private void connectToDatabase() {
        String databaseURL = "jdbc:mysql://3.227.166.251/U071hy";
        //TODO find out if need these or if another solution:
        String userName ="U071hy";
        String password = "53688931963";
        try {
            databaseConnection = DriverManager.getConnection(databaseURL, userName, password);
        } catch(SQLException e) {
            //TODO see if want to print out other SQL information
            e.printStackTrace();
        }
    }

    private void retrieveDataFromDatabase() {
        retrieveUsersFromDatabase();
        retrieveAppointmentsFromDatabase();
        retrieveCustomersFromDatabase();
        retrieveAddressesFromDatabase();
        retrieveCitiesFromDatabase();
        retrieveCountriesFromDatabase();

        for(Customer customer: customers) {

            for(Address address : addresses) {
                if(customer.getAddressId() == address.getAddressId()) {
                    customer.setAddress(address);

                    for(City city : cities) {
                        if(customer.getAddress().getCityId() == city.getCityId()) {
                            customer.getAddress().setCity(city);

                            for(Country country : countries) {
                                if(customer.getAddress().getCity().getCountryId() == country.getCountryId()) {
                                    customer.getAddress().getCity().setCountry(country);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void retrieveUsersFromDatabase() {
        try {
            String selectUsersString = "SELECT * FROM user";
            PreparedStatement selectUsers = databaseConnection.prepareStatement(selectUsersString);
            ResultSet usersResultSet = selectUsers.executeQuery();

            while (usersResultSet.next()) {
                int userId = usersResultSet.getInt("userId");
                String userName = usersResultSet.getString("userName");

                User user = new User(userId, userName);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void retrieveAppointmentsFromDatabase() {
        try {
            String selectAppointmentsString = "SELECT * FROM appointment";
            PreparedStatement selectAppointments = databaseConnection.prepareStatement(selectAppointmentsString);
            ResultSet appointmentsResultSet = selectAppointments.executeQuery();

            while(appointmentsResultSet.next()) {
                int appointmentId = appointmentsResultSet.getInt("appointmentId");
                int customerId = appointmentsResultSet.getInt("customerId");
                int userId = appointmentsResultSet.getInt("userId");
                String title = appointmentsResultSet.getString("title");
                String description = appointmentsResultSet.getString("description");
                String location = appointmentsResultSet.getString("location");
                String contact = appointmentsResultSet.getString("contact");
                String type = appointmentsResultSet.getString("type");
                String url = appointmentsResultSet.getString("url");
                Timestamp start = appointmentsResultSet.getTimestamp("start");
                Timestamp end = appointmentsResultSet.getTimestamp("end");

                Appointment appointment = new Appointment(appointmentId, customerId, userId, title, description, location, contact, type, url, start, end);
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void retrieveCustomersFromDatabase() {
        try {
            String selectCustomersString =  "SELECT * FROM customer";
            PreparedStatement selectCustomers = databaseConnection.prepareStatement(selectCustomersString);
            ResultSet customersResultSet = selectCustomers.executeQuery();

            while(customersResultSet.next()) {
                int customerId = customersResultSet.getInt("customerId");
                String customerName = customersResultSet.getString("customerName");
                int addressId = customersResultSet.getInt("addressId");
                byte active = customersResultSet.getByte("active");
                Timestamp createDate = customersResultSet.getTimestamp("createDate");
                String createdBy = customersResultSet.getString("createdBy");
                Timestamp lastUpdate = customersResultSet.getTimestamp("lastUpdate");
                String lastUpdateBy = customersResultSet.getString("lastUpdateBy");

                Customer customer = new Customer(customerId, customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy);
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void retrieveAddressesFromDatabase() {
        try {
            String selectAddressesString = "SELECT * FROM address";
            PreparedStatement selectAddresses = databaseConnection.prepareStatement(selectAddressesString);
            ResultSet addressesResultSet = selectAddresses.executeQuery();

            while(addressesResultSet.next()) {
                int addressId = addressesResultSet.getInt("addressId");
                String address = addressesResultSet.getString("address");
                String address2 = addressesResultSet.getString("address2");
                int cityId = addressesResultSet.getInt("cityId");
                String postalCode = addressesResultSet.getString("postalCode");
                String phone = addressesResultSet.getString("phone");
                Timestamp createDate = addressesResultSet.getTimestamp("createDate");
                String createdBy = addressesResultSet.getString("createdBy");
                Timestamp lastUpdate = addressesResultSet.getTimestamp("lastUpdate");
                String lastUpdateBy = addressesResultSet.getString("lastUpdateBy");

                Address addressObject = new Address(addressId, address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy);
                addresses.add(addressObject);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void retrieveCitiesFromDatabase() {
        try {
            String selectCitiesString = "SELECT * FROM city";
            PreparedStatement selectCities = databaseConnection.prepareStatement(selectCitiesString);
            ResultSet citiesResultSet = selectCities.executeQuery();

            while(citiesResultSet.next()) {
                int cityId = citiesResultSet.getInt("cityId");
                String cityName = citiesResultSet.getString("city");
                int countryId = citiesResultSet.getInt("countryId");
                Timestamp createDate = citiesResultSet.getTimestamp("lastUpdate");
                String createdBy = citiesResultSet.getString("createdBy");
                Timestamp lastUpdate = citiesResultSet.getTimestamp("lastUpdate");
                String lastUpdateBy = citiesResultSet.getString("lastUpdateBy");

                City city = new City(cityId, cityName, countryId, createDate, createdBy, lastUpdate, lastUpdateBy);
                cities.add(city);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void retrieveCountriesFromDatabase() {
        try {
            String selectCountriesString = "SELECT * FROM country";
            PreparedStatement selectCountries = databaseConnection.prepareStatement(selectCountriesString);
            ResultSet countriesResultSet = selectCountries.executeQuery();

            while(countriesResultSet.next()) {
                int countryId = countriesResultSet.getInt("countryId");
                String countryName = countriesResultSet.getString("country");
                Timestamp createDate = countriesResultSet.getTimestamp("createDate");
                String createdBy = countriesResultSet.getString("createdBy");
                Timestamp lastUpdate = countriesResultSet.getTimestamp("lastUpdate");
                String lastUpdateBy = countriesResultSet.getString("lastUpdateBy");

                Country country = new Country(countryId, countryName, createDate, createdBy, lastUpdate, lastUpdateBy);
                countries.add(country);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkIfUserHasAppointment() {
        ZonedDateTime currentTime = ZonedDateTime.now();
        String alertMessage = "";
        //checks which, if any, of the appointments are within the next 15 minutes.
        for(Appointment appointment : appointments) {
            ZonedDateTime appointmentStartTime = appointment.getStart();

            long minutes_BetweenCurrentTime_AndAppointmentTime = currentTime.until(appointmentStartTime, MINUTES);

            if(minutes_BetweenCurrentTime_AndAppointmentTime > 0 && minutes_BetweenCurrentTime_AndAppointmentTime <= 15){
                 alertMessage += "Appointment ID: " + appointment.getAppointmentId() + "\n";
            }
        }
        //if any appointments are within the next 15 minutes then shows an alert
        if(!alertMessage.equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("You have an appointment within the next 15 minutes.\n" + alertMessage);
            alert.showAndWait();
        }
    }

    private void showLogInForm() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("LogInForm.fxml"));
            AnchorPane logInForm = (AnchorPane) loader.load();

            LogInFormController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(logInForm);
            primaryStage.setScene(scene);

            primaryStage.show();

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void logIn(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        logUserLogIn();
        showBusinessHoursDialog();
    }

    private void logUserLogIn() {
        ZonedDateTime logInTime = ZonedDateTime.now();
        String timeStamp = "User ID: " + userId + ", " + "User Name: " + userName + ", Login Time: " + logInTime.toString() + "\n";

        try {
            Logger logger = Logger.getLogger("my.scheduling.application");
            //IMPORTANT stores log file on desktop!!!!!!!!!!!!!!!!!!!!!!"
            //also appends message to end of log file if file already exists
            Handler fileHandler = new FileHandler("%h/Desktop/scheduling_desktop_user_interface_application_log_file.txt",true);
            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);
            logger.info(timeStamp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showBusinessHoursDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("BusinessHoursPage.fxml"));
            AnchorPane businessHoursPage = loader.load();

            BusinessHoursPageController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(businessHoursPage);

            primaryStage.setScene(scene);

            // checks if user has an appointment in next 15 minutes;
            checkIfUserHasAppointment();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("UserPage.fxml"));
            AnchorPane userPage = (AnchorPane) loader.load();

            UserPageController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(userPage);

            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            //set title to userName
            primaryStage.setTitle(userName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //returns an ObservableList of all the different AppointmentTypes of the appointments in mainApp
    public ObservableList<AppointmentType> generateAppointmentTypes() {
        ObservableList<AppointmentType> appointmentTypes = FXCollections.observableArrayList();

        for(Appointment appointment : appointments) {
            int year = appointment.getStart().getYear();
            Month month = appointment.getStart().getMonth();
            String type = appointment.getType();

            AppointmentType a = new AppointmentType(year, month, type);
            a.setNumberOfAppointmentsWithType(1);
            boolean shouldAdd = true;

            // if appointment type is a duplicate then increments the number of Appointments with that type, else it will add new AppointmentType to appointmentTypes list
            if(appointmentTypes.size() > 0) {
                for(AppointmentType appointmentType : appointmentTypes){
                    if(a.equals(appointmentType)) {
                        appointmentType.incrementNumberOfAppointmentsWithType();
                        shouldAdd = false;
                        break;
                    }
                }

                if(shouldAdd) {
                    appointmentTypes.add(a);
                }
                // ^ if above not associated with else below !!!!!!!!!!!!
            } else {
                appointmentTypes.add(a);
            }
        }

        return appointmentTypes;
    }


    //main method
    public static void main(String[] args) { launch(args); }
}
