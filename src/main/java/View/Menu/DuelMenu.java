package View.Menu;



import Controller.MenuController.DuelMenuController;
import Controller.MenuController.MenuNavigationController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.Regex;
import Database.User;

import java.util.regex.Matcher;

public class DuelMenu implements Help {
    User currentUser;

    private static DuelMenu duelMenu;
    private DuelMenu(){

    }

    public static DuelMenu getInstance(){
        if (duelMenu == null) duelMenu = new DuelMenu();
        return duelMenu;
    }

    public void run(String command) {
        Matcher matcher;
        if (Regex.getCommandMatcher(command,Regex.help).matches()) help();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.startPlayerDuel)).matches()) startPlayerGame(matcher);
        else if ((matcher = Regex.getCommandMatcher(command,Regex.startAIDuel)).matches()) startAIGame(matcher);
        else if (Regex.getCommandMatcher(command, Regex.exitMenu).matches())
            MenuNavigationController.getInstance().toUpperMenu(Menu.DUEL_MENU);
        else System.err.println("invalid command");
    }

    public void help() {
        System.out.println("duel --new --second-player <player2 username> --rounds <1/3>");
        System.out.println("duel --new --ai --rounds <1/3>");
        System.out.println("menu show-current");
        System.out.println("help");
        System.out.println("menu exit");
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
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
