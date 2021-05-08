package View.Menu;

import Controller.MenuController.ScoreboardController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.Regex;

public class ScoreboardMenu implements Help {

    public void run(String command) {
        if (Regex.getCommandMatcher(command, Regex.help).matches()) help();
        else if (command.matches(Regex.showCurrentMenu)) System.out.println(Menu.SCOREBOARD_MENU.toString());
        else if (command.matches(Regex.exitMenu)) ScoreboardController.getInstance().toUpperMenu();
        else if (command.matches(Regex.menuNavigation)) ScoreboardController.getInstance().toLowerMenu("");
        else if (Regex.getCommandMatcher(command, Regex.showScoreboard).matches())
            ScoreboardController.getInstance().printSortedUsers();
        else System.out.println("invalid command");
    }

    public void help() {
        System.out.println("show scoreboard");
        System.out.println("menu show-current");
        System.out.println("help");
        System.out.println("menu exit");
    }
}
