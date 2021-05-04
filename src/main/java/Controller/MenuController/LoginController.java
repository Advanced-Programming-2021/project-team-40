package Controller.MenuController;

import Controller.DatabaseController.DatabaseController;
import Controller.ProgramController.Regex;
import Database.Cards.Card;
import Database.User;
import Database.Deck;
import View.Exceptions.*;

import java.util.ArrayList;
import java.util.regex.Matcher;

import Controller.ProgramController.ProgramController;

public class LoginController {
    private static LoginController loginController;

    private LoginController() {

    }

    public static LoginController getInstance() {
        if (loginController == null)
            loginController = new LoginController();
        return loginController;
    }

    public static void loginUser(String username,String password) throws InvalidLoginException {
        User currentUser;
        if ((currentUser = User.getUserByName(username)) == null) throw new InvalidLoginException();
        if (!currentUser.getPassword().equals(password)) throw new InvalidLoginException();
        ProgramController.setCurrentMenu(MenuNavigationController.getInstance().login(currentUser));
    }

    public static boolean passwordIsWeak(String password){
        if (password.matches(Regex.strongPassword)) return false;
        return true;
    }

    public static void registerUser(String username,String password,String nickname) throws RepetitiveUsernameException, RepetitiveNicknameException, WeakPasswordException {
        System.out.println("Nickname: " + nickname + "\nPassword: " + password);
        if (passwordIsWeak(password)) throw new WeakPasswordException();
        if (User.getUserByName(username) != null) throw new RepetitiveUsernameException(username);
        if (User.getUserByNickname(nickname) != null) throw new RepetitiveNicknameException(nickname);
        User tempUser = new User(username, password, nickname, 0, 10000, new ArrayList<Deck>(), new Deck("Base deck"), new ArrayList<Card>());
        DatabaseController.getInstance().saveUser(tempUser);
    }
}