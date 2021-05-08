package Controller.DatabaseController;

import Database.User;
import View.Exceptions.InvalidPasswordException;
import View.Exceptions.RepetitiveNicknameException;
import View.Exceptions.RepetitivePasswordException;

import javax.xml.crypto.Data;

public class UserController {
    private static UserController userController;

    private UserController() {
    }

    public static UserController getInstance() {
        if (userController == null)
            userController = new UserController();
        return userController;
    }

    public void changePassword(String currentPassword, String newPassword, User currentUser) throws RepetitivePasswordException, InvalidPasswordException {
        if (!currentUser.getPassword().equals(currentPassword)) throw new InvalidPasswordException();
        if (currentPassword.equals(newPassword)) throw new RepetitivePasswordException();
        currentUser.setPassword(newPassword);
        DatabaseController.getInstance().saveUser(currentUser);
    }

    public void changeNickname(String newNickname, User currentUser) throws RepetitiveNicknameException {
        if (User.getUserByNickname(newNickname) != null) throw new RepetitiveNicknameException(newNickname);
        currentUser.setNickname(newNickname);
        DatabaseController.getInstance().saveUser(currentUser);
    }

}