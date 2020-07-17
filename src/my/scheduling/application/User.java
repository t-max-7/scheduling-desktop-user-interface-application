package my.scheduling.application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User{
    private SimpleIntegerProperty userId = new SimpleIntegerProperty(0);
    private SimpleStringProperty userName = new SimpleStringProperty("none");

    public User(int userId, String userName) {
        this.userId.setValue(userId);
        this.userName.setValue(userName);
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

    public String getUserName() {
        return userName.get();
    }

    public SimpleStringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    @Override
    public String toString() {
        return userName.getValue();
    }
}
