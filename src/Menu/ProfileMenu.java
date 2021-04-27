package Menu;

import Database.User;
import ProgramController.Exceptions.invalidPassword;
import ProgramController.Exceptions.repetitiveNickname;
import ProgramController.Exceptions.repetitivePassword;
import ProgramController.Regex;

import java.util.regex.Matcher;

public class ProfileMenu {
    User currentUser;

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.changeNickname)).matches()) {
            try {
                changeNickname(matcher);
            } catch (repetitiveNickname e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.changePassword)).matches()) {
            try {
                changePassword(matcher);
            } catch (invalidPassword | repetitivePassword e) {
                System.out.println(e.getMessage());
            }
        }
        else if (Regex.getCommandMatcher(command, Regex.showCurrentMenu).matches()) {
            System.out.println("Profile");
        }
        else System.out.println("invalid command");
    }

    private void changePassword(Matcher matcher) throws repetitivePassword, invalidPassword {
        String currentPassword = matcher.group("current_pass");
        String newPassword = matcher.group("new_pass");
        if (!currentUser.getPassword().equals(currentPassword)) throw new invalidPassword();
        else if (currentPassword.equals(newPassword)) throw new repetitivePassword();
    }

    private void changeNickname(Matcher matcher) throws repetitiveNickname {
        String newNickname = matcher.group("nickname");
        if (User.getUserByNickname(newNickname) != null) throw new repetitiveNickname(newNickname);
        else currentUser.setNickname(newNickname);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
