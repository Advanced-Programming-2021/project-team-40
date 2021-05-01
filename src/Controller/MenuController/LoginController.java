package Controller.MenuController;

import Database.User;
import View.Exceptions.InvalidLoginException;
import View.Exceptions.RepetitiveNicknameException;
import View.Exceptions.RepetitiveUsernameException;

import java.util.regex.Matcher;

import Controller.ProgramController.ProgramController;

public class LoginController {
    public static void loginUser(Matcher matcher) throws InvalidLoginException {
        String username = matcher.group("username");
        String password = matcher.group("nickname");
        User currentUser;
        if ((currentUser = User.getUserByName(username)) == null) throw new InvalidLoginException();
        if (!currentUser.getPassword().equals(password)) throw new InvalidLoginException();
        ProgramController.setCurrentMenu(ProgramController.menuNavigator.login(currentUser));
    }

    public static void registerUser(Matcher matcher) throws RepetitiveUsernameException, RepetitiveNicknameException {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String nickname = matcher.group("nickname");
        if (User.getUserByName(username) != null) throw new RepetitiveUsernameException(username);
        if (User.getUserByNickname(nickname) != null) throw new RepetitiveNicknameException(nickname);
        new User(username, password, nickname);
    }
}