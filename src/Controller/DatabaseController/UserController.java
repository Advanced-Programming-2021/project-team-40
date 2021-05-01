package Controller.DatabaseController;

import Database.User;
import View.Exceptions.InvalidPasswordException;
import View.Exceptions.RepetitiveNicknameException;
import View.Exceptions.RepetitivePasswordException;

import java.util.regex.Matcher;

public class UserController {
    public void changePassword(Matcher matcher, User currentUser) throws RepetitivePasswordException, InvalidPasswordException {
        String currentPassword = matcher.group("currentPass");
        String newPassword = matcher.group("newPass");
        if (!currentUser.getPassword().equals(currentPassword)) throw new InvalidPasswordException();
        if (currentPassword.equals(newPassword)) throw new RepetitivePasswordException();
        currentUser.setPassword(newPassword);
    }

    public void changeNickname(Matcher matcher, User currentUser) throws RepetitiveNicknameException {
        String newNickname = matcher.group("nickname");
        if (User.getUserByNickname(newNickname) != null) throw new RepetitiveNicknameException(newNickname);
        currentUser.setNickname(newNickname);
    }

}