package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class DuelMenu extends Application {

    public static MediaPlayer mediaPlayer;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/duelMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void AIBattle(MouseEvent mouseEvent) {
    }

    public void playerBattle(MouseEvent mouseEvent) throws Exception {
        startGameMusic();
        new GameplayView().start(WelcomeMenu.stage);
    }

    private void startGameMusic() {
        WelcomeMenu.mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(new Media(DuelMenu.class.getResource("/Audio/gamemusic.mp3").toExternalForm()));
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(WelcomeMenu.stage);
    }
}
