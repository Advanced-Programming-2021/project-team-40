package View.Menu;

import Controller.MenuController.MenuNavigationController;
import Controller.MenuController.ScoreboardController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.Regex;

public class ScoreboardMenu implements Help{

    public void run(String command){
        if (Regex.getCommandMatcher(command,Regex.help).matches()) help();
        else if (Regex.getCommandMatcher(command,Regex.showScoreboard).matches())
            ScoreboardController.getInstance().printSortedUsers();
        else if (Regex.getCommandMatcher(command, Regex.exitMenu).matches())
            MenuNavigationController.getInstance().toUpperMenu(Menu.SCOREBOARD_MENU);
        else System.err.println("invalid command");
    }

    public void help() {
        System.out.println("show scoreboard");
        System.out.println("menu show-current");
        System.out.println("help");
        System.out.println("menu exit");
    }
}
