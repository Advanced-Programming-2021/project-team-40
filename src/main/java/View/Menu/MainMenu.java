package View.Menu;

import Controller.MenuController.MenuNavigationController;
import Database.User;
import Controller.ProgramController.Regex;
import Controller.ProgramController.ProgramController;

public class MainMenu {
    public User currentUser;

    public void run(String command) {
        if (Regex.getCommandMatcher(command, Regex.logout).matches()) {
            ProgramController.setCurrentMenu(MenuNavigationController.getInstance().logout());
            System.out.println("logout successful");
        } else System.out.println("invalid command");
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

}