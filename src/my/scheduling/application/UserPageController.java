package my.scheduling.application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class UserPageController {
    MainApp mainApp;

    @FXML
    private Button customersButton;
    @FXML
    private Button appointmentsButton;
    @FXML
    private Button calendarButton;
    @FXML
    private Button consultantsButton;

    @FXML
    private AnchorPane userPageAnchorPane;


    private Border mouseEnteredBorder = new Border(new BorderStroke(Paint.valueOf("white"),   Paint.valueOf("white"),   Paint.valueOf("white"),  Paint.valueOf("white"),
                                                                    BorderStrokeStyle.SOLID,  BorderStrokeStyle.SOLID,  BorderStrokeStyle.SOLID,          BorderStrokeStyle.SOLID,
                                                                    CornerRadii.EMPTY, BorderWidths.DEFAULT, null));

    private Border selectedBorder = new Border(new BorderStroke(Paint.valueOf("white"),   Paint.valueOf("cornflowerblue"),   Paint.valueOf("cornflowerblue"),  Paint.valueOf("cornflowerblue"),
                                                                BorderStrokeStyle.SOLID,  BorderStrokeStyle.SOLID,  BorderStrokeStyle.SOLID,          BorderStrokeStyle.SOLID,
                                                                CornerRadii.EMPTY, BorderWidths.DEFAULT, null));

    private boolean customersIsSelected;
    private boolean appointmentsIsSelected;
    private boolean calendarIsSelected;
    private boolean consultantsIsSelected;

    @FXML
    private void onCustomers_MouseEntered() {
        if(!customersIsSelected) {
            customersButton.setBorder(mouseEnteredBorder);
        }
    }

    @FXML
    private void onCustomers_MouseExited() {
        if(!customersIsSelected) {
            customersButton.setBorder(null);
        }
    }

    @FXML
    private void onCustomers_Clicked() {
        customersButton.setBorder(selectedBorder);
        customersIsSelected = true;

        //reset other buttons
        appointmentsButton.setBorder(null);
        appointmentsIsSelected = false;

        calendarButton.setBorder(null);
        calendarIsSelected = false;

        consultantsButton.setBorder(null);
        consultantsIsSelected = false;

        //Show customers page
        showCustomersPage();
    }

    @FXML
    private void onAppointments_MouseEntered() {
        if(!appointmentsIsSelected) {
            appointmentsButton.setBorder(mouseEnteredBorder);
        }
    }

    @FXML
    private void onAppointments_MouseExited() {
        if(!appointmentsIsSelected) {
            appointmentsButton.setBorder(null);
        }
    }

    @FXML
    private void onAppointments_Clicked() {
        appointmentsButton.setBorder(selectedBorder);
        appointmentsIsSelected = true;

        //reset other buttons
        customersButton.setBorder(null);
        customersIsSelected = false;

        calendarButton.setBorder(null);
        calendarIsSelected = false;

        consultantsButton.setBorder(null);
        consultantsIsSelected = false;

        //show appointments page
        showAppointmentsPage();
    }

    @FXML
    private void onCalendar_MouseEntered() {
        if(!calendarIsSelected) {
            calendarButton.setBorder(mouseEnteredBorder);
        }
    }

    @FXML
    private void onCalendar_MouseExited() {
        if(!calendarIsSelected) {
            calendarButton.setBorder(null);
        }
    }

    @FXML
    private void onCalendar_Clicked() {
        calendarButton.setBorder(selectedBorder);
        calendarIsSelected = true;

        //reset other buttons
        customersButton.setBorder(null);
        customersIsSelected = false;

        appointmentsButton.setBorder(null);
        appointmentsIsSelected = false;

        consultantsButton.setBorder(null);
        consultantsIsSelected = false;

        //show calendarPage
        showCalendarMonthPage();
    }

    @FXML
    private void onConsultants_MouseEntered() {
        if(!consultantsIsSelected) {
            consultantsButton.setBorder(mouseEnteredBorder);
        }
    }

    @FXML
    private void onConsultants_MouseExited() {
        if(!consultantsIsSelected) {
            consultantsButton.setBorder(null);
        }
    }

    @FXML
    private void onConsultants_Clicked() {
        consultantsButton.setBorder(selectedBorder);
        consultantsIsSelected = true;

        //reset other buttons
        customersButton.setBorder(null);
        customersIsSelected = false;

        appointmentsButton.setBorder(null);
        appointmentsIsSelected = false;

        calendarButton.setBorder(null);
        calendarIsSelected = false;

        showConsultantsPage();
    }

    public void showCustomersPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserPageController.class.getResource("CustomersPage.fxml"));
            AnchorPane customerPage = loader.load();

            CustomersPageController controller = loader.getController();
            controller.setMainApp(this.mainApp);

            //make sure userPageAnchorPane has no current children
            clearAnchorPane();
            //make customerPage fit to parent
            AnchorPane.setTopAnchor(customerPage, 20.00);
            AnchorPane.setLeftAnchor(customerPage, 0.00);
            AnchorPane.setRightAnchor(customerPage, 0.00);
            AnchorPane.setBottomAnchor(customerPage, 20.00);

            //add customerPage to userPageAnchorPane
            userPageAnchorPane.getChildren().add(customerPage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAppointmentsPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserPageController.class.getResource("AppointmentsPage.fxml"));
            AnchorPane appointmentsPage = loader.load();

            AppointmentsPageController controller = loader.getController();
            controller.setMainApp(this.mainApp);

            //make sure userPageAnchorPane has no current children
            clearAnchorPane();
            //make appointmentsPage fit to parent
            AnchorPane.setTopAnchor(appointmentsPage, 20.00);
            AnchorPane.setLeftAnchor(appointmentsPage, 0.00);
            AnchorPane.setRightAnchor(appointmentsPage, 0.00);
            AnchorPane.setBottomAnchor(appointmentsPage, 20.00);

            //add appointmentPage to userPageAnchorPane
            userPageAnchorPane.getChildren().add(appointmentsPage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCalendarMonthPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserPageController.class.getResource("CalendarMonthPage.fxml"));
            AnchorPane calendarPage = loader.load();

            CalendarMonthPageController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setUserPageController(this);

            //make sure userPageAnchorPane has no current children
            clearAnchorPane();
            //make calendarPage fit to parent
            AnchorPane.setTopAnchor(calendarPage, 20.00);
            AnchorPane.setLeftAnchor(calendarPage, 0.00);
            AnchorPane.setRightAnchor(calendarPage, 0.00);
            AnchorPane.setBottomAnchor(calendarPage, 20.00);

            //add calendarPage to userPageAnchorPane
            userPageAnchorPane.getChildren().add(calendarPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCalendarWeeklyViewPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserPageController.class.getResource("CalendarWeeklyViewPage.fxml"));
            AnchorPane weeklyViewPage = loader.load();

            CalendarWeeklyViewPageController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setUserPageController(this);

            //make sure userPageAnchorPane has no current children
            clearAnchorPane();
            //make calendarPage fit to parent
            AnchorPane.setTopAnchor(weeklyViewPage, 20.00);
            AnchorPane.setLeftAnchor(weeklyViewPage, 0.00);
            AnchorPane.setRightAnchor(weeklyViewPage, 0.00);
            AnchorPane.setBottomAnchor(weeklyViewPage, 20.00);

            //add calendarPage to userPageAnchorPane
            userPageAnchorPane.getChildren().add(weeklyViewPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showConsultantsPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserPageController.class.getResource("ConsultantsPage.fxml"));
            AnchorPane consultantsPage = loader.load();

            ConsultantsPageController controller = loader.getController();
            controller.setMainApp(this.mainApp);

            //make sure userPageAnchorPane has no current children
            clearAnchorPane();
            //make calendarPage fit to parent
            AnchorPane.setTopAnchor(consultantsPage, 20.00);
            AnchorPane.setLeftAnchor(consultantsPage, 0.00);
            AnchorPane.setRightAnchor(consultantsPage, 0.00);
            AnchorPane.setBottomAnchor(consultantsPage, 20.00);

            //add calendarPage to userPageAnchorPane
            userPageAnchorPane.getChildren().add(consultantsPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearAnchorPane() {
        if(userPageAnchorPane.getChildren().size() > 0) {
            userPageAnchorPane.getChildren().clear();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
