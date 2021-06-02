package tmax7.scheduling.application;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class City {
    private SimpleIntegerProperty cityId = new SimpleIntegerProperty(0);
    private SimpleStringProperty cityName = new SimpleStringProperty("none");

    private SimpleIntegerProperty countryId = new SimpleIntegerProperty(0);
    private SimpleObjectProperty<Country> country = new SimpleObjectProperty<>(new Country());

    //
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    public City() {
    }

    public City(String cityName, String createdBy, String lastUpdateBy) {
        this.cityName.setValue(cityName);
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
    }

    public City(int cityId, String cityName, int countryId, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.cityId.setValue(cityId);
        this.cityName.setValue(cityName);
        this.countryId.setValue(countryId);
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }
    //getters and setters

    public int getCityId() {
        return cityId.get();
    }

    public IntegerProperty cityIdProperty() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId.set(cityId);
    }

    public String getCityName() {
        return cityName.get();
    }

    public StringProperty cityNameProperty() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName.set(cityName);
    }

    public int getCountryId() {
        return countryId.get();
    }

    public IntegerProperty countryIdProperty() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId.set(countryId);
    }

    public Country getCountry() {
        return country.get();
    }

    public SimpleObjectProperty<Country> countryProperty() {
        return country;
    }

    public void setCountry(Country country) {
        this.country.set(country);
        this.countryId.setValue(country.getCountryId());
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
