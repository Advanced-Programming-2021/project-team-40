package Controller.MenuController;

import Controller.ProgramController.Menu;
import Controller.ProgramController.ProgramController;
import Database.User;
import View.Exceptions.MenuNavigationNotPossibleException;
import View.ScoreboardView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreboardController implements MenuNavigation {
    private static ScoreboardController scoreboardController;
    private List<User> users;
    private final ScoreboardView scoreboardView = new ScoreboardView();

    private ScoreboardController() {

    }

    public static ScoreboardController getInstance() {
        if (scoreboardController == null)
            scoreboardController = new ScoreboardController();
        return scoreboardController;
    }
    private void sortUsers() {
        Comparator<User> byScore = Comparator.comparing(User::getScore);
        users.sort(byScore);
    }

    public void printSortedUsers() {
        setUsers(User.getUsers());
        sortUsers();
        ArrayList<Integer> ranks = new ArrayList<>();
        int previousScore = 0;
        int rank = 1;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getScore() < previousScore) {
                rank++;
                previousScore = users.get(i).getScore();
            }
            ranks.add(i, rank);
        }
        scoreboardView.showScoreboard(users, ranks);
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void toUpperMenu() {
        ProgramController.getInstance().setCurrentMenu(Menu.MAIN_MENU);
    }

    public void toLowerMenu(String menuName) {
        try {
            throw new MenuNavigationNotPossibleException();
        } catch (MenuNavigationNotPossibleException e) {
            System.err.println(e.getMessage());
        }
    }
}