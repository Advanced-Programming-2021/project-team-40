package View.Menu;



import Controller.MenuController.LoginController;
import Controller.ProgramController.Regex;
import View.Exceptions.InvalidLoginException;
import View.Exceptions.RepetitiveNicknameException;
import View.Exceptions.RepetitiveUsernameException;
import View.Exceptions.WeakPasswordException;

import java.util.regex.Matcher;

public class LoginMenu {
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.createUser)).matches()) registerUser(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.login)).matches()) loginUser(matcher);
        else System.out.println("invalid command");
    }

    private void loginUser(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        try {
            LoginController.loginUser(username,password);
        } catch (InvalidLoginException e) {
            System.err.println(e.getMessage());
        }
    }

    private void registerUser(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String nickname = matcher.group("nickname");
        try {
            LoginController.registerUser(username,password,password);
        } catch (RepetitiveUsernameException | RepetitiveNicknameException | WeakPasswordException e) {
            System.err.println(e.getMessage());
        }
    }
}