package main.java.View.Menu;

import main.java.Controller.MenuController.MenuNavigationController;
import main.java.Database.User;
import main.java.Controller.ProgramController.Regex;
import main.java.Controller.ProgramController.ProgramController;

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