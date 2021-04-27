package ProgramController;
import Menu.LoginMenu;
import Menu.MainMenu;
import Menu.DeckMenu;
import Menu.ProfileMenu;
import Menu.ShopMenu;
import java.util.Scanner;

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
    public Scanner sc = new Scanner(System.in);
    public void run() {
        while (currentMenu != Menu.EXIT) {
            String command = sc.nextLine();
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
                    break;
            }
        }
    }

    public void initializeDatabase() {

    }
}
