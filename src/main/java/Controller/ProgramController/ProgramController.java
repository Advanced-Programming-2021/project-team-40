package Controller.ProgramController;

import Controller.DatabaseController.DatabaseController;
import View.GameplayView;
import View.Menu.*;

import java.util.Scanner;
import java.util.regex.Matcher;

import Controller.MenuController.MenuNavigationController;
import View.Exceptions.MenuNavigationNotPossibleException;


public class ProgramController {
    private static Menu currentMenu = Menu.LOGIN_MENU;
    public static LoginMenu loginMenu = new LoginMenu();
    public static MainMenu mainMenu = new MainMenu();
    public static DeckMenu deckMenu = new DeckMenu();
    public static ProfileMenu profileMenu = new ProfileMenu();
    public static ShopMenu shopMenu = ShopMenu.getInstance();
    public static DuelMenu duelMenu = DuelMenu.getInstance();
    public static Scoreboard scoreboard = new Scoreboard();
    public static ImportExport importExport = new ImportExport();
    public Scanner sc = new Scanner(System.in);

    public static void setCurrentMenu(Menu currentMenu) {
        ProgramController.currentMenu = currentMenu;
    }

    public void run() {
        DatabaseController.getInstance();
        while (currentMenu != Menu.EXIT) {
            System.out.flush();
            System.err.flush();
            String command = sc.nextLine();
            Matcher matcher;
            if (command.matches(Regex.showCurrentMenu)) System.out.println(currentMenu.toString());
            else if ((matcher = Regex.getCommandMatcher(command, Regex.menuNavigation)).matches()) {
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
                        importExport.run(command);
                        break;
                    case DUEL_MENU:
                        duelMenu.run(command);
                        break;
                    case SCOREBOARD_MENU:
                        scoreboard.run(command);
                        break;
                    case GAMEPLAY:
                        GameplayView.getInstance().run(command);
                }
        }
    }

    public static Menu getCurrentMenu() {
        return currentMenu;
    }
}
