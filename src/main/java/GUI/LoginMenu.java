package GUI;

import Controller.MenuController.LoginController;
import View.Exceptions.InvalidLoginException;
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

public class LoginMenu extends Application {
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

    public void login(MouseEvent mouseEvent) {
        String username = this.username.getText();
        String password = this.password.getText();
        try {
            LoginController.loginUser(username, password);
            System.out.println("user logged in successfully!");
            //TODO
        } catch (InvalidLoginException e) {
            System.out.println(e.getMessage());
        }
    }


    public void back(MouseEvent mouseEvent) throws Exception {
        new WelcomeMenu().start(WelcomeMenu.stage);
    }
}
