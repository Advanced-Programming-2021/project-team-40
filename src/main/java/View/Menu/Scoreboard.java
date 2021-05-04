package View.Menu;

import Controller.MenuController.ScoreboardController;
import Controller.ProgramController.Regex;

public class Scoreboard {

    public void run(String command){
        if (Regex.getCommandMatcher(command,Regex.help).matches()) help();
        else if (Regex.getCommandMatcher(command,Regex.showScoreboard).matches())
            ScoreboardController.getInstance().printSortedUsers();
        else System.err.println("invalid command");
    }

    private void help() {
        System.out.println("menu exit");
        System.out.println("menu show-current");
        System.out.println("show scoreboard");
    }
}
