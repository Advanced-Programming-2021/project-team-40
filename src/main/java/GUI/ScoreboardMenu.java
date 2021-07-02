package GUI;

import Database.User;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
        HashMap<User, Integer> users = getSortedUsers();
        for (User user : users.keySet()) {
            setUserBox(user, users.get(user));
        }

    }

    private void setUserBox(User user, int rank){
    }


    private HashMap<User, Integer> getSortedUsers() {
        ArrayList<User> users = User.getUsers();
        sortUsers(users);
        HashMap<User, Integer> rankedUsers = new HashMap<>();
        int previousScore = users.get(0).getScore();
        int rank = 1;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getScore() < previousScore) {
                rank = i + 1;
                previousScore = users.get(i).getScore();
            }
            rankedUsers.put(users.get(i), rank);
        }
        return rankedUsers;
    }

    private void sortUsers(ArrayList<User> users) {
        users.sort(nicknameComparator);
        users.sort(scoreComparator);
    }

    public static Comparator<User> nicknameComparator = new Comparator<>() {
        @Override
        public int compare(User firstUser, User secondUser) {
            String first = firstUser.getUsername().toUpperCase();
            String second = secondUser.getUsername().toUpperCase();
            return first.compareTo(second);
        }
    };

    public static Comparator<User> scoreComparator = new Comparator<>() {
        @Override
        public int compare(User firstUser, User secondUser) {
            int first = firstUser.getScore();
            int second = secondUser.getScore();
            return Integer.compare(second, first);
        }
    };
}
