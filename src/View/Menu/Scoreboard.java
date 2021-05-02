package View.Menu;

import Controller.MenuController.ScoreboardController;

public class Scoreboard {
    private ScoreboardController scoreboardController = new ScoreboardController();
    public void run(){
        scoreboardController.printSortedUsers();
    }
}
