package Menu;

import Database.User;
import ProgramController.Menu;
import ProgramController.Regex;

import java.util.regex.Matcher;

import static ProgramController.ProgramController.*;

public class MainMenu {
    User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void run(String command) {
        Matcher matcher;
        if (Regex.getCommandMatcher(command, Regex.logout).matches())
            logoutUser();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.menuNavigation)).matches())
            toLowerMenu(matcher);
        else if ((Regex.getCommandMatcher(command, Regex.exitMenu)).matches())
            toUpperMenu();
        else if (Regex.getCommandMatcher(command,Regex.showCurrentMenu).matches())
            System.out.println("Main Menu");
    }

    private void toUpperMenu() {
        switch (currentMenu) {
            case LOGIN_MENU -> currentMenu = Menu.EXIT;
            case MAIN_MENU -> {
                currentMenu = Menu.LOGIN_MENU;
                this.setCurrentUser(null);
            }
            default -> currentMenu = Menu.MAIN_MENU;
        }
    }

    private void toLowerMenu(Matcher matcher) {
        String menuName = matcher.group("menu_name");
        switch (menuName) {
            case "Duel" -> currentMenu = Menu.DuelMenu;
            case "Deck" -> {
                deckMenu.setCurrentUser(this.currentUser);
                currentMenu = Menu.DECK_MENU;
            }
            case "Scoreboard" -> {

                currentMenu = Menu.Scoreboard_Menu;
            }
            case "Profile" -> {
                profileMenu.setCurrentUser(this.currentUser);
                currentMenu = Menu.PROFILE_MENU;
            }
            case "Shop" -> {
                currentMenu = Menu.SHOP_MENU;
            }
            case "Import/Export" -> currentMenu = Menu.IMPORT_EXPORT_MENU;
        }
    }

    private void logoutUser() {
        this.setCurrentUser(null);
        currentMenu = Menu.LOGIN_MENU;
        //TODO missing success message
    }


}
