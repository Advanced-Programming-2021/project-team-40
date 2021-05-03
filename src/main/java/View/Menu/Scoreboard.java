package main.java.View.Menu;

import main.java.Controller.MenuController.ScoreboardController;

public class Scoreboard {

    public void run(){
        ScoreboardController.getInstance().printSortedUsers();
    }
}
