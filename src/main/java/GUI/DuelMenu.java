package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DuelMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/duelMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void AIBattle(MouseEvent mouseEvent) {
    }

    public void playerBattle(MouseEvent mouseEvent) {

    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(WelcomeMenu.stage);
    }
}
