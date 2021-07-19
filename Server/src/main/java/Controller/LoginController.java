package Controller;

import Controller.DatabaseController.DatabaseController;
import Controller.Exceptions.*;
import Database.Cards.Card;
import Database.Deck;
import Database.User;
import java.util.ArrayList;
import java.util.UUID;


public class LoginController {

    public static String registerUser(String username, String password, String nickname) throws RepetitiveUsernameException, RepetitiveNicknameException, WeakPasswordException {
        if (passwordIsWeak(password)) throw new WeakPasswordException();
        if (User.getUserByName(username) != null) throw new RepetitiveUsernameException(username);
        if (User.getUserByNickname(nickname) != null) throw new RepetitiveNicknameException(nickname);
        User tempUser = new User(username, password, nickname, "1", 0, 110000, new ArrayList<Deck>(), new ArrayList<Card>());
        DatabaseController.getInstance().saveUser(tempUser);
        return UUID.randomUUID().toString();
    }

    private static boolean passwordIsWeak(String password) {
        return !password.matches(Regex.strongPassword);
    }
    public static String loginUser(String username, String password) throws InvalidLoginException {
        User currentUser;
        if ((currentUser = User.getUserByName(username)) == null) throw new InvalidLoginException();
        if (!currentUser.getPassword().equals(password)) throw new InvalidLoginException();
        return UUID.randomUUID().toString();
    }
}
