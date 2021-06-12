package GUI;

import Controller.MenuController.LoginController;
import View.Exceptions.RepetitiveNicknameException;
import View.Exceptions.RepetitiveUsernameException;
import View.Exceptions.WeakPasswordException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.time.temporal.Temporal;

public class SignupMenu extends Application {

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

    public void signup(MouseEvent mouseEvent) {
        String username = this.username.getText();
        String password = this.password.getText();
        String nickname = this.nickname.getText();
        try {
            LoginController.registerUser(username, password, nickname);
            System.out.println("user created successfully!");
            //TODO
        } catch (RepetitiveUsernameException | RepetitiveNicknameException | WeakPasswordException e) {
            System.out.println(e.getMessage());
        }
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new WelcomeMenu().start(WelcomeMenu.stage);
    }
}
