package GUI;

import Controller.ClientController;
import Database.EfficientUser;
import Database.User;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ScoreboardMenu extends Application {

    @FXML
    TableView table;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/scoreboard.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        createTable();

    }

    private void createTable() {
        TableColumn nameColumn = new TableColumn("Nickname");
        nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("nickname"));
        nameColumn.setSortable(false);
        TableColumn scoreColumn = new TableColumn("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<User, String>("score"));
        scoreColumn.setSortable(false);
        TableColumn rankColumn = new TableColumn("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<User, String>("rank"));
        rankColumn.setSortable(false);
        table.getColumns().addAll(rankColumn, nameColumn, scoreColumn);
        table.getSortOrder().add(rankColumn);
        table.getSortOrder().add(nameColumn);
        table.setEditable(false);
        for (EfficientUser user : getEfficientUsers()) {
            table.getItems().add(user);
        }
        table.sort();
    }

    private ArrayList<EfficientUser> getEfficientUsers() {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " request efficient users");
        ArrayList<EfficientUser> returning = new ArrayList<>();
        return returning;
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(WelcomeMenu.stage);
    }

}
