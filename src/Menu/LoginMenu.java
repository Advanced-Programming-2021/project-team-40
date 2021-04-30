package Menu;

import Database.User;
import ProgramController.Regex;
import Exceptions.InvalidLoginException;
import Exceptions.RepetitiveNicknameException;
import Exceptions.RepetitiveUsernameException;

import java.util.regex.Matcher;

public class LoginMenu {
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.createUser)).matches()) {
            try {
                registerUser(matcher);
            } catch (RepetitiveUsernameException | RepetitiveNicknameException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.login)).matches()) {
            try {
                loginUser(matcher);
            } catch (InvalidLoginException e) {
                System.out.println(e.getMessage());
            }
        }
        else System.out.println("invalid command");
    }

    private static void loginUser(Matcher matcher) throws InvalidLoginException {
        String username = matcher.group("username");
        String password = matcher.group("nickname");
        if (User.getUserByName(username) == null) throw new InvalidLoginException();
        User currentUser = User.getUserByName(username);
        if (!currentUser.getPassword().equals(password)) throw new InvalidLoginException();
        MenuNavigationController.login(currentUser);
        //TODO success message missing
    }

    private static void registerUser(Matcher matcher) throws RepetitiveUsernameException, RepetitiveNicknameException {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String nickname = matcher.group("nickname");
        if (User.getUserByName(username) != null) throw new RepetitiveUsernameException(username);
        if (User.getUserByNickname(nickname) != null) throw new RepetitiveNicknameException(nickname);
        new User(username, password, nickname);
        //TODO success message missing
    }
}
