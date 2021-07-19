package Controller;

import Controller.DatabaseController.DatabaseController;
import Controller.Exceptions.*;
import Database.User;

public class ProfileController {

    public static String changePassword(String currentPassword, String newPassword, User currentUser) throws RepetitivePasswordException, InvalidPasswordException, WeakPasswordException {
        if (!currentUser.getPassword().equals(currentPassword)) throw new InvalidPasswordException();
        if (currentPassword.equals(newPassword)) throw new RepetitivePasswordException();
        if (passwordIsWeak(newPassword)) throw new WeakPasswordException();
        currentUser.setPassword(newPassword);
        DatabaseController.getInstance().saveUser(currentUser);
        return "SUCCESS";
    }

    private static boolean passwordIsWeak(String newPassword) {
        return !newPassword.matches(Regex.strongPassword);
    }

    public static String changeNickname(String newNickname, User currentUser) throws RepetitiveNicknameException {
        if (User.getUserByNickname(newNickname) != null) throw new RepetitiveNicknameException(newNickname);
        currentUser.setNickname(newNickname);
        DatabaseController.getInstance().saveUser(currentUser);
        return "SUCCESS";
    }

}
