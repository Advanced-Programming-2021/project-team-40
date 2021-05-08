package View.Menu;


import Controller.DatabaseController.UserController;
import Controller.MenuController.MenuNavigationController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.Regex;
import Database.User;
import View.Exceptions.InvalidPasswordException;
import View.Exceptions.RepetitiveNicknameException;
import View.Exceptions.RepetitivePasswordException;

import java.util.regex.Matcher;

public class ProfileMenu implements Help{
    private static ProfileMenu profileMenu;
    User currentUser;

    private ProfileMenu(){

    }

    public static ProfileMenu getInstance(){
        if (profileMenu == null) profileMenu = new ProfileMenu();
        return profileMenu;
    }

    public void run(String command) {
        Matcher matcher;
        if (Regex.getCommandMatcher(command,Regex.help).matches()) help();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.changeNickname)).matches()) changeNickname(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.changePassword)).matches()) changePassword(matcher);
        else if (Regex.getCommandMatcher(command, Regex.exitMenu).matches())
            MenuNavigationController.getInstance().toUpperMenu(Menu.PROFILE_MENU);
        else System.err.println("invalid command");
    }

    public void help() {
        System.out.println("menu exit");
        System.out.println("menu show-current");
        System.out.println("profile change --nickname <nickname>");
        System.out.println("profile change --password --current <current password> --new <new password>");
    }

    private void changeNickname(Matcher matcher) {
        String newNickname = matcher.group("nickname");
        try {
            UserController.getInstance().changeNickname(newNickname,currentUser);
            System.out.println("nickname changed successfully!");
        } catch (RepetitiveNicknameException e) {
            System.err.println(e.getMessage());
        }
    }

    private void changePassword(Matcher matcher) {
        String currentPassword = matcher.group("currentPass");
        String newPassword = matcher.group("newPass");
        try {
            UserController.getInstance().changePassword(currentPassword,newPassword,currentUser);
            System.out.println("password changed successfully!");
        } catch (RepetitivePasswordException | InvalidPasswordException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}