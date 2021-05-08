package Controller.MenuController;

import Controller.ProgramController.Menu;
import Controller.ProgramController.ProgramController;
import View.Exceptions.MenuNavigationNotPossibleException;

public class ImportExportController implements MenuNavigation {

    public void importCard(){

    }
    public void exportCard(){

    }

    public void toUpperMenu() {
        ProgramController.getInstance().setCurrentMenu(Menu.MAIN_MENU);
    }

    public void toLowerMenu(String menuName) {
        try {
            throw new MenuNavigationNotPossibleException();
        } catch (MenuNavigationNotPossibleException e) {
            System.err.println(e.getMessage());
        }
    }
}