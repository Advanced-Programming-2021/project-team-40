package MenuController;

import Database.User;
import Exceptions.MenuNavigationNotPossibleException;
import MenuController.ProgramController.Menu;

import java.util.regex.Matcher;

import static MenuController.ProgramController.ProgramController.*;

public class MenuNavigationController {

    public Menu toUpperMenu(Menu menu) {
        switch (menu) {
            case LOGIN_MENU:
                menu = Menu.EXIT;
                resetMenusForLogout();
                break;
            case MAIN_MENU: {
                menu = Menu.LOGIN_MENU;
                mainMenu.setCurrentUser(null);
                break;
            }
            default:
                menu = Menu.MAIN_MENU;
        }
        return menu;
    }

    public Menu toLowerMenu(Matcher matcher, Menu menu) throws MenuNavigationNotPossibleException {
        String menuName = matcher.group("menu_name");
        if (menu == Menu.MAIN_MENU)
            switch (menuName) {
                case "Duel":
                    menu = Menu.DUEL_MENU;
                    break;
                case "Deck":
                    deckMenu.setCurrentUser(mainMenu.currentUser);
                    menu = Menu.DECK_MENU;
                    break;
                case "Scoreboard":

                    menu = Menu.SCOREBOARD_MENU;
                    break;
                case "Profile":
                    profileMenu.setCurrentUser(mainMenu.currentUser);
                    menu = Menu.PROFILE_MENU;
                    break;
                case "Shop":
                    shopMenu.setCurrentUser(mainMenu.currentUser);
                    menu = Menu.SHOP_MENU;
                    break;
                case "Import/Export":
                    menu = Menu.IMPORT_EXPORT_MENU;
                    break;
            }
        else throw new MenuNavigationNotPossibleException();
        return menu;
    }

    public void resetMenusForLogout() {
        deckMenu.setCurrentUser(null);
        profileMenu.setCurrentUser(null);
        shopMenu.setCurrentUser(null);
        //TODO complete list
    }

    public void login(User currentUser) {
        currentMenu = Menu.MAIN_MENU;
        mainMenu.setCurrentUser(currentUser);
    }

    public void logout() {
        mainMenu.setCurrentUser(null);
        currentMenu = Menu.LOGIN_MENU;
    }
}
