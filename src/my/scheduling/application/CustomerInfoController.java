package my.scheduling.application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CustomerInfoController {

    private Stage stage;
    private Customer customer;

    @FXML
    private Label customerIdLabel;
    @FXML
    private Label customerNameLabel;
    @FXML
    private Label phoneLabel;

    @FXML
    private void initialize() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customerIdLabel.setText(String.valueOf(customer.getCustomerId()));
        customerNameLabel.setText(customer.getCustomerName());
        phoneLabel.setText(customer.getAddress().getPhone());
    }
}
