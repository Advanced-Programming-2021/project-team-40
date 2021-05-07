package View.Menu;



import Controller.MenuController.LoginController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.ProgramController;
import Controller.ProgramController.Regex;
import View.Exceptions.InvalidLoginException;
import View.Exceptions.RepetitiveNicknameException;
import View.Exceptions.RepetitiveUsernameException;
import View.Exceptions.WeakPasswordException;

import java.util.regex.Matcher;

public class LoginMenu implements Help{
    public void run(String command) {
        Matcher matcher;
        if (Regex.getCommandMatcher(command,Regex.help).matches()) help();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.createUser)).matches()) registerUser(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.login)).matches()) loginUser(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, "exit")).matches()) ProgramController.getInstance().setCurrentMenu(Menu.EXIT);
        else System.out.println("invalid command");
    }

    public void help() {
        System.out.println("user create --username <username> --password <password> --nickname <nickname>");
        System.out.println("user login --username <username> --password <password>");
        System.out.println("menu show-current");
        System.out.println("menu exit");
    }

    private void loginUser(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        try {
            LoginController.loginUser(username,password);
            System.out.println("user logged in successfully!");
        } catch (InvalidLoginException e) {
            System.err.println(e.getMessage());
        }
    }

    private void registerUser(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String nickname = matcher.group("nickname");
        try {
            LoginController.registerUser(username,password,nickname);
            System.out.println("user created successfully!");
        } catch (RepetitiveUsernameException | RepetitiveNicknameException | WeakPasswordException e) {
            System.err.println(e.getMessage());
        }
    }
}