package MenuController;

import Database.User;
import Exceptions.InvalidPasswordException;
import Exceptions.RepetitiveNicknameException;
import Exceptions.RepetitivePasswordException;

import java.util.regex.Matcher;

public class ProfileController {
    public void changePassword(Matcher matcher, User currentUser) throws RepetitivePasswordException, InvalidPasswordException {
        String currentPassword = matcher.group("current_pass");
        String newPassword = matcher.group("new_pass");
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
