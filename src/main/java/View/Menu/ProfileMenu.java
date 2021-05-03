package main.java.View.Menu;

import main.java.Database.User;
import main.java.View.Exceptions.InvalidPasswordException;
import main.java.View.Exceptions.RepetitiveNicknameException;
import main.java.View.Exceptions.RepetitivePasswordException;
import main.java.Controller.DatabaseController.UserController;
import main.java.Controller.ProgramController.Regex;

import java.util.regex.Matcher;

public class ProfileMenu {
    User currentUser;

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.changeNickname)).matches()) {
            try {
                UserController.getInstance().changeNickname(matcher, currentUser);
            } catch (RepetitiveNicknameException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.changePassword)).matches()) {
            try {
                UserController.getInstance().changePassword(matcher, currentUser);
            } catch (InvalidPasswordException | RepetitivePasswordException e) {
                System.out.println(e.getMessage());
            }
        } else System.out.println("invalid command");
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}