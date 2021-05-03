package main.java.Controller.MenuController;

import main.java.Controller.ProgramController.Regex;
import main.java.Database.User;
import main.java.View.Exceptions.*;

import java.util.regex.Matcher;

import main.java.Controller.ProgramController.ProgramController;

public class LoginController {
    private static LoginController loginController;

    private LoginController() {

    }

    public static LoginController getInstance() {
        if (loginController == null)
            loginController = new LoginController();
        return loginController;
    }

    public static void loginUser(Matcher matcher) throws InvalidLoginException {
        String username = matcher.group("username");
        String password = matcher.group("password");
        User currentUser;
        if ((currentUser = User.getUserByName(username)) == null) throw new InvalidLoginException();
        if (!currentUser.getPassword().equals(password)) throw new InvalidLoginException();
        ProgramController.setCurrentMenu(MenuNavigationController.getInstance().login(currentUser));
    }

    public static boolean passwordIsWeak(String password){
        if (password.matches(Regex.strongPassword)) return false;
        return true;
    }

    public static void registerUser(Matcher matcher) throws RepetitiveUsernameException, RepetitiveNicknameException, WeakPasswordException {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String nickname = matcher.group("nickname");
        if (passwordIsWeak(password)) throw new WeakPasswordException();
        if (User.getUserByName(username) != null) throw new RepetitiveUsernameException(username);
        if (User.getUserByNickname(nickname) != null) throw new RepetitiveNicknameException(nickname);
        new User(username, password, nickname);
    }
}