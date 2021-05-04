package View.Menu;


import Controller.MenuController.MenuNavigationController;
import Controller.ProgramController.ProgramController;
import Controller.ProgramController.Regex;
import Database.User;

public class MainMenu {
    public User currentUser;

    public void run(String command) {
        if (Regex.getCommandMatcher(command,Regex.help).matches()) help();
        else if (Regex.getCommandMatcher(command, Regex.logout).matches()) {
            ProgramController.setCurrentMenu(MenuNavigationController.getInstance().logout());
            System.out.println("logout successful");
        } else System.err.println("invalid command");
    }

    private void help() {
        System.out.println("menu enter <menu name>");
        System.out.println("menu exit");
        System.out.println("menu show-current");
        System.out.println("logout");
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

}