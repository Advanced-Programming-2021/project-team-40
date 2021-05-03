package main.java.View.Menu;

import main.java.Controller.ProgramController.Regex;
import main.java.View.Exceptions.InvalidLoginException;
import main.java.View.Exceptions.RepetitiveNicknameException;
import main.java.View.Exceptions.RepetitiveUsernameException;
import main.java.Controller.MenuController.LoginController;
import main.java.View.Exceptions.WeakPasswordException;

import java.util.regex.Matcher;

public class LoginMenu {
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.createUser)).matches()) {
            try {
                LoginController.registerUser(matcher);
                System.out.println("user created successfully!");
            } catch (RepetitiveUsernameException | RepetitiveNicknameException | WeakPasswordException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.login)).matches()) {
            try {
                LoginController.loginUser(matcher);
                System.out.println("user logged in successfully!");
            } catch (InvalidLoginException e) {
                System.out.println(e.getMessage());
            }
        } else System.out.println("invalid command");
    }
}