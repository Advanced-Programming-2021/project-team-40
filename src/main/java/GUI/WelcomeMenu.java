package GUI;

import Controller.DatabaseController.DatabaseController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WelcomeMenu extends Application {
    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        WelcomeMenu.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("fxml/welcome.fxml"));
        stage.setTitle("YOU ARE GAY!");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize(){
        DatabaseController.getInstance();
    }

    public void signup(MouseEvent mouseEvent) throws Exception {
        new SignupMenu().start(stage);
    }

    public void login(MouseEvent mouseEvent) throws Exception {
        new LoginMenu().start(stage);
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
