package my.scheduling.application;

import javafx.scene.control.Alert;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

public class HelperMethods {
    //TODO see where these methods where used to replace previous methods and see if they still make sense

    /* Checks if appointment is valid FOR CURRENT USER.
    Checks first if the start time is after end if this is false
    then checks if start and end are outside of business hours if this is false
    then checks if the start and end times overlap with another appointment time
     */
    public static boolean isValidAppointmentTime(ZonedDateTime start, ZonedDateTime end, MainApp mainApp) {
        boolean isValid = true;
        String errorMessage = "";
        if(start.isAfter(end)) {
            errorMessage += "Appointment start time must be before appointment end time\n";
            isValid = false;
        } else if (!isWithinBusinessHours(start, end, mainApp)) {
            errorMessage += "Appointment outside of business hours:\n" +
                            mainApp.getBusinessHours().toString() + "\n";
            isValid = false;
        } else {
            for (Appointment appointment : mainApp.getAppointments()) {
                if(appointment.getUserId() == mainApp.getUserId()) {
                    if (end.isBefore(appointment.getStart())) {
                        continue;
                    } else if (start.isAfter(appointment.getEnd())){
                        continue;
                    } else {
                        errorMessage += "The given appointment time overlaps with the appointment with ID " + appointment.getAppointmentId() + "\n"
                                + "which has the following time: " + appointment.getStart().toLocalTime() + " - " + appointment.getEnd().toLocalTime() + "\n";
                        isValid = false;
                    }
                }
            }
        }

        if(!errorMessage.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);

            alert.showAndWait();
        }
        return isValid;
    }
    /* for use when the appointment is being edited*/
    public static boolean isValidAppointmentTime(ZonedDateTime start, ZonedDateTime end, MainApp mainApp, int appointmentId) {
        boolean isValid = true;
        String errorMessage = "";
        if(start.isAfter(end)) {
            errorMessage += "Appointment start time must be before appointment end time\n";
            isValid = false;
        } else if (!isWithinBusinessHours(start, end, mainApp)) {
            errorMessage += "Appointment outside of business hours:\n" +
                    mainApp.getBusinessHours().toString() + "\n";
            isValid = false;
        } else {
            for (Appointment appointment : mainApp.getAppointments()) {
                if(appointment.getUserId() == mainApp.getUserId()) {
                    //if appointment's id is equal to appointmentId that means that it represents edited appointments old time so does nothing if they are equal
                    if(appointment.getAppointmentId() != appointmentId) {
                        if (end.isBefore(appointment.getStart())) {
                            continue;
                        } else if (start.isAfter(appointment.getEnd())){
                            continue;
                        } else {
                            errorMessage += "The given appointment time overlaps with the appointment with ID " + appointment.getAppointmentId() + "\n"
                                    + "which has the following time: " + appointment.getStart().toLocalTime() + " - " + appointment.getEnd().toLocalTime() + "\n";
                            isValid = false;
                        }
                    }
                }
            }
        }

        if(!errorMessage.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);

            alert.showAndWait();
        }
        return isValid;
    }

    private static boolean isWithinBusinessHours(ZonedDateTime start, ZonedDateTime end, MainApp mainApp) {
        BusinessHours businessHours = mainApp.getBusinessHours();
        DayOfWeek appointmentDay = start.getDayOfWeek();

        OffsetTime startOffsetTime = start.toOffsetDateTime().toOffsetTime();
        OffsetTime endOffsetTime = end.toOffsetDateTime().toOffsetTime();

       if(appointmentDay == DayOfWeek.MONDAY) {
           if (!isStartWithinBusinessHours(startOffsetTime, businessHours.getMondayStartTime(), businessHours.getMondayEndTime())) {
               return false;
           }
           if (!isEndWithinBusinessHours(endOffsetTime, businessHours.getMondayStartTime(), businessHours.getMondayEndTime())) {
               return false;
           }
       }

        if(appointmentDay == DayOfWeek.TUESDAY) {
            if (!isStartWithinBusinessHours(startOffsetTime, businessHours.getTuesdayStartTime(), businessHours.getTuesdayEndTime())) {
                return false;
            }
            if (!isEndWithinBusinessHours(endOffsetTime, businessHours.getTuesdayStartTime(), businessHours.getTuesdayEndTime())) {
                return false;
            }
        }

        if(appointmentDay == DayOfWeek.WEDNESDAY) {
            if (!isStartWithinBusinessHours(startOffsetTime, businessHours.getWednesdayStartTime(), businessHours.getWednesdayEndTime())) {
                return false;
            }
            if (!isEndWithinBusinessHours(endOffsetTime, businessHours.getWednesdayStartTime(), businessHours.getWednesdayEndTime())) {
                return false;
            }
        }

        if(appointmentDay == DayOfWeek.THURSDAY) {
            if (!isStartWithinBusinessHours(startOffsetTime, businessHours.getThursdayStartTime(), businessHours.getThursdayEndTime())) {
                return false;
            }
            if (!isEndWithinBusinessHours(endOffsetTime, businessHours.getThursdayStartTime(), businessHours.getThursdayEndTime())) {
                return false;
            }
        }

        if(appointmentDay == DayOfWeek.FRIDAY) {
            if (!isStartWithinBusinessHours(startOffsetTime, businessHours.getFridayStartTime(), businessHours.getFridayEndTime())) {
                return false;
            }
            if (!isEndWithinBusinessHours(endOffsetTime, businessHours.getFridayStartTime(), businessHours.getFridayEndTime())) {
                return false;
            }
        }

        return true;
    }

    private static boolean isStartWithinBusinessHours(OffsetTime startOffsetTime, OffsetTime businessStartTime, OffsetTime businessEndTime) {
        if(
                           ( startOffsetTime.isAfter(businessStartTime) || startOffsetTime.isEqual(businessStartTime) )
                        && ( startOffsetTime.isBefore(businessEndTime) )
        ) {
            return true;
        }
        return false;
    }

    private static boolean isEndWithinBusinessHours(OffsetTime endOffsetTime, OffsetTime businessStartTime, OffsetTime businessEndTime) {
        if (
                           ( endOffsetTime.isBefore(businessEndTime) || endOffsetTime.isEqual(businessEndTime) )
                        && ( endOffsetTime.isAfter(businessStartTime) )
        ) {
            return true;
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /* returns true is string is an integer  */
    public static boolean isStringAnInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /* checks if customerId matches any of the customerId's of the customers in the mainApp ObservableList of customers*/
    public static boolean isCustomerIdValid(String input, MainApp mainApp) {
        int customerId = Integer.parseInt(input);
        for(Customer customer : mainApp.getCustomers()) {
            if(customerId == customer.getCustomerId()) {
                return true;
            }
        }
        return false;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*returns int value of an hour string of the form "hh:mm" e.g 09:00*/
    public static int toHourInt(String input) {
        return Integer.parseInt(input.substring(0,2));
    }
    //////////////////////////////////////////////////////////////////////

    /* returns a string where the minute has a leading 0 if it is less than 10 e.g. 01, 07, 08*/
    public static String formatMinute(int minute) {
        String minuteAsString = String.valueOf(minute);
        if(minute < 10) {
            minuteAsString = "0" + minuteAsString;
        }
        return minuteAsString;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /*returns a string where the hour has a leading 0 if it is less than 10 e.g. 01, 07, 08 */
    public static String formatHour(int hour) {
        String hourAsString = String.valueOf(hour);
        if(hour < 10) {
            hourAsString = "0" + hourAsString;
        }
        return hourAsString;
    }
}
