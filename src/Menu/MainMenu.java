package Menu;

import Database.User;
import ProgramController.Menu;
import ProgramController.Regex;

import java.util.regex.Matcher;

import static Menu.MenuNavigationController.toLowerMenu;
import static Menu.MenuNavigationController.toUpperMenu;
import static ProgramController.ProgramController.*;

public class MainMenu {
    User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void run(String command) {
        if (Regex.getCommandMatcher(command, Regex.logout).matches())
            logoutUser();
        else System.out.println("invalid command");
    }

    private void logoutUser() {
        MenuNavigationController.logout();
        //TODO missing success message
    }


}
