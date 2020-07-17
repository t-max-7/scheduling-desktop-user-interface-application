package my.scheduling.application;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;

public class Country {
    private SimpleIntegerProperty countryId = new SimpleIntegerProperty(0);
    private SimpleStringProperty countryName = new SimpleStringProperty("none");

    //
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    //getters and setters
    public Country() {
    }

    public Country(String countryName, String createdBy, String lastUpdateBy) {
        this.countryName.setValue(countryName);
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
    }

    public Country(int countryId, String countryName, Timestamp createDate, String createdby, Timestamp lastUpdate, String lastUpdateBy) {
        this.countryId.setValue(countryId);
        this.countryName.setValue(countryName);
        this.createDate = createDate;
        this.lastUpdateBy = lastUpdateBy;
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

    public String getCountryName() {
        return countryName.get();
    }

    public StringProperty countryNameProperty() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName.set(countryName);
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
