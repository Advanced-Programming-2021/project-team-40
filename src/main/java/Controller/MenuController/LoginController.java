package Controller.MenuController;

import Controller.DatabaseController.DatabaseController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.Regex;
import Database.Cards.Card;
import Database.User;
import Database.Deck;
import View.Exceptions.*;

import java.util.ArrayList;

import Controller.ProgramController.ProgramController;

public class LoginController implements MenuNavigation {
    private static LoginController loginController;

    private LoginController() {

    }

    public static LoginController getInstance() {
        if (loginController == null)
            loginController = new LoginController();
        return loginController;
    }

    public void loginUser(String username,String password) throws InvalidLoginException {
        User currentUser;
        if ((currentUser = User.getUserByName(username)) == null) throw new InvalidLoginException();
        if (!currentUser.getPassword().equals(password)) throw new InvalidLoginException();
        ProgramController.getInstance().setCurrentMenu(Menu.MAIN_MENU);
        ProgramController.mainMenu.setCurrentUser(currentUser);
    }

    public boolean passwordIsWeak(String password){
        return !password.matches(Regex.strongPassword);
    }

    public void registerUser(String username,String password,String nickname) throws RepetitiveUsernameException, RepetitiveNicknameException, WeakPasswordException {
        if (passwordIsWeak(password)) throw new WeakPasswordException();
        if (User.getUserByName(username) != null) throw new RepetitiveUsernameException(username);
        if (User.getUserByNickname(nickname) != null) throw new RepetitiveNicknameException(nickname);
        User tempUser = new User(username, password, nickname, 0, 10000, new ArrayList<Deck>(), new ArrayList<Card>());
        DatabaseController.getInstance().saveUser(tempUser);
    }

    public void toUpperMenu() {
        ProgramController.getInstance().setCurrentMenu(Menu.EXIT);
    }

    public void toLowerMenu(String menuName) {
        try {
            throw new MenuNavigationNotPossibleException();
        } catch (MenuNavigationNotPossibleException e) {
            System.err.println(e.getMessage());
        }
    }
}