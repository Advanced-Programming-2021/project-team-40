package View.Menu;


import Controller.MenuController.MainMenuController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.Regex;
import Database.User;

import java.util.regex.Matcher;

public class MainMenu implements Help {

    private static MainMenu mainMenu;
    public User currentUser;

    private MainMenu() {

    }

    public static MainMenu getInstance() {
        if (mainMenu == null) mainMenu = new MainMenu();
        return mainMenu;
    }

    public void run(String command) {
        Matcher matcher;
        if (Regex.getCommandMatcher(command, Regex.help).matches()) help();
        else if (command.matches(Regex.showCurrentMenu)) System.out.println(Menu.MAIN_MENU.toString());
        else if (command.matches(Regex.exitMenu)) MainMenuController.getInstance().toUpperMenu();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.menuNavigation)).matches()) goToMenu(matcher);
        else if (Regex.getCommandMatcher(command, Regex.logout).matches()) logout();
        else System.out.println("invalid command");
    }

    private void logout() {
        MainMenuController.getInstance().toUpperMenu();
        System.out.println("logout successful");
    }

    private void goToMenu(Matcher matcher) {
        String menuName = matcher.group(matcher.group("menuName"));
        MainMenuController.getInstance().toLowerMenu(menuName);
    }

    public void help() {
        System.out.println("menu enter [Duel | Deck | ScoreboardMenu | Profile | Shop | Import/Export]");
        System.out.println("menu exit");
        System.out.println("menu show-current");
        System.out.println("logout");
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

}