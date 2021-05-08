package View.Menu;


import Controller.MenuController.ProfileMenuController;
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
        else if (command.matches(Regex.showCurrentMenu)) System.out.println(Menu.PROFILE_MENU.toString());
        else if (command.matches(Regex.exitMenu)) ProfileMenuController.getInstance().toUpperMenu();
        else if (command.matches(Regex.menuNavigation)) ProfileMenuController.getInstance().toLowerMenu("");
        else if ((matcher = Regex.getCommandMatcher(command, Regex.changeNickname)).matches()) changeNickname(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.changePassword)).matches()) changePassword(matcher);
        else System.out.println("invalid command");
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
            ProfileMenuController.getInstance().changeNickname(newNickname,currentUser);
            System.out.println("nickname changed successfully!");
        } catch (RepetitiveNicknameException e) {
            System.out.println(e.getMessage());
        }
    }

    private void changePassword(Matcher matcher) {
        String currentPassword = matcher.group("currentPass");
        String newPassword = matcher.group("newPass");
        try {
            ProfileMenuController.getInstance().changePassword(currentPassword,newPassword,currentUser);
            System.out.println("password changed successfully!");
        } catch (RepetitivePasswordException | InvalidPasswordException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}