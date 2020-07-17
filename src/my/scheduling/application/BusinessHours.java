package my.scheduling.application;

import java.time.OffsetTime;

public class BusinessHours {
    private OffsetTime mondayStartTime;
    private OffsetTime mondayEndTime;
    private OffsetTime tuesdayStartTime;
    private OffsetTime tuesdayEndTime;
    private OffsetTime wednesdayStartTime;
    private OffsetTime wednesdayEndTime;
    private OffsetTime thursdayStartTime;
    private OffsetTime thursdayEndTime;
    private OffsetTime fridayStartTime;
    private OffsetTime fridayEndTime;

    public BusinessHours(OffsetTime mondayStartTime, OffsetTime mondayEndTime, OffsetTime tuesdayStartTime, OffsetTime tuesdayEndTime, OffsetTime wednesdayStartTime, OffsetTime wednesdayEndTime, OffsetTime thursdayStartTime, OffsetTime thursdayEndTime, OffsetTime fridayStartTime, OffsetTime fridayEndTime) {
        this.mondayStartTime = mondayStartTime;
        this.mondayEndTime = mondayEndTime;
        this.tuesdayStartTime = tuesdayStartTime;
        this.tuesdayEndTime = tuesdayEndTime;
        this.wednesdayStartTime = wednesdayStartTime;
        this.wednesdayEndTime = wednesdayEndTime;
        this.thursdayStartTime = thursdayStartTime;
        this.thursdayEndTime = thursdayEndTime;
        this.fridayStartTime = fridayStartTime;
        this.fridayEndTime = fridayEndTime;
    }

    public OffsetTime getMondayStartTime() {
        return mondayStartTime;
    }

    public OffsetTime getMondayEndTime() {
        return mondayEndTime;
    }

    public OffsetTime getTuesdayStartTime() {
        return tuesdayStartTime;
    }

    public OffsetTime getTuesdayEndTime() {
        return tuesdayEndTime;
    }

    public OffsetTime getWednesdayStartTime() {
        return wednesdayStartTime;
    }

    public OffsetTime getWednesdayEndTime() {
        return wednesdayEndTime;
    }

    public OffsetTime getThursdayStartTime() {
        return thursdayStartTime;
    }

    public OffsetTime getThursdayEndTime() {
        return thursdayEndTime;
    }

    public OffsetTime getFridayStartTime() {
        return fridayStartTime;
    }

    public OffsetTime getFridayEndTime() {
        return fridayEndTime;
    }

    @Override
    public String toString() {
        String string = "Monday business hours: " + mondayStartTime.toLocalTime().toString() + " - " + mondayEndTime.toLocalTime().toString() + "\n" +
                        "Tuesday business hours: " + tuesdayStartTime.toLocalTime().toString() + " - " + tuesdayEndTime.toLocalTime().toString() + "\n" +
                        "Wednesday business hours: " + wednesdayStartTime.toLocalTime().toString() + " - " + wednesdayEndTime.toLocalTime().toString() + "\n" +
                        "Thursday business hours: " + thursdayStartTime.toLocalTime().toString() + " - " + thursdayEndTime.toLocalTime().toString() + "\n" +
                        "Friday business hours: " + fridayStartTime.toLocalTime().toString() + " - " + fridayEndTime.toLocalTime().toString() + "\n";
        return string;
    }
}
