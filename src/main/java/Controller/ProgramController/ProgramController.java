package Controller.ProgramController;

import Controller.DatabaseController.DatabaseController;
import View.GameplayView;
import View.Menu.*;

import java.util.Scanner;



public class ProgramController {
    private static ProgramController programController;
    private static Menu currentMenu = Menu.LOGIN_MENU;
    public static LoginMenu loginMenu = LoginMenu.getInstance();
    public static MainMenu mainMenu = MainMenu.getInstance();
    public static DeckMenu deckMenu = DeckMenu.getInstance();
    public static ProfileMenu profileMenu = ProfileMenu.getInstance();
    public static ShopMenu shopMenu = ShopMenu.getInstance();
    public static DuelMenu duelMenu = DuelMenu.getInstance();
    public static ScoreboardMenu scoreboardMenu = new ScoreboardMenu();
    public static ImportExport importExport = ImportExport.getInstance();
    public Scanner scanner = new Scanner(System.in);

    private ProgramController(){

    }

    public static ProgramController getInstance(){
        if (programController == null) programController = new ProgramController();
        return programController;
    }

    public void run() {
        DatabaseController.getInstance();
        while (currentMenu != Menu.EXIT) {
            System.out.flush();
            System.out.flush();
            String command = scanner.nextLine();
            switch (currentMenu) {
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
                        scoreboardMenu.run(command);
                        break;
                    case GAMEPLAY:
                        GameplayView.getInstance().run(command);
                }
        }
    }

    public void setCurrentMenu(Menu currentMenu) {
        ProgramController.currentMenu = currentMenu;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public static Menu getCurrentMenu() {
        return currentMenu;
    }
}
