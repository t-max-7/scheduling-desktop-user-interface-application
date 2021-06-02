package tmax7.scheduling.application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tmax7.scheduling.application.MainApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInFormController {
    private MainApp mainApp;
    private String iso3Language;
    private int userId;
    private String userName;

    @FXML
    private Label logInLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private TextField userNameTextField;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button logInButton;

    @FXML
    private void initialize() {

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        iso3Language = this.mainApp.getIso3Language();
        if (iso3Language.equals("spa")) {
            logInLabel.setText("Ingresar");
            userNameLabel.setText("Nombre de usario");
            passwordLabel.setText("Contraseña");
            logInButton.setText("Ingresar");
        } else if (iso3Language.equals("por")) {
            logInLabel.setText("Entrar");
            userNameLabel.setText("Nome do usuàrio");
            passwordLabel.setText("Senha");
            logInButton.setText("Entrar");
        }
    }

    @FXML
    private void onLoginClicked() {
        if (isInputValid()) {
            mainApp.logIn(userId, userName);
        }
    }

    private boolean isInputValid() {
        boolean isValid = false;
        try {
            Connection databaseConnection = mainApp.getDatabaseConnection();
            String selectUserNameString = "SELECT userId, userName, password FROM user";
            PreparedStatement selectUserName = databaseConnection.prepareStatement(selectUserNameString);
            ResultSet userNameResultSet = selectUserName.executeQuery();

            while (userNameResultSet.next()) {
                int userId = userNameResultSet.getInt("userId");
                String validUserName = userNameResultSet.getString("userName");
                String validPassword = userNameResultSet.getString("password");
                if (userNameTextField.getText().equals(validUserName)) {
                    if (passwordField.getText().equals(validPassword)) {
                        this.userId = userId;
                        this.userName = validUserName;
                        isValid = true;
                        break;
                    }
                }
            }

            if (!isValid) {
                showLogInAlert();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    private void showLogInAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String alertTitle = "Error";
        String alertContentText = "Username and/or password not valid";

        if(iso3Language.equals("spa")){
            alertTitle = "Error";
            alertContentText = "Nombre de usuario y/o contraseña no válidos";
        } else if(iso3Language.equals("por")) {
            alertTitle = "Erro";
            alertContentText = "Nome de usuário e/ou senha inválidos";
        }

        alert.setTitle(alertTitle);
        alert.setHeaderText("");
        alert.setContentText(alertContentText);
        alert.showAndWait();
    }

}
