package Controller.MenuController;

import Controller.ProgramController.Menu;
import Controller.ProgramController.ProgramController;
import View.Exceptions.InvalidMenuNameException;
import View.Exceptions.MenuNavigationNotPossibleException;

import static Controller.ProgramController.ProgramController.*;

public class MainMenuController implements MenuNavigation {
    private static MainMenuController mainMenuController;

    private MainMenuController() {

    }

    public static MainMenuController getInstance() {
        if (mainMenuController == null)
            mainMenuController = new MainMenuController();
        return mainMenuController;
    }

    public void resetMenusForLogout() {
        deckMenu.setCurrentUser(null);
        duelMenu.setCurrentUser(null);
        profileMenu.setCurrentUser(null);
        shopMenu.setCurrentUser(null);
        mainMenu.setCurrentUser(null);
    }

    public void toUpperMenu() {
        resetMenusForLogout();
        ProgramController.getInstance().setCurrentMenu(Menu.LOGIN_MENU);
    }

    public void toLowerMenu(String menuName) throws InvalidMenuNameException {
        Menu menu = Menu.MAIN_MENU;
        switch (menuName) {
            case "Duel":
                duelMenu.setCurrentUser(mainMenu.currentUser);
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
            default:
                throw new InvalidMenuNameException();
        }
        ProgramController.getInstance().setCurrentMenu(menu);
    }
}
