package Controller.MenuController;

import Controller.ProgramController.ProgramController;
import Database.User;
import View.Exceptions.MenuNavigationNotPossibleException;
import Controller.ProgramController.Menu;

import java.util.regex.Matcher;

import static Controller.ProgramController.ProgramController.*;

public class MenuNavigationController {
    private static MenuNavigationController menuNavigator;
    private MenuNavigationController() {

    }

    public static MenuNavigationController getInstance() {
        if (menuNavigator == null)
            menuNavigator = new MenuNavigationController();
        return menuNavigator;
    }

    public void toUpperMenu(Menu menu) {
        switch (menu) {
            case LOGIN_MENU -> menu = Menu.EXIT;
            case MAIN_MENU -> {
                menu = Menu.LOGIN_MENU;
                mainMenu.setCurrentUser(null);
                resetMenusForLogout();
            }
            default -> menu = Menu.MAIN_MENU;
        }
        ProgramController.getInstance().setCurrentMenu(menu);
    }

    public Menu toLowerMenu(Matcher matcher, Menu menu) throws MenuNavigationNotPossibleException {
        String menuName = matcher.group("menuName");
        if (menu == Menu.MAIN_MENU)
            switch (menuName) {
                case "Duel" -> {
                    duelMenu.setCurrentUser(mainMenu.currentUser);
                    menu = Menu.DUEL_MENU;
                }
                case "Deck" -> {
                    deckMenu.setCurrentUser(mainMenu.currentUser);
                    menu = Menu.DECK_MENU;
                }
                case "ScoreboardMenu" -> menu = Menu.SCOREBOARD_MENU;
                case "Profile" -> {
                    profileMenu.setCurrentUser(mainMenu.currentUser);
                    menu = Menu.PROFILE_MENU;
                }
                case "Shop" -> {
                    shopMenu.setCurrentUser(mainMenu.currentUser);
                    menu = Menu.SHOP_MENU;
                }
                case "Import/Export" -> menu = Menu.IMPORT_EXPORT_MENU;
            }
        else throw new MenuNavigationNotPossibleException();
        return menu;
    }

    public void resetMenusForLogout() {
        deckMenu.setCurrentUser(null);
        profileMenu.setCurrentUser(null);
        shopMenu.setCurrentUser(null);
        mainMenu.setCurrentUser(null);
        //TODO complete list and add to login as well
    }

    public Menu login(User currentUser) {
        mainMenu.setCurrentUser(currentUser);
        return Menu.MAIN_MENU;
    }

    public Menu logout() {
        resetMenusForLogout();
        return Menu.LOGIN_MENU;
    }
}