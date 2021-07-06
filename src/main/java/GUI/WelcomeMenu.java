package GUI;

import Controller.DatabaseController.DatabaseController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class WelcomeMenu extends Application {
    public static Stage stage;
    public static MediaPlayer mediaPlayer;

    public static void main(String[] args) {
        gameMusic();
        launch(args);
    }

    public static void gameMusic() {
        String mediaAddress = WelcomeMenu.class.getResource("/Audio/menumusic.m4a").toExternalForm();
        mediaPlayer = new MediaPlayer(new Media(mediaAddress));
        mediaPlayer.play();
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    @Override
    public void start(Stage stage) throws Exception {
        WelcomeMenu.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("fxml/welcome.fxml"));
        stage.setTitle("YOU ARE GAY!");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void initialize() {
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
