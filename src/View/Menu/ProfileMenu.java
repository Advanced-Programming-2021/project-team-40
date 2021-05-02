package View.Menu;

import Database.User;
import View.Exceptions.InvalidPasswordException;
import View.Exceptions.RepetitiveNicknameException;
import View.Exceptions.RepetitivePasswordException;
import Controller.DatabaseController.UserController;
import Controller.ProgramController.Regex;

import java.util.regex.Matcher;

public class ProfileMenu {
    User currentUser;
    private final UserController userController = new UserController();

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.changeNickname)).matches()) {
            try {
                userController.changeNickname(matcher, currentUser);
            } catch (RepetitiveNicknameException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.changePassword)).matches()) {
            try {
                userController.changePassword(matcher, currentUser);
            } catch (InvalidPasswordException | RepetitivePasswordException e) {
                System.out.println(e.getMessage());
            }
        } else System.out.println("invalid command");
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}