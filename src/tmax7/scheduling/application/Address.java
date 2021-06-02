package tmax7.scheduling.application;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class Address {
    private SimpleIntegerProperty addressId = new SimpleIntegerProperty(0);
    private SimpleStringProperty address = new SimpleStringProperty("none");
    private SimpleStringProperty address2 = new SimpleStringProperty("none");

    private SimpleIntegerProperty cityId = new SimpleIntegerProperty(0);
    private SimpleObjectProperty<City> city = new SimpleObjectProperty<>(new City());

    private SimpleStringProperty postalCode = new SimpleStringProperty("none");
    private SimpleStringProperty phone = new SimpleStringProperty("none");

    //
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    public Address() {
    }

    public Address(String address, String address2, String postalCode, String phone, String createdBy, String lastUpdateBy) {
        this.address.setValue(address);
        this.address2.setValue(address2);
        this.postalCode.setValue(postalCode);
        this.phone.setValue(phone);
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
    }

    public Address(int addressId, String address, String address2, int cityId, String postalCode, String phone, Timestamp createDate,
                    String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.addressId.setValue(addressId);
        this.address.setValue(address);
        this.address2.setValue(address2);
        this.cityId.setValue(cityId);
        this.postalCode.setValue(postalCode);
        this.phone.setValue(phone);
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }
    //getters and setters

    public int getAddressId() {
        return addressId.get();
    }

    public IntegerProperty addressIdProperty() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId.set(addressId);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getAddress2() {
        return address2.get();
    }

    public StringProperty address2Property() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2.set(address2);
    }

    public int getCityId() {
        return cityId.get();
    }

    public IntegerProperty cityIdProperty() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId.set(cityId);
    }

    public City getCity() {
        return city.get();
    }

    public SimpleObjectProperty<City> cityProperty() {
        return city;
    }

    public void setCity(City city) {
        this.city.set(city);
        this.cityId.setValue(city.getCityId());
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
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
