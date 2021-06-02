package tmax7.scheduling.application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

public class Customer {
    private SimpleIntegerProperty customerId = new SimpleIntegerProperty(0);
    private SimpleStringProperty customerName = new SimpleStringProperty("none");

    private SimpleIntegerProperty addressId = new SimpleIntegerProperty(0);
    private SimpleObjectProperty<Address> address = new SimpleObjectProperty<>( new Address());
    //
    private byte active = 1;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    public Customer() {
    }

    public Customer(String customerName, String createdBy, String lastUpdateBy) {
       this.customerName.setValue(customerName);
       this.createdBy = createdBy;
       this.lastUpdateBy = lastUpdateBy;
    }

    //constructor
    public Customer(int customerId, String customerName, int addressId, byte active, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.customerId.setValue(customerId);
        this.customerName.setValue(customerName);
        this.addressId.setValue(addressId);
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    //getters and setters
    public int getCustomerId() {
        return customerId.get();
    }

    public SimpleIntegerProperty customerIdProperty() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public int getAddressId() {
        return addressId.get();
    }

    public SimpleIntegerProperty addressIdProperty() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId.set(addressId);
    }

    public Address getAddress() {
        return address.get();
    }

    public SimpleObjectProperty<Address> addressProperty() {
        return address;
    }

    public void setAddress(Address address) {
        this.address.set(address);
        this.addressId.setValue(address.getAddressId());
    }

    public byte getActive() {
        return active;
    }

    public void setActive(byte active) {
        this.active = active;
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
