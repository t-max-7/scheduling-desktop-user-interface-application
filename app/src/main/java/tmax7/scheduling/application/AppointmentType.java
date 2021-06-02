package tmax7.scheduling.application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.Month;

public class AppointmentType {
    private int year;
    private Month month;
    private SimpleStringProperty type = new SimpleStringProperty("none");
    private SimpleIntegerProperty numberOfAppointmentsWithType = new SimpleIntegerProperty(0);

    public AppointmentType(int year, Month month, String type) {
        this.year = year;
        this.month = month;
        this.type.setValue(type);
    }

    void incrementNumberOfAppointmentsWithType() {
        int oldValue = numberOfAppointmentsWithType.getValue();
        numberOfAppointmentsWithType.setValue(oldValue + 1);
    }

    //getters and setters
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
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

    public int getNumberOfAppointmentsWithType() {
        return numberOfAppointmentsWithType.get();
    }

    public SimpleIntegerProperty numberOfAppointmentsWithTypeProperty() {
        return numberOfAppointmentsWithType;
    }

    public void setNumberOfAppointmentsWithType(int numberOfAppointmentsWithType) {
        this.numberOfAppointmentsWithType.set(numberOfAppointmentsWithType);
    }

    @Override
    public boolean equals(Object thatObject) {
        if(thatObject == this) {
            return true;
        }
        if(thatObject instanceof  AppointmentType) {
            AppointmentType thatAppointmentType = (AppointmentType) thatObject;
            if(
                    thatAppointmentType.getYear() == this.year
                && thatAppointmentType.getMonth().equals(this.month)
                && thatAppointmentType.getType().equals(this.type.getValue())
            ) {
               return true;
            }
        }
        return false;
    }

}
