package GUI;

import Controller.ClientController;
import Controller.MenuController.LoginController;
import Database.User;
import View.Exceptions.InvalidLoginException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class LoginMenu extends Application implements AlertFunction {
    @FXML
    TextField username;
    @FXML
    PasswordField password;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void login(MouseEvent mouseEvent) throws Exception {
        String username = this.username.getText();
        String password = this.password.getText();
        try {
            String serverMessage = ClientController.sendMessage("user login -u " + username + " -p " + password);
            if (serverMessage.isEmpty()) throw new Exception("SERVER RESPONSE NULL");
            if (serverMessage.startsWith("ERROR")) throw new Exception(serverMessage.substring(5));
            else
                new MainMenu(serverMessage).start(WelcomeMenu.stage);
        } catch (Exception e) {
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    public void back(MouseEvent mouseEvent) throws Exception {
        new WelcomeMenu().start(WelcomeMenu.stage);
    }

    @Override
    public void showAlert(String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Alert");
        alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        alert.setContentText(text);
        alert.show();
    }
}
