package Menu;

import Database.User;
import Exceptions.InvalidPasswordException;
import Exceptions.RepetitiveNicknameException;
import Exceptions.RepetitivePasswordException;
import ProgramController.Regex;

import java.util.regex.Matcher;

public class ProfileMenu {
    User currentUser;

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.changeNickname)).matches()) {
            try {
                changeNickname(matcher);
            } catch (RepetitiveNicknameException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.changePassword)).matches()) {
            try {
                changePassword(matcher);
            } catch (InvalidPasswordException | RepetitivePasswordException e) {
                System.out.println(e.getMessage());
            }
        }
        else System.out.println("invalid command");
    }

    private void changePassword(Matcher matcher) throws RepetitivePasswordException, InvalidPasswordException {
        String currentPassword = matcher.group("current_pass");
        String newPassword = matcher.group("new_pass");
        if (!currentUser.getPassword().equals(currentPassword)) throw new InvalidPasswordException();
        else if (currentPassword.equals(newPassword)) throw new RepetitivePasswordException();
    }

    private void changeNickname(Matcher matcher) throws RepetitiveNicknameException {
        String newNickname = matcher.group("nickname");
        if (User.getUserByNickname(newNickname) != null) throw new RepetitiveNicknameException(newNickname);
        else currentUser.setNickname(newNickname);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
