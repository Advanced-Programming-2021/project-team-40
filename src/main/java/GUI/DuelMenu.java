package GUI;

import Controller.DuelController.GameplayController;
import Controller.MenuController.DeckMenuController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.ProgramController;
import Database.User;
import Gameplay.*;
import View.Exceptions.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class DuelMenu extends Application implements AlertFunction {

    public static MediaPlayer mediaPlayer;
    @FXML
    TextField username;

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
        User userTwo;
        String playerTwoUsername = username.getText();
        try {

            if ((userTwo = User.getUserByName(playerTwoUsername)) == null) throw new UserNotFoundException();
            if (userTwo.equals(MainMenu.currentUser)) throw new SameUserChosenException();
            if (MainMenu.currentUser.getActiveDeck() == null) throw new ActiveDeckNotFoundException(MainMenu.currentUser.getUsername());
            if (userTwo.getActiveDeck() == null) throw new ActiveDeckNotFoundException(playerTwoUsername);
            if (DeckMenuController.getInstance().isDeckInvalid(MainMenu.currentUser.getActiveDeck())) throw new InvalidDeckException(MainMenu.currentUser.getUsername());
            if (DeckMenuController.getInstance().isDeckInvalid(userTwo.getActiveDeck())) throw new InvalidDeckException(playerTwoUsername);
            //TODO implement round count
            if (3 != 3) throw new InvalidRoundNumberException();
            ProgramController.getInstance().setCurrentMenu(Menu.GAMEPLAY);
            startGameMusic();
            Gameplay gameplay = new Gameplay(new Player(MainMenu.currentUser),new Player(userTwo),Integer.parseInt("3"));
            GameplayController.getInstance().setGameplay(gameplay);
            new GameplayView().start(WelcomeMenu.stage);
        }catch (Exception e){
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void startGameMusic() {
        WelcomeMenu.mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(new Media(DuelMenu.class.getResource("/Audio/gamemusic.mp3").toExternalForm()));
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        mediaPlayer.setVolume(0);
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(WelcomeMenu.stage);
    }

    @Override
    public void showAlert(String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        alert.setContentText(text);
        alert.show();
    }
}
