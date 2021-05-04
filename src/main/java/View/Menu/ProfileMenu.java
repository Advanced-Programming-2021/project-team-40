package View.Menu;


import Controller.DatabaseController.UserController;
import Controller.ProgramController.Regex;
import Database.User;
import View.Exceptions.InvalidPasswordException;
import View.Exceptions.RepetitiveNicknameException;
import View.Exceptions.RepetitivePasswordException;

import java.util.regex.Matcher;

public class ProfileMenu {
    User currentUser;

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.changeNickname)).matches()) changeNickname(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.changePassword)).matches()) changePassword(matcher);
        else System.out.println("invalid command");
    }

    private void changeNickname(Matcher matcher) {
        String newNickname = matcher.group("nickname");
        try {
            UserController.getInstance().changeNickname(newNickname,currentUser);
        } catch (RepetitiveNicknameException e) {
            System.err.println(e.getMessage());
        }
    }

    private void changePassword(Matcher matcher) {
        String currentPassword = matcher.group("currentPass");
        String newPassword = matcher.group("newPass");
        try {
            UserController.getInstance().changePassword(currentPassword,newPassword,currentUser);
        } catch (RepetitivePasswordException | InvalidPasswordException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}