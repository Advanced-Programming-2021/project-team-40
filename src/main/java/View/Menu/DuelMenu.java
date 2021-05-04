package View.Menu;



import Controller.MenuController.DuelMenuController;
import Controller.ProgramController.Regex;
import Database.User;
import View.Exceptions.ActiveDeckNotFoundException;
import View.Exceptions.InvalidDeckException;
import View.Exceptions.InvalidRoundNumberException;
import View.Exceptions.UserNotFoundException;

import java.util.regex.Matcher;

public class DuelMenu {
    User currentUser;

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.startPlayerDuel)).matches()) startPlayerGame(matcher);
        if ((matcher = Regex.getCommandMatcher(command,Regex.startAIDuel)).matches()) {
            startAIGame(matcher);
        }
        else System.out.println("invalid command");
    }

    private void startAIGame(Matcher matcher) {
        String roundCount = matcher.group("rounds");
        DuelMenuController.getInstance().startAIGame(roundCount);
    }

    private void startPlayerGame(Matcher matcher) {
        String userTwo = matcher.group("username");
        String roundCount = matcher.group("rounds");
        try {
            DuelMenuController.getInstance().startPlayerGame(userTwo,roundCount,currentUser);
        } catch (UserNotFoundException | InvalidRoundNumberException | ActiveDeckNotFoundException | InvalidDeckException e) {
            System.err.println(e.getMessage());
        }
    }
}
