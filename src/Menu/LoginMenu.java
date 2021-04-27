package Menu;

import Database.User;
import ProgramController.*;
import ProgramController.Exceptions.invalidLogin;
import ProgramController.Exceptions.repetitiveNickname;
import ProgramController.Exceptions.repetitiveUsername;

import java.util.regex.Matcher;

import static ProgramController.ProgramController.currentMenu;
import static ProgramController.ProgramController.mainMenu;

public class LoginMenu {
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.createUser)).matches()) {
            try {
                registerUser(matcher);
            } catch (repetitiveUsername | repetitiveNickname e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.login)).matches()) {
            try {
                loginUser(matcher);
            } catch (invalidLogin e) {
                System.out.println("Username and password didn't match!");
            }
        } else if (Regex.getCommandMatcher(command, Regex.showCurrentMenu).matches())
            System.out.println("Login Menu");
        else System.out.println("invalid command");
    }

    private static void loginUser(Matcher matcher) throws invalidLogin {
        String username = matcher.group("username");
        String password = matcher.group("nickname");
        if (User.getUserByName(username) == null) throw new invalidLogin();
        User currentUser = User.getUserByName(username);
        if (!currentUser.getPassword().equals(password)) throw new invalidLogin();
        currentMenu = Menu.MAIN_MENU;
        mainMenu.setCurrentUser(currentUser);
        //TODO success message missing
    }

    private static void registerUser(Matcher matcher) throws repetitiveUsername, repetitiveNickname {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String nickname = matcher.group("nickname");
        if (User.getUserByName(username) != null) throw new repetitiveUsername(username);
        if (User.getUserByNickname(nickname) != null) throw new repetitiveNickname(nickname);
        new User(username, password, nickname);
        //TODO success message missing
    }
}
