package Menu;

import Database.User;
import Exceptions.InvalidPasswordException;
import Exceptions.RepetitiveNicknameException;
import Exceptions.RepetitivePasswordException;
import MenuController.ProfileController;
import MenuController.ProgramController.Regex;

import java.util.regex.Matcher;

public class ProfileMenu {
    User currentUser;
    private final ProfileController profileController = new ProfileController();
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.changeNickname)).matches()) {
            try {
                profileController.changeNickname(matcher,currentUser);
            } catch (RepetitiveNicknameException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.changePassword)).matches()) {
            try {
                profileController.changePassword(matcher,currentUser);
            } catch (InvalidPasswordException | RepetitivePasswordException e) {
                System.out.println(e.getMessage());
            }
        }
        else System.out.println("invalid command");
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
