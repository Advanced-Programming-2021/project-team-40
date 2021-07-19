package Controller.MenuController;

import View.Exceptions.InvalidMenuNameException;

public interface MenuNavigation {
    void toUpperMenu();

    void toLowerMenu(String menuName) throws InvalidMenuNameException;
}
