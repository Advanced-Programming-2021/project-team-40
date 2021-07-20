package GUI;

import Controller.ClientController;
import Controller.DatabaseController.DatabaseController;
import Controller.MenuController.MainMenuController;
import Database.EfficientUser;
import Database.User;
import View.Exceptions.InvalidMenuNameException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MainMenu extends Application {

    public static User currentUser = null;
    public static String userToken = null;

    public MainMenu(){
        DatabaseController.getInstance();
        if (DuelMenu.mediaPlayer != null && DuelMenu.mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
            DuelMenu.mediaPlayer.stop();
            WelcomeMenu.gameMusic();
        }
    }

    public MainMenu(String token){
        userToken = token;
        String userString = ClientController.sendMessage(userToken + " get user");
        Gson gson = new GsonBuilder().create();
        EfficientUser efficientUser = gson.fromJson(userString, EfficientUser.class);
        DatabaseController.getInstance().createUserFromEffUser(efficientUser);
        currentUser = User.getUserByName(efficientUser.getUsername());
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/mainMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        WelcomeMenu.stage = stage;
    }

    public void logout(MouseEvent mouseEvent) throws Exception {
        ClientController.sendMessage(userToken + " user logout");
        userToken = null;
        MainMenuController.getInstance().toUpperMenu();
        new WelcomeMenu().start(WelcomeMenu.stage);
    }

    public void newDuel(MouseEvent mouseEvent) throws Exception {
        MainMenuController.getInstance().toLowerMenu("Duel");
        new DuelMenu().start(WelcomeMenu.stage);
    }

    public void deckMenu(MouseEvent mouseEvent) throws Exception {
        new DeckMenu().start(WelcomeMenu.stage);
    }

    public void scoreboard(MouseEvent mouseEvent) throws Exception {
        new ScoreboardMenu().start(WelcomeMenu.stage);
    }

    public void profileMenu(MouseEvent mouseEvent) throws Exception {
        new ProfileMenu().start(WelcomeMenu.stage);
    }

    public void shop(MouseEvent mouseEvent) throws Exception {
        new ShopMenu().start(WelcomeMenu.stage);
    }

    public void importExport(MouseEvent mouseEvent) {
    }

    public void chat() throws Exception {
        new ChatBox().start(WelcomeMenu.stage);
    }
}
