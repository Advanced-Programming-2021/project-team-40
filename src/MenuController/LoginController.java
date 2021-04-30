package MenuController;

import Database.User;
import Exceptions.InvalidLoginException;
import Exceptions.RepetitiveNicknameException;
import Exceptions.RepetitiveUsernameException;

import java.util.regex.Matcher;

import MenuController.ProgramController.ProgramController;

public class LoginController {
    public static void loginUser(Matcher matcher) throws InvalidLoginException {
        String username = matcher.group("username");
        String password = matcher.group("nickname");
        if (User.getUserByName(username) == null) throw new InvalidLoginException();
        User currentUser = User.getUserByName(username);
        if (!currentUser.getPassword().equals(password)) throw new InvalidLoginException();
        ProgramController.menuNavigator.login(currentUser);
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
