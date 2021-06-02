package tmax7.scheduling.application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Appointment {
    private SimpleIntegerProperty appointmentId = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty customerId = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty userId = new SimpleIntegerProperty(0);
    private SimpleStringProperty title = new SimpleStringProperty("none");
    private SimpleStringProperty description = new SimpleStringProperty("none");
    private SimpleStringProperty location = new SimpleStringProperty("none");
    private SimpleStringProperty contact = new SimpleStringProperty("none");
    private SimpleStringProperty type = new SimpleStringProperty("none");
    private SimpleStringProperty url = new SimpleStringProperty("none");
    private SimpleObjectProperty<ZonedDateTime> start = new SimpleObjectProperty<>(ZonedDateTime.now());
    private SimpleObjectProperty<ZonedDateTime> end = new SimpleObjectProperty<>(ZonedDateTime.now());

    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String type, String url, Timestamp start, Timestamp end) {
        this.appointmentId.setValue(appointmentId);
        this.customerId.setValue(customerId);
        this.userId.setValue(userId);
        this.title.setValue(title);
        this.description.setValue(description);
        this.location.setValue(location);
        this.contact.setValue(contact);
        this.type.setValue(type);
        this.url.setValue(url);

        ZoneId systemDefaultZoneId = ZoneId.systemDefault();

        Instant startAsInstant = start.toInstant();
        this.start.setValue(ZonedDateTime.ofInstant(startAsInstant, systemDefaultZoneId));

        Instant endAsInstant = end.toInstant();
        this.end.setValue(ZonedDateTime.ofInstant(endAsInstant, systemDefaultZoneId));
    }

    public Appointment(int customerId, int userId, String title, String description, String location, String contact, String type, String url, ZonedDateTime start, ZonedDateTime end) {
        this.customerId.setValue(customerId);
        this.userId.setValue(userId);
        this.title.setValue(title);
        this.description.setValue(description);
        this.location.setValue(location);
        this.contact.setValue(contact);
        this.type.setValue(type);
        this.url.setValue(url);
        this.start.setValue(start);
        this.end.setValue(end);
    }

    //getters and setters
    public int getAppointmentId() {
        return appointmentId.get();
    }

    public SimpleIntegerProperty appointmentIdProperty() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId.set(appointmentId);
    }

    public int getCustomerId() {
        return customerId.get();
    }

    public SimpleIntegerProperty customerIdProperty() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    public int getUserId() {
        return userId.get();
    }

    public SimpleIntegerProperty userIdProperty() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getContact() {
        return contact.get();
    }

    public SimpleStringProperty contactProperty() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getUrl() {
        return url.get();
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public ZonedDateTime getStart() {
        return start.get();
    }

    public SimpleObjectProperty<ZonedDateTime> startProperty() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start.set(start);
    }

    public ZonedDateTime getEnd() {
        return end.get();
    }

    public SimpleObjectProperty<ZonedDateTime> endProperty() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end.set(end);
    }
}
