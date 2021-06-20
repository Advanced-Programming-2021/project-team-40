package GUI;

import Controller.MenuController.MainMenuController;
import Database.User;
import View.Exceptions.InvalidMenuNameException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainMenu extends Application {

    public static User currentUser = null;

    public MainMenu(){

    }

    public MainMenu(User user){
        currentUser = user;
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/mainMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logout(MouseEvent mouseEvent) throws Exception {
        currentUser = null;
        MainMenuController.getInstance().toUpperMenu();
        new WelcomeMenu().start(WelcomeMenu.stage);
    }

    public void newDuel(MouseEvent mouseEvent) throws Exception {
        MainMenuController.getInstance().toLowerMenu("Duel");
        new DuelMenu().start(WelcomeMenu.stage);
    }

    public void deckMenu(MouseEvent mouseEvent) {
    }

    public void scoreboard(MouseEvent mouseEvent) throws Exception {
        new ScoreboardMenu().start(WelcomeMenu.stage);
    }

    public void profileMenu(MouseEvent mouseEvent) {
    }

    public void shop(MouseEvent mouseEvent) {
    }

    public void importExport(MouseEvent mouseEvent) {
    }
}
