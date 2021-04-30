package Menu;

import Database.User;
import MenuController.ProgramController.Regex;
import MenuController.ProgramController.ProgramController;

public class MainMenu {
    public User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void run(String command) {
        if (Regex.getCommandMatcher(command, Regex.logout).matches()) {
            ProgramController.menuNavigator.logout();
            System.out.println("logout successful");
        } else System.out.println("invalid command");
    }
}
