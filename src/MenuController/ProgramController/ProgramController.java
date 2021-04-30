package MenuController.ProgramController;

import Menu.LoginMenu;
import Menu.MainMenu;
import Menu.DeckMenu;
import Menu.ProfileMenu;
import Menu.ShopMenu;

import java.util.Scanner;
import java.util.regex.Matcher;

import MenuController.MenuNavigationController;
import Exceptions.MenuNavigationNotPossibleException;


public class ProgramController {
    /*TODO
       instantiate menus
       initialize Database
     */
    public static Menu currentMenu = Menu.LOGIN_MENU;
    public static LoginMenu loginMenu = new LoginMenu();
    public static MainMenu mainMenu = new MainMenu();
    public static DeckMenu deckMenu = new DeckMenu();
    public static ProfileMenu profileMenu = new ProfileMenu();
    public static ShopMenu shopMenu = new ShopMenu();
    public static MenuNavigationController menuNavigator = new MenuNavigationController();
    public Scanner sc = new Scanner(System.in);

    public void run() {
        while (currentMenu != Menu.EXIT) {
            String command = sc.nextLine();
            Matcher matcher;
            if ((matcher = Regex.getCommandMatcher(command, Regex.menuNavigation)).matches()) {
                try {
                    currentMenu = menuNavigator.toLowerMenu(matcher,currentMenu);
                } catch (MenuNavigationNotPossibleException e) {
                    System.out.println(e.getMessage());
                }
            }
            else if (Regex.getCommandMatcher(command, Regex.exitMenu).matches())
                currentMenu = menuNavigator.toUpperMenu(currentMenu);
            else switch (currentMenu) {
                case LOGIN_MENU:
                    loginMenu.run(command);
                    break;
                case MAIN_MENU:
                    mainMenu.run(command);
                    break;
                case DECK_MENU:
                    deckMenu.run(command);
                    break;
                case SHOP_MENU:
                    shopMenu.run(command);
                    break;
                case PROFILE_MENU:
                    profileMenu.run(command);
                    break;
                case IMPORT_EXPORT_MENU:
                    break;
            }
        }
    }

    public void initializeDatabase() {

    }
}
