package GUI;

import Controller.ClientController;
import Database.EfficientUser;
import Database.Message;
import Database.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ScoreboardMenu extends Application {

    @FXML
    TableView table;

    public Thread tableRefreshThread;
    public boolean stopRefresh = false;

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
        tableRefreshThread = new Thread() {
            @Override
            public void run() {
                for (EfficientUser user : getEfficientUsers()) {
                    table.getItems().add(user);
                }
                while (!stopRefresh) {
                    int i = 0;
                    for (EfficientUser user : getEfficientUsers()) {
                        if (table.getItems().size() > i) table.getItems().remove(i);
                        table.getItems().add(i, user);
                        i++;
                    }
                    table.sort();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        tableRefreshThread.start();

    }

    private ArrayList<EfficientUser> getEfficientUsers() {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " request efficient users");
        Type efficientUserList = new TypeToken<ArrayList<EfficientUser>>() {
        }.getType();
        Gson gson = new Gson();
        ArrayList<EfficientUser> actualList = gson.fromJson(serverMessage, efficientUserList);
        EfficientUser.updateRanks(actualList);
        return actualList;
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        stopRefresh = true;
        new MainMenu().start(WelcomeMenu.stage);
    }

}
