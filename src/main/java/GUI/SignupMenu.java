package GUI;

import Controller.MenuController.LoginController;
import Database.User;
import View.Exceptions.RepetitiveNicknameException;
import View.Exceptions.RepetitiveUsernameException;
import View.Exceptions.WeakPasswordException;
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

import java.time.temporal.Temporal;

public class SignupMenu extends Application implements AlertFunction{

    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    TextField nickname;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/signup.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void signup(MouseEvent mouseEvent) throws Exception {
        String username = this.username.getText();
        String password = this.password.getText();
        String nickname = this.nickname.getText();
        try {
            LoginController.registerUser(username, password, nickname);
            new MainMenu(User.getUserByName(username)).start(WelcomeMenu.stage);
        } catch (RepetitiveUsernameException | RepetitiveNicknameException | WeakPasswordException e) {
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
            System.out.println(e.getMessage());
        }
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new WelcomeMenu().start(WelcomeMenu.stage);
    }

    @Override
    public void showAlert(String text, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle("Alert");
        alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        alert.setContentText(text);
        alert.show();
    }

}
