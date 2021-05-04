package View.Menu;

import Controller.MenuController.ScoreboardController;

public class Scoreboard {

    public void run(){
        ScoreboardController.getInstance().printSortedUsers();
    }
}
