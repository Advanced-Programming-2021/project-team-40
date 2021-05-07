package View.Menu;


import Controller.MenuController.MenuNavigationController;
import Controller.ProgramController.ProgramController;
import Controller.ProgramController.Regex;
import Database.User;

public class MainMenu implements Help{

    private static MainMenu mainMenu;
    public User currentUser;

    private MainMenu(){

    }

    public static MainMenu getInstance(){
        if (mainMenu == null) mainMenu = new MainMenu();
        return mainMenu;
    }

    public void run(String command) {
        if (Regex.getCommandMatcher(command,Regex.help).matches()) help();
        else if (Regex.getCommandMatcher(command, Regex.logout).matches()) {
            ProgramController.getInstance().setCurrentMenu(MenuNavigationController.getInstance().logout());
            System.out.println("logout successful");
        } else System.err.println("invalid command");
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