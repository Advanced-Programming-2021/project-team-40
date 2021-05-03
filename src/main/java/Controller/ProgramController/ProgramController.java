package main.java.Controller.ProgramController;

import main.java.Controller.DuelController.GameplayController;
import main.java.View.Menu.LoginMenu;
import main.java.View.Menu.MainMenu;
import main.java.View.Menu.DeckMenu;
import main.java.View.Menu.ProfileMenu;
import main.java.View.Menu.ShopMenu;
import main.java.View.Menu.Scoreboard;
import main.java.View.Menu.ImportExport;

import java.util.Scanner;
import java.util.regex.Matcher;

import main.java.Controller.MenuController.MenuNavigationController;
import main.java.View.Exceptions.MenuNavigationNotPossibleException;


public class ProgramController {
    //TODO initialize main.java.Database
    private static Menu currentMenu = Menu.LOGIN_MENU;
    public static LoginMenu loginMenu = new LoginMenu();
    public static MainMenu mainMenu = new MainMenu();
    public static DeckMenu deckMenu = new DeckMenu();
    public static ProfileMenu profileMenu = new ProfileMenu();
    public static ShopMenu shopMenu = new ShopMenu();
    public static Scoreboard scoreboard = new Scoreboard();
    public static ImportExport importExport = new ImportExport();
    public Scanner sc = new Scanner(System.in);

    public static void setCurrentMenu(Menu currentMenu) {
        ProgramController.currentMenu = currentMenu;
    }

    public void run() {
        initializeDatabase();
        while (currentMenu != Menu.EXIT) {
            String command = sc.nextLine();
            Matcher matcher;
            if ((matcher = Regex.getCommandMatcher(command, Regex.menuNavigation)).matches()) {
                try {
                    currentMenu = MenuNavigationController.getInstance().toLowerMenu(matcher, currentMenu);
                } catch (MenuNavigationNotPossibleException e) {
                    System.out.println(e.getMessage());
                }
            } else if (Regex.getCommandMatcher(command, Regex.exitMenu).matches())
                currentMenu = MenuNavigationController.getInstance().toUpperMenu(currentMenu);
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
                        importExport.run();
                        break;
                    case DUEL_MENU:

                        break;
                    case SCOREBOARD_MENU:
                        scoreboard.run();
                        break;
                    case GAMEPLAY:
                        GameplayController.getInstance().run(command);
                }
        }
    }

    public void initializeDatabase() {

    }
}
