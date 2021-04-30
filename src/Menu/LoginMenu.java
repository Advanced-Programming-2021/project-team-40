package Menu;

import MenuController.ProgramController.Regex;
import Exceptions.InvalidLoginException;
import Exceptions.RepetitiveNicknameException;
import Exceptions.RepetitiveUsernameException;
import MenuController.LoginController;

import java.util.regex.Matcher;

public class LoginMenu {
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.createUser)).matches()) {
            try {
                LoginController.registerUser(matcher);
                System.out.println("user created successfully!");
            } catch (RepetitiveUsernameException | RepetitiveNicknameException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.login)).matches()) {
            try {
                LoginController.loginUser(matcher);
                System.out.println("user logged in successfully!");
            } catch (InvalidLoginException e) {
                System.out.println(e.getMessage());
            }
        }
        else System.out.println("invalid command");
    }
}
