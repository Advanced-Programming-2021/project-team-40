package ProgramController;

import java.util.Scanner;

public class ProgramController {
    /*TODO
       instantiate menus
       initialize Database
     */
    public static Menu currentMenu = Menu.LOGIN_MENU;
    public Scanner sc = new Scanner(System.in);

    public void run() {
        while (currentMenu != Menu.EXIT) {
            switch (currentMenu) {
                case LOGIN_MENU:
                    break;
                case MAIN_MENU:
                    break;
                case DECK_MENU:
                    break;
                case SHOP_MENU:
                    break;
                case PROFILE_MENU:
                    break;
                case IMPORT_EXPORT_MENU:
                    break;
            }
        }
    }

    public void initializeDatabase() {

    }
}
