package Controller.MenuController;

import Controller.DatabaseController.DatabaseController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.ProgramController;
import Controller.ProgramController.Regex;
import Database.User;
import View.Exceptions.*;

public class ProfileMenuController implements MenuNavigation {
    private static ProfileMenuController profileMenuController;

    private ProfileMenuController() {
    }

    public static ProfileMenuController getInstance() {
        if (profileMenuController == null)
            profileMenuController = new ProfileMenuController();
        return profileMenuController;
    }

    public void changePassword(String currentPassword, String newPassword, User currentUser) throws RepetitivePasswordException, InvalidPasswordException, WeakPasswordException {
        if (!currentUser.getPassword().equals(currentPassword)) throw new InvalidPasswordException();
        if (currentPassword.equals(newPassword)) throw new RepetitivePasswordException();
        if (passwordIsWeak(newPassword)) throw new WeakPasswordException();
        currentUser.setPassword(newPassword);
        DatabaseController.getInstance().saveUser(currentUser);
    }

    private boolean passwordIsWeak(String newPassword) {
        return !newPassword.matches(Regex.strongPassword);
    }

    public void changeNickname(String newNickname, User currentUser) throws RepetitiveNicknameException {
        if (User.getUserByNickname(newNickname) != null) throw new RepetitiveNicknameException(newNickname);
        currentUser.setNickname(newNickname);
        DatabaseController.getInstance().saveUser(currentUser);
    }

    public void toUpperMenu() {
        ProgramController.getInstance().setCurrentMenu(Menu.MAIN_MENU);
    }

    public void toLowerMenu(String menuName) {
        try {
            throw new MenuNavigationNotPossibleException();
        } catch (MenuNavigationNotPossibleException e) {
            System.out.println(e.getMessage());
        }
    }
}