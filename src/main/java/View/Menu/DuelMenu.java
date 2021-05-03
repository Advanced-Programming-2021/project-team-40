package main.java.View.Menu;

import main.java.Controller.MenuController.DuelMenuController;
import main.java.Controller.ProgramController.Regex;
import main.java.Database.User;
import main.java.View.Exceptions.*;

import java.util.regex.Matcher;

public class DuelMenu {
    User currentUser;

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.startPlayerDuel)).matches()) {
            try {
                DuelMenuController.getInstance().startPlayerGame(matcher, currentUser);
            } catch (UserNotFoundException | ActiveDeckNotFoundException | InvalidRoundNumberException | InvalidDeckException e) {
                System.out.println(e.getMessage());
            }
        }
        if (Regex.getCommandMatcher(command,Regex.startAIDuel).matches()) {
            DuelMenuController.getInstance().startAIGame();
        }
        else System.out.println("invalid command");
    }
}
