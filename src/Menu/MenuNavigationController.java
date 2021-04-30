package Menu;

import Database.User;
import Exceptions.MenuNavigationNotPossibleException;
import ProgramController.Menu;

import java.util.regex.Matcher;

import static ProgramController.ProgramController.*;

public class MenuNavigationController {
    public static void toUpperMenu() {
        switch (currentMenu) {
            case LOGIN_MENU:
                currentMenu = Menu.EXIT;
                resetMenusForLogout();
            case MAIN_MENU: {
                currentMenu = Menu.LOGIN_MENU;
                mainMenu.setCurrentUser(null);
            }
            default:
                currentMenu = Menu.MAIN_MENU;
        }
    }

    public static void toLowerMenu(Matcher matcher) throws MenuNavigationNotPossibleException {
        String menuName = matcher.group("menu_name");
        if (currentMenu == Menu.MAIN_MENU)
            switch (menuName) {
                case "Duel":
                    currentMenu = Menu.DUEL_MENU;
                case "Deck":
                    deckMenu.setCurrentUser(mainMenu.currentUser);
                    currentMenu = Menu.DECK_MENU;
                case "Scoreboard":

                    currentMenu = Menu.SCOREBOARD_MENU;
                case "Profile":
                    profileMenu.setCurrentUser(mainMenu.currentUser);
                    currentMenu = Menu.PROFILE_MENU;
                case "Shop":
                    shopMenu.setCurrentUser(mainMenu.currentUser);
                    currentMenu = Menu.SHOP_MENU;
                case "Import/Export":
                    currentMenu = Menu.IMPORT_EXPORT_MENU;
            }
        else throw new MenuNavigationNotPossibleException();
    }

    public static void resetMenusForLogout() {
        deckMenu.setCurrentUser(null);
        profileMenu.setCurrentUser(null);
        shopMenu.setCurrentUser(null);
        //TODO complete list
    }

    public static void login(User currentUser) {
        currentMenu = Menu.MAIN_MENU;
        mainMenu.setCurrentUser(currentUser);
    }
    public static void logout(){
        mainMenu.setCurrentUser(null);
        currentMenu = Menu.LOGIN_MENU;
    }
}
