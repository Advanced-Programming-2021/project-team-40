package View.Menu;



import Controller.MenuController.DuelMenuController;
import Controller.ProgramController.Regex;
import Database.User;
import View.Exceptions.ActiveDeckNotFoundException;
import View.Exceptions.InvalidDeckException;
import View.Exceptions.InvalidRoundNumberException;
import View.Exceptions.UserNotFoundException;

import java.util.regex.Matcher;

public class DuelMenu implements Help {
    User currentUser;

    public void run(String command) {
        Matcher matcher;
        if (Regex.getCommandMatcher(command,Regex.help).matches()) help();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.startPlayerDuel)).matches()) startPlayerGame(matcher);
        else if ((matcher = Regex.getCommandMatcher(command,Regex.startAIDuel)).matches()) startAIGame(matcher);
        else System.err.println("invalid command");
    }

    public void help() {
        System.out.println("menu exit");
        System.out.println("menu show-current");
        System.out.println("duel --new --second-player <player2 username> --rounds <1/3>");
        System.out.println("duel --new --ai --rounds <1/3>");
    }

    private void startAIGame(Matcher matcher) {
        String roundCount = matcher.group("rounds");
        DuelMenuController.getInstance().startAIGame(roundCount);
        System.out.println("AI game started for" + currentUser.getUsername());
    }

    private void startPlayerGame(Matcher matcher) {
        String userTwo = matcher.group("username");
        String roundCount = matcher.group("rounds");
        try {
            DuelMenuController.getInstance().startPlayerGame(userTwo,roundCount,currentUser);
            System.out.println("game started between" + currentUser.getUsername() + "and" + User.getUserByName(userTwo).getUsername() + " !");
        } catch (UserNotFoundException | InvalidRoundNumberException | ActiveDeckNotFoundException | InvalidDeckException e) {
            System.err.println(e.getMessage());
        }
    }
}
